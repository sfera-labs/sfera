/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.logging;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import cc.sferalabs.sfera.console.Console;

/**
 * 
 */
public abstract class LoggerUtils {

	/**
	 * 
	 */
	public static void init() {
		Path log4j2Config = cc.sferalabs.sfera.core.Configuration.getConfigDir()
				.resolve("log4j2.xml");
		if (Files.exists(log4j2Config)) {
			System.setProperty("log4j.configurationFile", log4j2Config.toString());
		}
		Console.addHandler(LogConsoleCommandHandler.INSTANCE);
	}

	/**
	 * Reloads the logger configuration.
	 */
	public static void reloadConfig() {
		LoggerContext.getContext(false).reconfigure();
	}

	/**
	 * @return the logger configuration
	 */
	public static org.apache.logging.log4j.core.config.Configuration getConfiguration() {
		return LoggerContext.getContext(false).getConfiguration();
	}

	/**
	 * Adds the specified appender to all configured non-additive loggers.
	 * 
	 * @param appender
	 *            the appender
	 * @param level
	 *            the logging level to assign to the appender. Default is INFO
	 */
	public static void addApender(Appender appender, String level) {
		Configuration config = LoggerUtils.getConfiguration();
		appender.start();
		config.addAppender(appender);
		Level l = Level.toLevel(level, Level.INFO);
		for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
			if (!loggerConfig.isAdditive()) {
				loggerConfig.addAppender(appender, l, null);
			}
		}
		config.getRootLogger().addAppender(appender, l, null);
	}

	/**
	 * Removes the specified appender from all configured loggers.
	 * 
	 * @param appender
	 *            the appender
	 */
	public static void removeApender(Appender appender) {
		Configuration config = LoggerUtils.getConfiguration();
		String name = appender.getName();
		for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
			loggerConfig.removeAppender(name);
		}
		config.getRootLogger().removeAppender(name);
		appender.stop();
	}

}
