/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.drivers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.console.Console;
import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.PluginsClassLoader;
import cc.sferalabs.sfera.core.Sfera;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.util.files.FilesWatcher;

/**
 * Utility class for managing drivers.
 * 
 * @author Giampiero Baggiani
 *
 */
public abstract class Drivers {

	private static final String DEFAULT_DRIVERS_PACKAGE = Sfera.BASE_PACKAGE + ".drivers";
	private static final String CONFIG_DIR = "drivers";
	private static final Logger logger = LoggerFactory.getLogger(Drivers.class);

	private static final Map<String, Driver> drivers = new ConcurrentHashMap<>();

	/**
	 * Instantiate and starts all drivers defined in configuration.
	 */
	public synchronized static void load() {
		instantiateDrivers();
		try {
			FilesWatcher.register(Configuration.getConfigDir().resolve(CONFIG_DIR), "Drivers loucher",
					Drivers::instantiateDrivers, false, false);
		} catch (Exception e) {
			logger.error("Error watching drivers config directory", e);
		}
		Console.addHandler(DriversConsoleCommandHandler.INSTANCE);
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
						if (!fileName.endsWith(Configuration.FILE_EXTENSION)) {
							continue;
						}
						String driverId = fileName.substring(0,
								fileName.length() - Configuration.FILE_EXTENSION.length());
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
								driverClass = DEFAULT_DRIVERS_PACKAGE + "." + driverClass.toLowerCase() + "."
										+ driverClass;
							}
							Class<?> clazz = PluginsClassLoader.getClass(driverClass);
							Driver driverInstance = instantiateDriver(clazz, driverId);
							driverInstance.setConfigFile(CONFIG_DIR + "/" + fileName);
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
			if (!inConfig.contains(d.getId()) && d.getConfigFile() != null) {
				logger.info("Configuration file for driver '{}' deleted", d.getId());
				toRemove.add(d);
			}
		}

		for (Driver d : toRemove) {
			drivers.remove(d.getId());
			d.quit();
			d.destroy();
		}
	}

	/**
	 * 
	 * @param clazz
	 *            the driver class
	 * @param driverId
	 *            the driver ID
	 * @return the driver instance
	 * @throws NoSuchMethodException
	 *             if thrown when constructing the class
	 * @throws SecurityException
	 *             if thrown when instantiating the class
	 * @throws InstantiationException
	 *             if thrown when instantiating the class
	 * @throws IllegalAccessException
	 *             if thrown when instantiating the class
	 * @throws IllegalArgumentException
	 *             if thrown when instantiating the class
	 * @throws InvocationTargetException
	 *             if thrown when instantiating the class
	 */
	public synchronized static Driver instantiateDriver(Class<?> clazz, String driverId)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Constructor<?> constructor = clazz.getConstructor(new Class[] { String.class });
		Driver driverInstance = (Driver) constructor.newInstance(driverId);
		if (driverInstance instanceof EventListener) {
			Bus.register((EventListener) driverInstance);
		}
		drivers.put(driverId, driverInstance);
		logger.info("Driver '{}' of type '{}' instantiated", driverId, clazz.getName());
		return driverInstance;
	}

	/**
	 * Destroys the specified driver.
	 * 
	 * @param driverId
	 *            the driver ID
	 * @return true if driver was found, false otherwise
	 */
	public static boolean destroyDriver(String driverId) {
		Driver d = drivers.remove(driverId);
		if (d == null) {
			return false;
		}
		d.destroy();
		return true;
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
	 * Waits for the termination of all the drivers. If the timeout expires before
	 * termination a TimeoutException is thrown.
	 * 
	 * @param timeout
	 *            timeout in milliseconds
	 * @throws InterruptedException
	 *             if interrupted
	 */
	public synchronized static void waitTermination(long timeout) throws InterruptedException {
		for (final Driver d : drivers.values()) {
			if (timeout <= 0) {
				return;
			}
			long s = System.currentTimeMillis();
			try {
				d.waitTermination(timeout);
			} catch (TimeoutException e) {
			}
			timeout -= System.currentTimeMillis() - s;
		}
	}

	/**
	 * Returns the driver instance with the specified ID.
	 * 
	 * @param id
	 *            the ID of the driver to be returned
	 * @return the driver instance with the specified ID or {@code null} if no such
	 *         driver is found
	 */
	public synchronized static Driver getDriver(String id) {
		return drivers.get(id);
	}

	/**
	 * Returns a collection of all the driver instances.
	 * 
	 * @return a collection of all the driver instances
	 */
	public synchronized static Collection<Driver> getAll() {
		return new ArrayList<>(drivers.values());
	}

}
