package cc.sferalabs.sfera.util.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * 
 */
public abstract class LoggerUtils {
	
	public static final Marker SFERA_MARKER = MarkerManager.getMarker("SFERA");

	/**
	 * Reload the logger configuration.
	 */
	public static void reloadConfig() {
		((org.apache.logging.log4j.core.LoggerContext) LogManager
				.getContext(false)).reconfigure();
	}
}
