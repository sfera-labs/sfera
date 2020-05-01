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
package cc.sferalabs.sfera.drivers;

import cc.sferalabs.sfera.console.ConsoleCommandHandler;
import cc.sferalabs.sfera.console.ConsoleSession;

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
	public String accept(String cmd, ConsoleSession session) {
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
			d.restart();
			return "Restarted";

		default:
			return "Unkown command";
		}
	}

}
