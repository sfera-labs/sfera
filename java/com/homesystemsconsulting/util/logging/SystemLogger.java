package com.homesystemsconsulting.util.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.homesystemsconsulting.core.Configuration;
import com.homesystemsconsulting.util.logging.levels.DebugLevel;
import com.homesystemsconsulting.util.logging.levels.ErrorLevel;
import com.homesystemsconsulting.util.logging.levels.EventLevel;
import com.homesystemsconsulting.util.logging.levels.InfoLevel;
import com.homesystemsconsulting.util.logging.levels.VerboseLevel;
import com.homesystemsconsulting.util.logging.levels.WarningLevel;

public class SystemLogger {
	
	final static Level ERROR = new ErrorLevel();
	final static Level WARNING = new WarningLevel();
	final static Level INFO = new InfoLevel();
	final static Level EVENT = new EventLevel();
	final static Level DEBUG = new DebugLevel();
	final static Level VERBOSE = new VerboseLevel();

	private static final String LOG_DIR = "logs/";
	private static final String TEXT_LOG_FILE = LOG_DIR + "log.txt";
	private static final String JSON_LOG_FILE = LOG_DIR + "log.json";
	
	private static final Logger BASE_LOGGER = Logger.getLogger("sfera");
	
	public static final SystemLogger SYSTEM = new SystemLogger("system");
	
	private final String id;

	/**
	 * 
	 * @param id
	 */
	private SystemLogger(String id) {
		this.id = BASE_LOGGER.getName() + "." + id;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static SystemLogger getLogger(String name) {
		return new SystemLogger(name);
	}

	/**
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	static public void setup() throws SecurityException, IOException {
		LogManager.getLogManager().reset();
		BASE_LOGGER.setUseParentHandlers(false);
		
		Files.createDirectories(Paths.get(LOG_DIR));
		
		FileHandler fileTxt;
		fileTxt = new FileHandler(TEXT_LOG_FILE, true);
		fileTxt.setFormatter(new TextFormatter());
		BASE_LOGGER.addHandler(fileTxt);
		
		fileTxt = new FileHandler(JSON_LOG_FILE, true);
		fileTxt.setFormatter(new JSONFormatter());
		BASE_LOGGER.addHandler(fileTxt);
		
		Level level = getLevel(Configuration.getProperty("log_level", "INFO"));
		
		BASE_LOGGER.setLevel(level);	
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	private static Level getLevel(String name) {
		if (name.equalsIgnoreCase("ERROR")) {
			return ERROR;
		} else if (name.equalsIgnoreCase("WARNING")) {
			return WARNING;
		} else if (name.equalsIgnoreCase("INFO")) {
			return INFO;
		} else if (name.equalsIgnoreCase("EVENT")) {
			return EVENT;
		} else if (name.equalsIgnoreCase("DEBUG")) {
			return DEBUG;
		} else if (name.equalsIgnoreCase("VERBOSE")) {
			return VERBOSE;
		} else {
			return null;
		}
	}

	/**
	 * error log
	 * @param message
	 */
	public void error(String message) {
		Logger.getLogger(id).log(ERROR, message);
	}
	
	/**
	 * error log
	 * @param tag
	 * @param message
	 */
	public void error(String tag, String message) {
		Logger.getLogger(id + "." + tag).log(ERROR, message);
	}
	
	/**
	 * warning log
	 * @param message
	 */
	public void warning(String message) {
		Logger.getLogger(id).log(WARNING, message);
	}
	
	/**
	 * warning log
	 * @param tag
	 * @param message
	 */
	public void warning(String tag, String message) {
		Logger.getLogger(id + "." + tag).log(WARNING, message);
	}
	
	/**
	 * information log
	 * @param message
	 */
	public void info(String message) {
		Logger.getLogger(id).log(INFO, message);
	}
	
	/**
	 * information log
	 * @param tag
	 * @param message
	 */
	public void info(String tag, String message) {
		Logger.getLogger(id + "." + tag).log(INFO, message);
	}
	
	/**
	 * event log
	 * @param message
	 */
	public void event(String message) {
		Logger.getLogger(id).log(EVENT, message);
	}
	
	/**
	 * event log
	 * @param tag
	 * @param message
	 */
	public void event(String tag, String message) {
		Logger.getLogger(id + "." + tag).log(EVENT, message);
	}
	
	/**
	 * debug log
	 * @param message
	 */
	public void debug(String message) {
		Logger.getLogger(id).log(DEBUG, message);
	}
	
	/**
	 * debug log
	 * @param tag
	 * @param message
	 */
	public void debug(String tag, String message) {
		Logger.getLogger(id + "." + tag).log(DEBUG, message);
	}
	
	/**
	 * verbose log
	 * @param message
	 */
	public void verbose(String message) {
		Logger.getLogger(id).log(VERBOSE, message);
	}
	
	/**
	 * verbose log
	 * @param tag
	 * @param message
	 */
	public void verbose(String tag, String message) {
		Logger.getLogger(id + "." + tag).log(VERBOSE, message);
	}
}
	 