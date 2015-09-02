package cc.sferalabs.sfera.apps;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.Sfera;
import cc.sferalabs.sfera.core.SystemClassLoader;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.events.Bus;

/**
 * Utility class for managing applications
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Applications {

	private static final String DEFAULT_APPS_PACKAGE = Sfera.BASE_PACKAGE + ".apps";
	private static final String CONFIG_DIR = "apps";
	private static final Logger logger = LogManager.getLogger();

	private static List<Application> applications = new ArrayList<>();

	/**
	 * Instantiates the configured applications and register the applications
	 * configuration director to be monitored for changes
	 */
	public synchronized static void load() {
		instantiateApps();
		try {
			FilesWatcher.register(Configuration.getConfigDir().resolve(CONFIG_DIR),
					Applications::instantiateApps, false);
		} catch (Exception e) {
			logger.error("Error watching drivers config directory", e);
		}
	}

	/**
	 * Instantiates configured applications
	 */
	private static void instantiateApps() {
		Path configDir = Configuration.getConfigDir().resolve(CONFIG_DIR);
		List<String> inConfig = new ArrayList<String>();
		if (Files.exists(configDir)) {
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(configDir)) {
				for (Path file : stream) {
					if (Files.isRegularFile(file) && !Files.isHidden(file)) {
						String fileName = file.getFileName().toString();
						String appClass = fileName;
						if (appClass.endsWith(Configuration.FILE_EXTENSION)) {
							appClass = appClass.substring(0,
									appClass.length() - Configuration.FILE_EXTENSION.length());
						}
						if (!appClass.contains(".")) {
							appClass = DEFAULT_APPS_PACKAGE + "." + appClass.toLowerCase() + "."
									+ appClass;
						}
						inConfig.add(appClass);
						try {
							if (appAlreadyInstantiated(appClass)) {
								continue;
							}
							Class<?> clazz = SystemClassLoader.getClass(appClass);
							Constructor<?> constructor = clazz.getConstructor();
							Application appInstance = (Application) constructor.newInstance();
							appInstance.setConfigFile(CONFIG_DIR + "/" + fileName);
							applications.add(appInstance);
							if (appInstance instanceof EventListener) {
								Bus.register((EventListener) appInstance);
							}
							logger.info("App '{}' loaded", appClass);
							appInstance.enable();
						} catch (Throwable e) {
							logger.error("Error instantiating app: " + appClass, e);
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException("Error loading apps config", e);
			}
		} else {
			logger.debug("Apps config directory not found");
		}

		Iterator<Application> it = applications.iterator();
		while (it.hasNext()) {
			Application a = it.next();
			String appClass = a.getClass().getName();
			if (!inConfig.contains(appClass)) {
				logger.info("Configuration file for app '{}' deleted", appClass);
				a.disable();
				it.remove();
			}
		}
	}

	/**
	 * 
	 * @param appClass
	 * @return
	 */
	private static boolean appAlreadyInstantiated(String appClass) {
		for (Application a : applications) {
			if (a.getClass().getName().equals(appClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Disables all the running applications
	 */
	public synchronized static void disable() {
		for (Application a : applications) {
			a.disable();
		}
	}

}
