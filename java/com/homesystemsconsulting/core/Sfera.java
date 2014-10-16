package com.homesystemsconsulting.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.homesystemsconsulting.apps.Application;
import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.events.Bus;
import com.homesystemsconsulting.events.EventsMonitor;
import com.homesystemsconsulting.events.SystemEvent;
import com.homesystemsconsulting.script.EventsScriptEngine;
import com.homesystemsconsulting.util.logging.SystemLogger;


public class Sfera {
	
	public static final String VERSION = "0.0.1";
	
	public static final Charset CHARSET = Charset.forName("UTF-8");

	private static final BufferedReader STD_INPUT = new BufferedReader(new InputStreamReader(System.in));
	private static boolean run = true;
	
	private static List<PluginSchema> plugins;
	
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
			
			Configuration.load();
			
			SystemLogger.setup();
			SystemLogger.SYSTEM.info("STARTED - version " + VERSION);
			
			SystemNode.init();
			
			// TODO Maybe we don't need a general database... maybe just a "Variables" module
//			Database.init();
			
			loadPlugins();
			loadDrivers();
			loadApplications();
			loadScripts();
			
			Bus.register(EventsMonitor.INSTANCE);
			
			EventsScriptEngine.loadScriptFiles();
			
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
			} catch (Exception e) {}
			
			Bus.post(new SystemEvent("quit", null));
			
			for (final Driver d : drivers) {
				d.quit();
			}
			
			SystemLogger.SYSTEM.debug("Waiting 5 seconds for modules to quit...");
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
			
			SystemLogger.SYSTEM.debug("Shutting down remaining threads...");
			
			FilesWatcher.quit();
			
			try {
				TasksManager.DEFAULT.getExecutorService().shutdownNow();
				TasksManager.DEFAULT.getExecutorService().awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {}
			
//			TODO Database.close();
			
			SystemLogger.SYSTEM.info("Bye!");
			System.exit(0);
			
		} catch (Throwable t) {
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
				try {
					String appClassName = null;
					for (PluginSchema ps : plugins) {
						if ((appClassName = ps.getApplicationClass(appName)) != null) {
							Class<?> appClass = Class.forName(appClassName);
							Constructor<?> constructor = appClass.getConstructor(new Class[]{String.class});
							Object appInstance = constructor.newInstance(appName);
							if (appInstance instanceof Application) {
								applications.add((Application) appInstance);
								if (appInstance instanceof EventListener) {
									Bus.register((EventListener) appInstance);
								}
								SystemLogger.SYSTEM.info("apps", "app '" + appName + "' loaded");
							}
							
							break;
						}
					}
					
					if (appClassName == null) {
						SystemLogger.SYSTEM.error("apps", "app '" + appName + "' not found");
					}
				} catch (Throwable e) {
					SystemLogger.SYSTEM.error("apps", "error instantiating app '" + appName + "': " + e);
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
						Set<String> driverClasses = null;
						for (PluginSchema ps : plugins) {
							if ((driverClasses = ps.getDriverClasses(driverType)) != null) {
								for (String driverClassName : driverClasses) {
									Class<?> driverClass = Class.forName(driverClassName);
									Constructor<?> constructor = driverClass.getConstructor(new Class[]{String.class});
									Object driverInstance = constructor.newInstance(propName);
									if (driverInstance instanceof Driver) {
										drivers.add((Driver) driverInstance);
										EventsScriptEngine.addDriver((Driver) driverInstance);
										SystemLogger.SYSTEM.info("drivers", "driver '" + propName + "' of type '" + driverType + "' instantiated");
									}
								}
								
								break;
							}
						}
						
						if (driverClasses == null) {
							SystemLogger.SYSTEM.error("drivers", "driver '" + driverType + "' not found");
						}
					} catch (Throwable e) {
						SystemLogger.SYSTEM.error("drivers", "error instantiating driver '" + propName + "' of type '" + driverType + "': " + e);
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private static void checkStandardInput() {
		try {
			if (STD_INPUT.ready()) {
				String cmd;
				while(STD_INPUT.ready() && ((cmd = STD_INPUT.readLine()) != null)) {
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
		} catch (Exception e) {}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private static void loadPlugins() throws IOException {
		plugins = new ArrayList<PluginSchema>();
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("plugins"))) {
		    for (Path file : stream) {
		    	if (!Files.isHidden(file)) {
			    	try {
			    		PluginSchema ps = new PluginSchema(file);
			    		plugins.add(ps);
			    	} catch (Exception e) {
			    		SystemLogger.SYSTEM.warning("Error loading file " + file + " in plugins folder - " + e);
			    	}
		    	}
		    }
		} catch (NoSuchFileException e) {}
	}
}
