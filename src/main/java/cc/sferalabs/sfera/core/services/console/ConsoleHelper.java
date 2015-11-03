/**
 * 
 */
package cc.sferalabs.sfera.core.services.console;

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
	 */
	ConsoleHelper(Map<String, ConsoleCommandHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public void accept(String cmd) {
		for (Entry<String, ConsoleCommandHandler> entry : handlers.entrySet()) {
			String[] lines = entry.getValue().getHelp();
			if (lines != null) {
				for (String line : lines) {
					logger.info("{} {}", entry.getKey(), line);
				}
			}
		}
	}

	@Override
	public String[] getHelp() {
		return null;
	}

}
