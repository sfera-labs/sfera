/**
 * 
 */
package cc.sferalabs.sfera.drivers;

import cc.sferalabs.sfera.console.ConsoleCommandHandler;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class DriversConsoleCommandHandler implements ConsoleCommandHandler {

	static final DriversConsoleCommandHandler INSTANCE = new DriversConsoleCommandHandler();

	/**
	 * 
	 */
	private DriversConsoleCommandHandler() {
	}

	@Override
	public String getKey() {
		return "drivers";
	}

	@Override
	public String accept(String cmd) {
		String[] args = cmd.split("\\s+");
		if (args.length != 2) {
			return "Syntax error";
		}

		Driver d = Drivers.getDriver(args[1]);
		if (d == null) {
			return "Driver '" + args[1] + "' not found";
		}

		switch (args[0]) {
		case "quit":
			d.quit();
			return "Quitting";

		case "start":
			d.start();
			return "Started";

		case "restart":
			try {
				d.restart();
			} catch (InterruptedException e) {
				return "Interrupted";
			}
			return "Restarted";

		default:
			return "Unkown command";
		}
	}

}
