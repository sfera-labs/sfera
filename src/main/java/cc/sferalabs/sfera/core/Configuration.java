package cc.sferalabs.sfera.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuration {
	
	// TODO maybe use YAML ??

	private static final String CONFIG_DIR = "config";

	private final Path path;
	private final Properties props;

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
	 * @param configFilile
	 * @throws IOException
	 */
	public Configuration(Path configFilile) throws IOException {
		path = configFilile;
		props = new Properties();
		try (BufferedReader r = Files.newBufferedReader(path)) {
			props.load(r);
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
	public String getProperty(String key, String defaultValue) {
		String val = props.getProperty(key, defaultValue);
		if (val == null) {
			return null;
		}

		return val.trim();
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Integer getIntProperty(String key, Integer defaultValue) {
		String val = getProperty(key, null);
		if (val == null) {
			return defaultValue;
		}

		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Boolean getBoolProperty(String key, Boolean defaultValue) {
		String val = getProperty(key, null);
		if (val == null) {
			return defaultValue;
		}

		if (val.equalsIgnoreCase("true")) {
			return true;
		}
		if (val.equalsIgnoreCase("false")) {
			return false;
		}
		return defaultValue;
	}

}
