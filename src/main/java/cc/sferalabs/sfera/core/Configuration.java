package cc.sferalabs.sfera.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuration {
	
	static final Path CONFIG_DIR = Paths.get("config");
	static final Path SYSTEM_CONFIG_FILE = CONFIG_DIR.resolve("sfera.ini");
	
	public static final Configuration SYSTEM = new Configuration("system");
	
	private static Properties props;
	
	private final String prefix;
	
	/**
	 * 
	 */
	public Configuration(String prefix) {
		this.prefix = prefix + ".";
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void load() throws IOException {
		props = new Properties();
		
		try (BufferedReader r = Files.newBufferedReader(SYSTEM_CONFIG_FILE)) {
			props.load(r);
		} catch (NoSuchFileException e) {
			// no config file, that's fine...
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static Properties getProperties() {
		return props;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		key = prefix + key;
		String val = props.getProperty(key, defaultValue);
		if (val == null) {
			return null;
		}
		
		return val.trim();
	}
	
	/**
	 * 
	 * @param key
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
