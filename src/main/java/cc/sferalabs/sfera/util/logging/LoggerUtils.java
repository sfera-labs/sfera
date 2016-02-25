package cc.sferalabs.sfera.util.logging;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

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

	/**
	 * @param outputStream
	 * @param name
	 * @param pattern
	 */
	public static void addOutputStreamAppender(final OutputStream outputStream, final String name,
			final String pattern) {
		final LoggerContext context = LoggerContext.getContext(false);
		final Configuration config = context.getConfiguration();
		final PatternLayout layout = PatternLayout.createLayout(pattern, null, config, null,
				StandardCharsets.UTF_8, false, false, null, null);
		final Appender appender = OutputStreamAppender.createAppender(layout, null, outputStream,
				name, false, true);
		appender.start();
		config.addAppender(appender);
		updateLoggers(appender, config);
	}

	/**
	 * @param appender
	 * @param config
	 */
	private static void updateLoggers(final Appender appender, final Configuration config) {
		for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
			loggerConfig.addAppender(appender, null, null);
		}
		config.getRootLogger().addAppender(appender, null, null);
	}

}
