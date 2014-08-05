package com.homesystemsconsulting.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	
	private static final String CONFIG_DIR = "config/";
	private static final String SYSTEM_CONFIG_FILE = CONFIG_DIR + "system.conf";
	
	private static Properties props;
	
	/**
	 * 
	 * @throws IOException
	 */
	public static void load() throws IOException {
		props = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(SYSTEM_CONFIG_FILE);
			props.load(input);
		} catch (FileNotFoundException e) {
			// no config file, that's fine...
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key, String defaultValue) {
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
	public static Integer getIntProperty(String key, Integer defaultValue) {
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
	public static Boolean getBoolProperty(String key, Boolean defaultValue) {
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
