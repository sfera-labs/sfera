/**
 * 
 */
package cc.sferalabs.sfera.console;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ConsoleHelper implements ConsoleCommandHandler {

	private static final Logger logger = LoggerFactory.getLogger(ConsoleHelper.class);

	private final Map<String, ConsoleCommandHandler> handlers;

	/**
	 * @param handlers
	 *            the map of existing {@code ConsoleCommandHandlers}
	 */
	ConsoleHelper(Map<String, ConsoleCommandHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public void accept(String cmd) {
		if (cmd.isEmpty()) {
			for (Entry<String, ConsoleCommandHandler> entry : handlers.entrySet()) {
				printHelp(entry.getKey(), entry.getValue());
			}
		} else {
			ConsoleCommandHandler h = handlers.get(cmd);
			if (h == null) {
				logger.warn("Handler not found");
				return;
			}
			printHelp(cmd, h);
		}
	}

	/**
	 * 
	 * @param key
	 * @param handler
	 */
	private void printHelp(String key, ConsoleCommandHandler handler) {
		String[] lines = handler.getHelp();
		if (lines != null) {
			for (String line : lines) {
				logger.info("{} {}", key, line);
			}
		}
	}

	@Override
	public String[] getHelp() {
		return null;
	}

}
