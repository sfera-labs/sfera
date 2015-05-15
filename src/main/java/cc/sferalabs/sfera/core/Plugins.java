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

public abstract class Plugins {
	
	private static ConcurrentHashMap<String, Plugin> plugins;
	private static final Logger logger = LogManager.getLogger();
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void load() throws IOException {
		plugins = new ConcurrentHashMap<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths
				.get("plugins"))) {
			for (Path file : stream) {
				try {
					if (!Files.isHidden(file)) {
						Plugin p = new Plugin(file);
						plugins.put(p.getId(), p);
						logger.info("Plugin '{}' loaded", p.getId());
					}
				} catch (Exception e) {
					logger.error("Error loading file '" + file
							+ "' in plugins folder", e);
				}
			}
		} catch (NoSuchFileException e) {
			logger.debug("No plugins directory found");
		}
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
