package cc.sferalabs.sfera.core;

import java.io.BufferedReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Plugin {

	final static String PLUGIN_PROPERTIES_PATH = "META-INF/sfera/plugin.properties";

	private final Path path;
	private final String id;
	private final Properties properties;

	/**
	 * 
	 * @param jarFile
	 * @throws Exception
	 */
	public Plugin(Path jarFile) throws Exception {
		if (!Files.isRegularFile(jarFile)) {
			throw new Exception("not a regular file");
		}
		if (!jarFile.getFileName().toString().endsWith(".jar")) {
			throw new Exception("not a jar file");
		}

		this.path = jarFile;
		properties = new Properties();
		try (FileSystem pluginFileSystem = FileSystems.newFileSystem(jarFile,
				null)) {
			try (BufferedReader br = Files.newBufferedReader(pluginFileSystem
					.getPath(PLUGIN_PROPERTIES_PATH))) {
				properties.load(br);
				this.id = properties.getProperty("id");
				if (this.id == null) {
					throw new Exception("id not found");
				}
			}
		} catch (Exception e) {
			throw new Exception("error reading plugin properties", e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
