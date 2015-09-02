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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.Sfera;
import cc.sferalabs.sfera.core.SystemClassLoader;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.script.ScriptsEngine;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Drivers {

	private static final String DEFAULT_DRIVERS_PACKAGE = Sfera.BASE_PACKAGE + ".drivers";
	private static final String CONFIG_DIR = "drivers";
	private static final Logger logger = LogManager.getLogger();

	private static Map<String, Driver> drivers = new HashMap<>();

	/**
	 * 
	 */
	public synchronized static void load() {
		instantiateDrivers();
		try {
			FilesWatcher.register(Configuration.getConfigDir().resolve(CONFIG_DIR),
					Drivers::instantiateDrivers, false);
		} catch (Exception e) {
			logger.error("Error watching drivers config directory", e);
		}
	}

	/**
	 * 
	 */
	private static void instantiateDrivers() {
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
							Class<?> clazz = SystemClassLoader.getClass(driverClass);
							Constructor<?> constructor = clazz
									.getConstructor(new Class[] { String.class });
							Driver driverInstance = (Driver) constructor.newInstance(driverId);
							driverInstance.setConfigFile(CONFIG_DIR + "/" + fileName);
							drivers.put(driverId, driverInstance);
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

		Iterator<Driver> it = drivers.values().iterator();
		while (it.hasNext()) {
			Driver d = it.next();
			if (!inConfig.contains(d.getId())) {
				logger.info("Configuration file for driver '{}' deleted", d.getId());
				d.quit();
				it.remove();
			}
		}
	}

	/**
	 * 
	 */
	public synchronized static void quit() {
		for (final Driver d : drivers.values()) {
			d.quit();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public synchronized static Driver getDriver(String id) {
		return drivers.get(id);
	}

}
