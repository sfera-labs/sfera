/**
 * 
 */
package cc.sferalabs.sfera.console;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ConsoleHelpHandler implements ConsoleCommandHandler {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleHelpHandler.class);

	private final Map<String, ConsoleCommandHandler> handlers;

	/**
	 * @param handlers
	 *            the map of existing {@code ConsoleCommandHandlers}
	 */
	ConsoleHelpHandler(Map<String, ConsoleCommandHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public String getKey() {
		return "help";
	}

	@Override
	public String accept(String cmd, ConsoleSession session) {
		ConsoleCommandHandler h = handlers.get(cmd);
		if (h == null || h == this) {
			StringBuilder sb = new StringBuilder("Usage: help <handler>\nActive handlers:");
			for (ConsoleCommandHandler handler : handlers.values()) {
				if (handler != this) {
					sb.append("\n    ").append(handler.getKey());
				}
			}
			return sb.toString();
		}

		try (InputStream in = getClass().getResourceAsStream("help/" + h.getKey() + ".txt");
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in, StandardCharsets.UTF_8))) {
			return br.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (Exception e) {
			String err = "Could not load help file for handler '" + h.getKey() + "'";
			logger.error(err, e);
			return err + ": " + e;
		}
	}

}
