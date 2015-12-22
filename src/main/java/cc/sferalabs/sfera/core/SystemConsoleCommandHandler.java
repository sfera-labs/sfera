/**
 * 
 */
package cc.sferalabs.sfera.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.console.ConsoleCommandHandler;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class SystemConsoleCommandHandler implements ConsoleCommandHandler {

	private static final Logger logger = LoggerFactory.getLogger(SystemConsoleCommandHandler.class);

	static final SystemConsoleCommandHandler INSTANCE = new SystemConsoleCommandHandler();

	/**
	 * 
	 */
	private SystemConsoleCommandHandler() {
	}

	@Override
	public void accept(String cmd) {
		switch (cmd) {
		case "quit":
			SystemNode.getInstance().quit();
			break;

		case "kill":
			System.exit(1);
			break;

		default:
			logger.warn("Unkown command");
			break;
		}
	}

	@Override
	public String[] getHelp() {
		return new String[] { "[quit | kill]" };
	}

}
