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
import java.util.Iterator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import cc.sferalabs.sfera.console.Console;

/**
 *
 * @author Giampiero Baggiani
 *
 */
public abstract class LoggerUtils {

	private static Level originalRootLevel;
	private static int addedAppenders = 0;

	/**
	 * 
	 */
	public static void init() {
		Path log4j2Config = cc.sferalabs.sfera.core.Configuration.getConfigDir().resolve("log4j2.xml");
		if (Files.exists(log4j2Config)) {
			System.setProperty("log4j.configurationFile", log4j2Config.toString());
		}
		getContext().reconfigure();
		Console.addHandler(LogConsoleCommandHandler.INSTANCE);
	}

	/**
	 * @return the logger context
	 */
	static LoggerContext getContext() {
		return LoggerContext.getContext(false);
	}

	/**
	 * @return the logger configuration
	 */
	static org.apache.logging.log4j.core.config.Configuration getConfiguration() {
		return getContext().getConfiguration();
	}

	/**
	 * Adds the specified appender to the root logger.
	 * 
	 * @param appender
	 *            the appender
	 * @param level
	 *            the logging level to assign to the appender. Default is INFO
	 */
	public static synchronized void addRootApender(Appender appender, String level) {
		Configuration config = getConfiguration();
		appender.start();
		config.addAppender(appender);
		Level l = Level.toLevel(level, Level.INFO);

		LoggerConfig root = config.getRootLogger();
		if (!root.getLevel().isLessSpecificThan(l)) {
			if (originalRootLevel == null) {
				originalRootLevel = root.getLevel();
				Iterator<AppenderRef> it = root.getAppenderRefs().iterator();
				while (it.hasNext()) {
					AppenderRef ar = it.next();
					if (ar.getLevel() == null) {
						Appender a = root.getAppenders().get(ar.getRef());
						root.removeAppender(a.getName());
						root.addAppender(a, originalRootLevel, ar.getFilter());
					}
				}
			}
			root.setLevel(l);
		}
		root.addAppender(appender, l, null);
		addedAppenders++;
		getContext().updateLoggers();
	}

	/**
	 * Removes the specified previously added appender from the root logger.
	 * 
	 * @param appender
	 *            the appender to remove
	 */
	public static synchronized void removeRootApender(Appender appender) {
		if (addedAppenders > 0) {
			Configuration config = getConfiguration();
			String name = appender.getName();
			LoggerConfig root = config.getRootLogger();
			root.removeAppender(name);
			appender.stop();
			if (--addedAppenders == 0 && originalRootLevel != null) {
				root.setLevel(originalRootLevel);
			}
			getContext().updateLoggers();
		}
	}

}
