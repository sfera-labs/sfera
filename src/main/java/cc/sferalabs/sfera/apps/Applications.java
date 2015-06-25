package cc.sferalabs.sfera.apps;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.Sfera;
import cc.sferalabs.sfera.events.Bus;

public abstract class Applications {

	private static final String DEFAULT_APPS_PACKAGE = Sfera.BASE_PACKAGE
			+ ".apps";
	private static final Logger logger = LogManager.getLogger();

	private static List<Application> applications;

	/**
	 * 
	 */
	public static void load() {
		applications = new ArrayList<Application>();
		String appList = Configuration.getSystemConfig().getProperty("apps",
				null);
		if (appList != null) {
			for (String appClass : appList.split(",")) {
				appClass = appClass.trim();
				try {
					if (!appClass.contains(".")) {
						appClass = DEFAULT_APPS_PACKAGE + "."
								+ appClass.toLowerCase() + "." + appClass;
					}
					Class<?> clazz = Class.forName(appClass);
					Constructor<?> constructor = clazz.getConstructor();
					Application appInstance = (Application) constructor
							.newInstance();
					applications.add(appInstance);
					if (appInstance instanceof EventListener) {
						Bus.register((EventListener) appInstance);
					}
					logger.info("App '{}' loaded", appClass);
				} catch (Throwable e) {
					logger.error("Error instantiating app: " + appClass, e);
				}
			}
		}
	}

	/**
	 * 
	 */
	public synchronized static void enable() {
		for (final Application a : applications) {
			//TODO get app configuration
			a.onEnable(new Configuration(a.getClass().getName()));
		}
	}

	/**
	 * 
	 */
	public synchronized static void disable() {
		for (final Application a : applications) {
			a.onDisable();
		}
	}

}
