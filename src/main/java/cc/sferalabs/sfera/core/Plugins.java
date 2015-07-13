package cc.sferalabs.sfera.core;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.events.PluginsEvent;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.events.Bus;

public abstract class Plugins {

	private static final String DIR_PATH = "plugins";
	private static ConcurrentHashMap<String, Plugin> plugins;
	private static final Logger logger = LogManager.getLogger();

	/**
	 * 
	 */
	public static void load() {
		doLoad();
		try {
			FilesWatcher.register(Paths.get(DIR_PATH), Plugins::doLoad, false);
		} catch (Exception e) {
			logger.error("Error watching plugins directory", e);
		}
	}

	/**
	 * 
	 */
	private static void doLoad() {
		plugins = new ConcurrentHashMap<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(DIR_PATH))) {
			for (Path file : stream) {
				try {
					if (!Files.isHidden(file)) {
						Plugin p = new Plugin(file);
						plugins.put(p.getId(), p);
						logger.info("Plugin '{}' loaded", p.getId());
					}
				} catch (Exception e) {
					logger.error("Error loading file '" + file + "' in plugins folder", e);
				}
			}
		} catch (NoSuchFileException e) {
			logger.debug("Plugins directory not found");
		} catch (IOException e) {
			logger.error("Error loading plugins", e);
			return;
		}
		Bus.post(PluginsEvent.RELOAD);
	}

	/**
	 * 
	 * @return
	 */
	public static Map<String, Plugin> getAll() {
		return new HashMap<String, Plugin>(plugins);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Plugin get(String id) {
		return plugins.get(id);
	}

}
