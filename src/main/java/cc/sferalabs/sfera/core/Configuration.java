package cc.sferalabs.sfera.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Configuration {

	/** File extension for configuration files */
	public static final String FILE_EXTENSION = ".yml";
	private static final String CONFIG_DIR = "config";

	private final Path path;
	private final Object config;

	/**
	 * Construct a configuration from the specified file. The specified path is
	 * considered relative to the configuration directory
	 * 
	 * @param configFile
	 *            the path of the configuration file relative to the
	 *            configuration directory
	 * @throws IOException
	 *             if an I/O error occurs opening the configuration file
	 */
	public Configuration(String configFile) throws IOException {
		this(getPath(configFile));
	}

	/**
	 * Construct a configuration from the specified file
	 * 
	 * @param configFile
	 *            the path to the configuration file
	 * @throws IOException
	 *             if an I/O error occurs opening the configuration file
	 */
	public Configuration(Path configFile) throws IOException {
		path = configFile;
		config = load();
	}

	/**
	 * 
	 * @return the corresponding Java object loaded from the configuration file
	 * @throws IOException
	 *             if an I/O error occurs opening the configuration file
	 */
	private Object load() throws IOException {
		Yaml yaml = new Yaml();
		try (BufferedReader r = Files.newBufferedReader(path)) {
			return yaml.load(r);
		}
	}

	/**
	 * Parses the configuration file and produces an object of the specified
	 * class.
	 * 
	 * @param <T>
	 *            Class of the object specified by the {@code clazz} parameter
	 * @param clazz
	 *            class of the object to be created
	 * @return the object created from the configuration file
	 * @throws IOException
	 *             if an I/O error occurs opening the configuration file
	 */
	public <T> T loadAs(Class<T> clazz) throws IOException {
		Yaml yaml = new Yaml();
		try (BufferedReader r = Files.newBufferedReader(path)) {
			return yaml.loadAs(r, clazz);
		}
	}

	/**
	 * 
	 * @param configFile
	 *            the configuration file
	 * @return the actual path to the configuration file resolved against the
	 *         configuration directory
	 */
	public static Path getPath(String configFile) {
		return Paths.get(CONFIG_DIR).resolve(configFile);
	}

	/**
	 * 
	 * @return the path to the configuration directory
	 */
	public static Path getConfigDir() {
		return Paths.get(CONFIG_DIR);
	}

	/**
	 * 
	 * @return the actual path to the configuration file corresponding to this
	 *         object
	 */
	public Path getRealPath() {
		return path;
	}

	/**
	 * Returns the value to which the specified key is mapped, or
	 * {@code defaultValue} if this configuration contains no mapping for the
	 * key.
	 * 
	 * @param <T>
	 *            the type of the object to be returned
	 * @param key
	 *            the key whose associated value is to be returned
	 * @param defaultValue
	 *            the default value to be returned if this configuration
	 *            contains no mapping for the key
	 * @return the value to which the specified key is mapped, or
	 *         {@code defaultValue} if this map contains no mapping for the key.
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
