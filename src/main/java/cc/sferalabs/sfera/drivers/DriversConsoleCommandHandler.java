/**
 * 
 */
package cc.sferalabs.sfera.drivers;

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
public class DriversConsoleCommandHandler implements ConsoleCommandHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(DriversConsoleCommandHandler.class);

	static final DriversConsoleCommandHandler INSTANCE = new DriversConsoleCommandHandler();

	/**
	 * 
	 */
	private DriversConsoleCommandHandler() {
	}

	@Override
	public void accept(String cmd) {
		String[] args = cmd.split("\\s+");
		if (args.length != 2) {
			logger.warn("Format error");
			return;
		}

		Driver d = Drivers.getDriver(args[1]);
		if (d == null) {
			logger.warn("Driver '{}' not found", args[1]);
			return;
		}

		switch (args[0]) {
		case "quit":
			d.quit();
			break;

		case "start":
			d.start();
			break;

		case "restart":
			try {
				d.restart();
			} catch (InterruptedException e) {
				logger.warn("Command interrupted");
			}
			break;

		default:
			logger.warn("Unkown command");
			break;
		}
	}

	@Override
	public String[] getHelp() {
		return new String[] { "[quit | start | restart] <id>" };
	}

}
