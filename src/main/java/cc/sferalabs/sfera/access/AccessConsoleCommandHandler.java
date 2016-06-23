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

package cc.sferalabs.sfera.access;

import java.io.IOException;
import java.util.Arrays;

import cc.sferalabs.sfera.console.ConsoleCommandHandler;
import cc.sferalabs.sfera.console.ConsoleSession;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class AccessConsoleCommandHandler implements ConsoleCommandHandler {

	static final AccessConsoleCommandHandler INSTANCE = new AccessConsoleCommandHandler();

	/**
	 * 
	 */
	private AccessConsoleCommandHandler() {
	}

	@Override
	public String getKey() {
		return "access";
	}

	@Override
	public String accept(String cmd, ConsoleSession session) {
		String[] args = cmd.split("\\s+");
		if (args.length < 2) {
			return "Syntax error";
		}

		String username = args[1];

		switch (args[0]) {
		case "add":
			if (args.length < 3) {
				return "Parameters missing";
			}

			try {
				String password = args[2];
				String[] roles = Arrays.copyOfRange(args, 3, args.length);
				Access.addUser(username, password, roles);
				return "Added '" + username + "' password '" + password + "' roles "
						+ Arrays.asList(roles);
			} catch (IOException e) {
				return "Error: " + e;
			} catch (UsernameAlreadyUsedException e) {
				return "Username already used";
			}

		case "get":
			User u = Access.getUser(username);
			if (u == null) {
				return "User not found";
			}
			return "Added '" + u.getUsername() + "' roles " + Arrays.asList(u.getRoles());

		case "remove":
			try {
				Access.removeUser(username);
				return "Removed";
			} catch (IOException e) {
				return "Error: " + e;
			} catch (UserNotFoundException e) {
				return "User not found";
			}

		case "update":
			if (args.length < 3) {
				return "Parameters missing";
			}
			String password = null;
			String[] roles = null;
			switch (args[2]) {
			case "password":
				if (args.length < 4) {
					return "Parameters missing";
				}
				password = args[3];
				break;

			case "roles":
				roles = Arrays.copyOfRange(args, 3, args.length);
				break;

			default:
				return "Unkown update parameter";
			}

			try {
				Access.updateUser(username, password, roles);
				return "Updated '" + username + "'";
			} catch (IOException e) {
				return "Error: " + e;
			} catch (UserNotFoundException e) {
				return "User not found";
			}

		default:
			return "Unkown command";
		}
	}

}
