/**
 * 
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
			if (args.length > 3) {
				String password = args[2];
				String[] roles = Arrays.copyOfRange(args, 3, args.length);
				try {
					Access.addUser(username, password, roles);
					return "Added";
				} catch (IOException e) {
					return "Error: " + e;
				} catch (UsernameAlreadyUsedException e) {
					return "Username already used";
				}
			} else {
				return "Parameters missing";
			}

		case "remove":
			try {
				Access.removeUser(username);
				return "Removed";
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
