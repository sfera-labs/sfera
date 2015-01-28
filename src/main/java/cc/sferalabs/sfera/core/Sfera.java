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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import cc.sferalabs.sfera.apps.Application;
import cc.sferalabs.sfera.drivers.Driver;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.SystemEvent;
import cc.sferalabs.sfera.script.ScriptsEngine;

public class Sfera {

	static {
		try {
			System.setProperty("java.awt.headless", "true");
			Path log4j2Config = Configuration.CONFIG_DIR.resolve("log4j2.xml");
			if (Files.exists(log4j2Config)) {
				System.setProperty("log4j.configurationFile", log4j2Config.toString());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static final BufferedReader STD_INPUT = new BufferedReader(
			new InputStreamReader(System.in));
	private static boolean run = true;

	private static ConcurrentHashMap<String, Plugin> plugins;

	private static List<Driver> drivers;
	private static List<Application> applications;

	private static final Logger logger = LogManager.getLogger();
	public static final Marker SFERA_MARKER = MarkerManager.getMarker("SFERA");

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Thread.setDefaultUncaughtExceptionHandler(new SystemExceptionHandler());

			init();
			while (run) {
				checkStandardInput();
			}
			quit();

		} catch (RuntimeException t) {
			logger.catching(Level.FATAL, t);
		}

		logger.warn("Quitted");
		System.exit(0);
	}

	/**
	 * 
	 */
	private static void init() {
		try {
			Configuration.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		logger.info("STARTED");

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
			logger.error("Error loading script files");
			logger.catching(e);
		}

		for (final Driver d : drivers) {
			d.start();
		}

		TasksManager.DEFAULT.submit(FilesWatcher.INSTANCE);
	}

	/**
	 * 
	 */
	private static void quit() {
		logger.warn("System stopped");
		logger.info("Quitting modules...");

		Bus.post(new SystemEvent("quit", null));

		try {
			STD_INPUT.close();
		} catch (Exception e) {
		}

		for (final Driver d : drivers) {
			d.disable();
		}

		logger.debug("Waiting 5 seconds for modules to quit...");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		logger.debug("Shutting down remaining threads...");

		FilesWatcher.quit();

		try {
			TasksManager.DEFAULT.getExecutorService().shutdownNow();
			TasksManager.DEFAULT.getExecutorService().awaitTermination(5,
					TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}

		// TODO Database.close();
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
					Object appInstance = getModuleInstance(appName, null,
							"appClass");
					if (appInstance instanceof Application) {
						applications.add((Application) appInstance);
						if (appInstance instanceof EventListener) {
							Bus.register((EventListener) appInstance);
						}
						logger.info("App '{}' loaded", appName);
					}
				} catch (Throwable e) {
					logger.error("Error instantiating app '{}'", appName);
					logger.catching(e);
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
				// it's a driver instance definition
				String driverType = props.getProperty(propName);
				if (driverType != null) {
					try {
						Object driverInstance = getModuleInstance(driverType,
								propName, "driverClass");
						if (driverInstance instanceof Driver) {
							Driver d = (Driver) driverInstance;
							drivers.add(d);
							ScriptsEngine.putInGlobalScope(d.getId(), d);
							if (d instanceof EventListener) {
								Bus.register((EventListener) d);
							}
							logger.info(
									"Driver '{}' of type '{}' instantiated",
									propName, driverType);
						}
					} catch (Throwable e) {
						logger.error(
								"Error instantiating driver '{}' of type '{}'",
								propName, driverType);
						logger.catching(e);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param type
	 * @param id
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	private static Object getModuleInstance(String type, String id,
			String propertyName) throws Exception {
		Plugin plugin = plugins.get(type);
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
		if (id == null) {
			return constructor.newInstance();
		} else {
			return constructor.newInstance(id);
		}
	}

	/**
	 * 
	 */
	private static void checkStandardInput() {
		try {
			String cmd;
			if ((cmd = STD_INPUT.readLine()) != null) {
				switch (cmd.trim().toLowerCase()) {
				case "quit":
					run = false;
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ie) {
			}
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
					logger.error("Error loading file {} in plugins folder",
							file);
					logger.catching(e);
				}
			}
		} catch (NoSuchFileException e) {
			logger.debug("No plugins directory found");
		} catch (IOException e) {
			logger.error("Error loading plugins");
			logger.catching(e);
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
