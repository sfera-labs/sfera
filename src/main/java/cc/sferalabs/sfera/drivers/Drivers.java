package cc.sferalabs.sfera.drivers;

import java.lang.reflect.Constructor;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.script.ScriptsEngine;

public abstract class Drivers {

	private static final String DEFAULT_DRIVERS_PACKAGE = SystemNode.BASE_PACKAGE
			+ ".drivers";
	private static final Logger logger = LogManager.getLogger();

	private static Map<String, Driver> drivers;

	/**
	 * 
	 */
	public synchronized static void load() {
		drivers = new HashMap<>();
		Properties props = Configuration.getProperties();
		for (String propName : props.stringPropertyNames()) {
			if (!propName.contains(".")) {
				String driverClass = props.getProperty(propName);
				try {
					if (!driverClass.contains(".")) {
						driverClass = DEFAULT_DRIVERS_PACKAGE + "."
								+ driverClass.toLowerCase() + "." + driverClass;
					}
					Class<?> clazz = Class.forName(driverClass);
					Constructor<?> constructor = clazz
							.getConstructor(new Class[] { String.class });
					Driver driverInstance = (Driver) constructor
							.newInstance(propName);
					drivers.put(driverInstance.getId(), driverInstance);
					ScriptsEngine.putObjectInGlobalScope(
							driverInstance.getId(), driverInstance);
					if (driverInstance instanceof EventListener) {
						Bus.register((EventListener) driverInstance);
					}
					logger.info("Driver '{}' of type '{}' instantiated",
							propName, driverClass);
				} catch (Throwable e) {
					logger.error("Error instantiating driver '" + propName
							+ "' of type '" + driverClass + "'", e);
				}
			}
		}
	}

	/**
	 * 
	 */
	public synchronized static void startAll() {
		for (final Driver d : drivers.values()) {
			d.start();
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
