package cc.sferalabs.sfera.util.logging;

import org.apache.logging.log4j.LogManager;

/**
 * 
 */
public abstract class LoggerUtils {

	/**
	 * Reloads the logger configuration.
	 */
	public static void reloadConfig() {
		((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
	}
}
