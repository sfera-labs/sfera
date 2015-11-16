package cc.sferalabs.sfera.drivers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.PluginsClassLoader;
import cc.sferalabs.sfera.core.Sfera;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.core.services.console.Console;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Nodes;
import cc.sferalabs.sfera.script.ScriptsEngine;

/**
 * Utility class for managing drivers.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Drivers {

	private static final String DEFAULT_DRIVERS_PACKAGE = Sfera.BASE_PACKAGE + ".drivers";
	private static final String CONFIG_DIR = "drivers";
	private static final Logger logger = LoggerFactory.getLogger(Drivers.class);

	private static final Map<String, Driver> drivers = new HashMap<>();

	/**
	 * Instantiate and starts all drivers defined in configuration.
	 */
	public synchronized static void load() {
		instantiateDrivers();
		try {
			FilesWatcher.register(Configuration.getConfigDir().resolve(CONFIG_DIR),
					Drivers::instantiateDrivers, false);
		} catch (Exception e) {
			logger.error("Error watching drivers config directory", e);
		}
		Console.setHandler("drivers", DriversConsoleCommandHandler.INSTANCE);
	}

	/**
	 * 
	 */
	private synchronized static void instantiateDrivers() {
		Path configDir = Configuration.getConfigDir().resolve(CONFIG_DIR);
		List<String> inConfig = new ArrayList<String>();
		if (Files.exists(configDir)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(configDir)) {
				for (Path file : stream) {
					if (Files.isRegularFile(file) && !Files.isHidden(file)) {
						String fileName = file.getFileName().toString();
						String driverId = fileName.replace(Configuration.FILE_EXTENSION, "");
						inConfig.add(driverId);
						try {
							if (drivers.containsKey(driverId)) {
								continue;
							}
							Configuration config = new Configuration(file);
							String driverClass = config.get("type", null);
							if (driverClass == null) {
								throw new Exception("type not specified in configuration");
							}
							if (!driverClass.contains(".")) {
								driverClass = DEFAULT_DRIVERS_PACKAGE + "."
										+ driverClass.toLowerCase() + "." + driverClass;
							}
							Class<?> clazz = PluginsClassLoader.getClass(driverClass);
							Constructor<?> constructor = clazz
									.getConstructor(new Class[] { String.class });
							Driver driverInstance = (Driver) constructor.newInstance(driverId);
							driverInstance.setConfigFile(CONFIG_DIR + "/" + fileName);
							addDriver(driverId, driverInstance);
							ScriptsEngine.putObjectInGlobalScope(driverInstance.getId(),
									driverInstance);
							if (driverInstance instanceof EventListener) {
								Bus.register((EventListener) driverInstance);
							}
							logger.info("Driver '{}' of type '{}' instantiated", driverId,
									driverClass);
							driverInstance.start();
						} catch (Throwable e) {
							logger.error("Error instantiating driver '" + driverId + "'", e);
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException("Error loading drivers config", e);
			}
		} else {
			logger.debug("Drivers config directory not found");
		}

		List<Driver> toRemove = new ArrayList<>();
		Iterator<Driver> it = drivers.values().iterator();
		while (it.hasNext()) {
			Driver d = it.next();
			if (!inConfig.contains(d.getId())) {
				logger.info("Configuration file for driver '{}' deleted", d.getId());
				toRemove.add(d);
			}
		}
		
		for (Driver d : toRemove) {
			d.quit();
			removeDriver(d);
		}
	}

	/**
	 * @param d
	 */
	private static void removeDriver(Driver d) {
		String id = d.getId();
		drivers.remove(id);
		Nodes.remove(id);
	}

	/**
	 * @param driverId
	 * @param driverInstance
	 */
	private static void addDriver(String driverId, Driver driverInstance) {
		drivers.put(driverId, driverInstance);
		Nodes.put(driverInstance);
	}

	/**
	 * Quits all the drivers.
	 */
	public synchronized static void quit() {
		for (final Driver d : drivers.values()) {
			d.quit();
		}
	}

	/**
	 * Returns the driver instance with the specified ID.
	 * 
	 * @param id
	 *            the ID of the driver to be returned
	 * @return the driver instance with the specified ID or {@code null} if no
	 *         such driver is found
	 */
	public synchronized static Driver getDriver(String id) {
		return drivers.get(id);
	}

}
