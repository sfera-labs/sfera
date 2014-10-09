package com.homesystemsconsulting.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import com.homesystemsconsulting.apps.Application;
import com.homesystemsconsulting.apps.myapp.MyApp;
import com.homesystemsconsulting.apps.myapp2.MyApp2;
import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.events.Bus;
import com.homesystemsconsulting.events.EventsMonitor;
import com.homesystemsconsulting.script.EventsScriptEngine;
import com.homesystemsconsulting.util.logging.SystemLogger;


public class Sfera {
	
	public static final String VERSION = "0.0.1";
	
	public static final Charset CHARSET = Charset.forName("UTF-8");

	private static boolean run = true;

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
			
			// Maybe we don't need a general database... maybe just a "Variables" module
//			Database.init();
			
			List<Driver> drivers = loadDrivers();
			for (Driver d : drivers) {
				EventsScriptEngine.addDriver(d);
			}
			
			//TODO initialize all applications...
			final Application myApp = new MyApp();
			final Application myApp2 = new MyApp2();
			
			Bus.register(EventsMonitor.INSTANCE);
			EventsScriptEngine.loadScriptFiles();
			
			//register all listeners...
			if (myApp instanceof EventListener) {
	//			Bus.register((EventListener) myApp);
			}
			if (myApp2 instanceof EventListener) {
	//			Bus.register((EventListener) myApp2);
			}
			
			while (run) {
				for (final Driver d : drivers) {
					d.startIfNotActive();
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					if (!run) {
						break;
					}
				}
			}
	
			SystemLogger.SYSTEM.warning("System stopped");
			SystemLogger.SYSTEM.info("Quitting modules...");
			
			for (final Driver d : drivers) {
				d.quit();
			}
			
			SystemLogger.SYSTEM.debug("Waiting 5 seconds for modules to quit...");
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
			
			SystemLogger.SYSTEM.debug("Shutting down remaining threads...");
			
			try {
				TasksManager.DEFAULT.getExecutorService().shutdownNow();
				TasksManager.DEFAULT.getExecutorService().awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {}
			
//			Database.close();
			
			SystemLogger.SYSTEM.info("Bye!");
			System.exit(0);
			
		} catch (Throwable t) {
			SystemLogger.SYSTEM.error("exception in main thread: " + t);
		} finally {
			SystemLogger.close();
		}
	}

	/**
	 * 
	 * @return
	 */
	private static List<Driver> loadDrivers() {
		List<Driver> drivers = new ArrayList<Driver>();
		
		Properties props = Configuration.getProperties();
		for (String propName : props.stringPropertyNames()) {
			if (propName.indexOf('.') < 0) {
				// it's a driver definition
				String driverType = props.getProperty(propName);
				if (driverType != null) {
					FileSystem pluginFileSystem = null;
					try {
						pluginFileSystem = FileSystems.newFileSystem(Paths.get("plugins/" + driverType + ".jar"), null);
					} catch (FileSystemNotFoundException e) {
						SystemLogger.SYSTEM.error("drivers", "plugin '" + driverType + "' not found");
					} catch (IOException e) {
						SystemLogger.SYSTEM.error("drivers", "error loading plugin '" + driverType + "': " + e);
					}
					
					if (pluginFileSystem != null) {
						Set<String> driverClasses;
						try {
							driverClasses = getDriverClassesInManifest(pluginFileSystem.getPath("manifest.xml"));
							
							for (String driverClassName : driverClasses) {
								try {
									Class<?> driverClass = Class.forName(driverClassName);
									Constructor<?> constructor = driverClass.getConstructor(new Class[]{String.class});
									Object driverInstance = constructor.newInstance(propName);
									if (driverInstance instanceof Driver) {
										drivers.add((Driver) driverInstance);
										SystemLogger.SYSTEM.info("drivers", "driver '" + propName + "' of type '" + driverClassName + "' instantiated");
									}
								} catch (Throwable e) {
									SystemLogger.SYSTEM.error("drivers", "error instantiating driver '" + propName + "' of type '" + driverClassName + "': " + e);
								}
							}
						} catch (Exception e) {
							SystemLogger.SYSTEM.error("drivers", "error reading manifest file in plugin '" + driverType + "': " + e);
						}
					}
				}
			}
		}
		
		return drivers;
	}

	/**
	 * 
	 * @param manifestPath
	 * @return
	 * @throws Exception
	 */
	private static Set<String> getDriverClassesInManifest(Path manifestPath) throws Exception {
		Set<String> classes = new HashSet<String>();
		
		XMLEventReader eventReader = null;
		try (BufferedReader in = Files.newBufferedReader(manifestPath, CHARSET)) {
			eventReader = XMLInputFactory.newInstance().createXMLEventReader(in);
			boolean inDriver = false;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					String tag = event.asStartElement().getName().getLocalPart();
					if (tag.equals("driver")) {
						inDriver = true;
						
					} else if (inDriver && tag.equals("class")) {
						event = eventReader.nextEvent();
			            classes.add(event.asCharacters().getData());
					}
				
				} else if (event.isEndElement()) {
					String tag = event.asEndElement().getName().getLocalPart();
					if (tag.equals("driver")) {
						inDriver = false;
					}
				}
			}
		} finally {
			try { eventReader.close(); } catch (Exception e) {}
		}
		
		return classes;
	}
}
