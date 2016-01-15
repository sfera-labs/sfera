/**
 * 
 */
package cc.sferalabs.sfera.console;

import java.util.Map;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ConsoleHelper implements ConsoleCommandHandler {

	private final Map<String, ConsoleCommandHandler> handlers;

	/**
	 * @param handlers
	 *            the map of existing {@code ConsoleCommandHandlers}
	 */
	ConsoleHelper(Map<String, ConsoleCommandHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public String accept(String cmd) {
		ConsoleCommandHandler h = handlers.get(cmd);
		StringBuilder sb = new StringBuilder();
		if (h == null) {
			sb.append("Specify a valid handler:");
			for (String key : handlers.keySet()) {
				sb.append("\n").append(key);
			}
		} else {
			String[] lines = h.getHelp();
			if (lines != null) {
				for (String line : lines) {
					sb.append(cmd).append(" ").append(line).append("\n");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String[] getHelp() {
		return null;
	}

}
