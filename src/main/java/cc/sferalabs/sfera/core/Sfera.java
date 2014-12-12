package cc.sferalabs.sfera.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import cc.sferalabs.sfera.apps.Application;
import cc.sferalabs.sfera.drivers.Driver;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.SystemEvent;
import cc.sferalabs.sfera.script.ScriptsEngine;
import cc.sferalabs.sfera.util.logging.SystemLogger;

public class Sfera {

	private static final BufferedReader STD_INPUT = new BufferedReader(
			new InputStreamReader(System.in));
	private static boolean run = true;

	private static ConcurrentHashMap<String, Plugin> plugins;

	private static List<Driver> drivers;
	private static List<Application> applications;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		try {
			System.setProperty("java.awt.headless", "true");

			Thread.setDefaultUncaughtExceptionHandler(new SystemExceptionHandler());

			try {
				Configuration.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			try {
				SystemLogger.setup();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			SystemLogger.SYSTEM.info("STARTED");

			SystemNode.init();

			Bus.register(ScriptsEngine.INSTANCE);

			// TODO Maybe we don't need a general database... maybe just a
			// "Variables" module
			// Database.init();

			loadPlugins();
			loadDrivers();
			loadApplications();
			try {
				ScriptsEngine.loadScriptFiles();
			} catch (IOException e) {
				SystemLogger.SYSTEM.error("error loading script files: " + e);
				e.printStackTrace();
			}

			while (run) {
				for (final Driver d : drivers) {
					d.startIfNotActive();
				}

				try {
					FilesWatcher.watch(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					if (!run) {
						Thread.currentThread().interrupt();
						break;
					}
				}

				checkStandardInput();
			}

			SystemLogger.SYSTEM.warning("System stopped");
			SystemLogger.SYSTEM.info("Quitting modules...");

			try {
				STD_INPUT.close();
			} catch (Exception e) {
			}

			Bus.post(new SystemEvent("quit", null));

			for (final Driver d : drivers) {
				d.quit();
			}

			SystemLogger.SYSTEM
					.debug("Waiting 5 seconds for modules to quit...");

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

			SystemLogger.SYSTEM.debug("Shutting down remaining threads...");

			FilesWatcher.quit();

			try {
				TasksManager.DEFAULT.getExecutorService().shutdownNow();
				TasksManager.DEFAULT.getExecutorService().awaitTermination(5,
						TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}

			// TODO Database.close();

			SystemLogger.SYSTEM.info("Bye!");
			System.exit(0);

		} catch (RuntimeException t) {
			SystemLogger.SYSTEM.error("exception in main thread: " + t);
			t.printStackTrace();

		} finally {
			SystemLogger.close();
		}
	}

	/**
	 * 
	 */
	private static void loadApplications() {
		applications = new ArrayList<Application>();
		String appList = Configuration.SYSTEM.getProperty("apps", null);
		if (appList != null) {
			for (String appName : appList.split(",")) {
				appName = appName.trim();
				try {
					Object appInstance = getModuleInstance(appName, "appClass");
					if (appInstance instanceof Application) {
						applications.add((Application) appInstance);
						if (appInstance instanceof EventListener) {
							Bus.register((EventListener) appInstance);
						}
						SystemLogger.SYSTEM.info("apps", "app '" + appName
								+ "' loaded");
					}
				} catch (Throwable e) {
					SystemLogger.SYSTEM.error("apps",
							"error instantiating app '" + appName + "': " + e);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 */
	private static void loadDrivers() {
		drivers = new ArrayList<Driver>();
		Properties props = Configuration.getProperties();
		for (String propName : props.stringPropertyNames()) {
			if (propName.indexOf('.') < 0) {
				// it's a driver definition
				String driverType = props.getProperty(propName);
				if (driverType != null) {
					try {
						Object driverInstance = getModuleInstance(propName,
								"driverClass");
						if (driverInstance instanceof Driver) {
							Driver d = (Driver) driverInstance;
							drivers.add(d);
							ScriptsEngine.putInGlobalScope(d.getId(), d);
							if (d instanceof EventListener) {
								Bus.register((EventListener) d);
							}
							SystemLogger.SYSTEM.info("drivers", "driver '"
									+ propName + "' of type '" + driverType
									+ "' instantiated");
						}
					} catch (Throwable e) {
						SystemLogger.SYSTEM.error("drivers",
								"error instantiating driver '" + propName
										+ "' of type '" + driverType + "': "
										+ e);
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param moduleName
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	private static Object getModuleInstance(String moduleName,
			String propertyName) throws Exception {
		Plugin plugin = plugins.get(moduleName);
		String className;
		if (plugin != null) {
			className = plugin.getProperty(propertyName);
		} else {
			// when in development
			Properties p = new Properties();
			InputStream is = Sfera.class.getClassLoader().getResourceAsStream(
					Plugin.PLUGIN_PROPERTIES_PATH);
			if (is == null) {
				throw new NoSuchFileException(Plugin.PLUGIN_PROPERTIES_PATH);
			}
			p.load(is);
			className = p.getProperty(propertyName);
		}

		if (className == null) {
			throw new Exception(propertyName + " property not found");
		}

		Class<?> clazz = Class.forName(className);
		Constructor<?> constructor = clazz
				.getConstructor(new Class[] { String.class });
		return constructor.newInstance(moduleName);
	}

	/**
	 * 
	 */
	private static void checkStandardInput() {
		try {
			if (STD_INPUT.ready()) {
				String cmd;
				while (STD_INPUT.ready()
						&& ((cmd = STD_INPUT.readLine()) != null)) {
					cmd = cmd.trim().toLowerCase();
					switch (cmd) {
					case "quit":
						run = false;
						break;

					default:
						break;
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private static void loadPlugins() {
		plugins = new ConcurrentHashMap<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths
				.get("plugins"))) {
			for (Path file : stream) {
				try {
					if (!Files.isHidden(file)) {
						Plugin p = new Plugin(file);
						plugins.put(p.getId(), p);
					}
				} catch (Exception e) {
					SystemLogger.SYSTEM.warning("Error loading file " + file
							+ " in plugins folder - " + e);
				}
			}
		} catch (NoSuchFileException e) {
			// no plugins directory
		} catch (IOException e) {
			SystemLogger.SYSTEM.error("Error loading plugins - " + e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static Map<String, Plugin> getPlugins() {
		return plugins;
	}
}
