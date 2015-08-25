package cc.sferalabs.sfera.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {

	private static final String CONFIG_DIR = "config";
	public static final String FILE_EXTENSION = ".yml";

	private final Path path;
	private final Object config;

	/**
	 * 
	 * @param configFile
	 * @throws IOException
	 */
	public Configuration(String configFile) throws IOException {
		this(getPath(configFile));
	}

	/**
	 * 
	 * @param configFile
	 * @throws IOException
	 */
	public Configuration(Path configFile) throws IOException {
		path = configFile;
		config = load();
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private Object load() throws IOException {
		Yaml yaml = new Yaml();
		try (BufferedReader r = Files.newBufferedReader(path)) {
			Object obj = yaml.load(r);
			return obj;
		}
	}

	/**
	 * 
	 * @param configFile
	 * @return
	 */
	public static Path getPath(String configFile) {
		return Paths.get(CONFIG_DIR).resolve(configFile);
	}

	/**
	 * 
	 * @return
	 */
	public static Path getConfigDir() {
		return Paths.get(CONFIG_DIR);
	}

	/**
	 * 
	 * @return
	 */
	public Path getRealPath() {
		return path;
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T defaultValue) {
		if (config == null) {
			return defaultValue;
		}
		Object obj = ((Map<String, Object>) config).get(key);
		if (obj == null) {
			return defaultValue;
		}
		return (T) obj;
	}

}
