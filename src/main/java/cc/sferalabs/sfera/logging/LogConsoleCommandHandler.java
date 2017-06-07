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

/**
 * 
 */
package cc.sferalabs.sfera.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import cc.sferalabs.sfera.console.ConsoleCommandHandler;
import cc.sferalabs.sfera.console.ConsoleSession;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class LogConsoleCommandHandler implements ConsoleCommandHandler {

	static final LogConsoleCommandHandler INSTANCE = new LogConsoleCommandHandler();

	/**
	 * 
	 */
	private LogConsoleCommandHandler() {
	}

	@Override
	public String getKey() {
		return "log";
	}

	@Override
	public String accept(String cmd, ConsoleSession session) {
		if (cmd.toLowerCase().startsWith("start")) {
			String level = null;
			String pattern = null;
			if (cmd.length() > 5) {
				Map<String, String> args;
				try {
					args = ConsoleCommandHandler.parseArguments(cmd.substring(5));
				} catch (Exception e) {
					return "Error: " + e.getMessage();
				}
				level = args.get("l");
				pattern = args.get("p");
			}
			if (pattern == null) {
				pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} | %-5level | %logger{1.} - %msg%n%throwable";
			}

			String sId = getId(session);
			Appender appender = null;
			try {
				session.output("=== Logging started ===\n");
				appender = addAppender(sId, level, pattern, session);
				String c = session.acceptCommand();
				if (c == null) {
					session.quit();
				}
			} catch (Throwable t) {
			}
			if (appender != null) {
				LoggerUtils.removeRootApender(appender);
			}
			return "=== Logging stopped ===";
		}

		return "Unknown command";
	}

	/**
	 * @param name
	 * @param level
	 * @param pattern
	 * @param session
	 * @return
	 */
	private static Appender addAppender(String name, String level, String pattern,
			ConsoleSession session) {
		SessionOutputStream out = new SessionOutputStream(session);

		org.apache.logging.log4j.core.config.Configuration config = LoggerUtils.getConfiguration();
		PatternLayout layout = PatternLayout.createLayout(pattern, null, config, null,
				StandardCharsets.UTF_8, false, false, null, null);
		Appender appender = OutputStreamAppender.createAppender(layout, null, out, name, false,
				true);

		LoggerUtils.addRootApender(appender, level);

		return appender;
	}

	/**
	 * @param session
	 * @return
	 */
	private static String getId(ConsoleSession session) {
		return "console_logger+" + System.identityHashCode(session);
	}

	/**
	 *
	 */
	private static class SessionOutputStream extends OutputStream {

		private final ConsoleSession session;
		private StringBuilder buffer = new StringBuilder();

		/**
		 * 
		 */
		private SessionOutputStream(ConsoleSession session) {
			this.session = session;
		}

		@Override
		public void write(int b) throws IOException {
			buffer.append((char) b);
		}

		@Override
		public void flush() throws IOException {
			String s = buffer.toString();
			buffer = new StringBuilder();
			session.output(s);
		}
	};
}
