/**
 * 
 */
package cc.sferalabs.sfera.access;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.console.ConsoleCommandHandler;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class AccessConsoleCommandHandler implements ConsoleCommandHandler {

	private static final Logger logger = LoggerFactory.getLogger(AccessConsoleCommandHandler.class);

	static final AccessConsoleCommandHandler INSTANCE = new AccessConsoleCommandHandler();

	/**
	 * 
	 */
	private AccessConsoleCommandHandler() {
	}

	@Override
	public void accept(String cmd) {
		String[] args = cmd.split("\\s+");
		if (args.length < 2) {
			logger.warn("Format error");
			return;
		}

		String username = args[1];

		switch (args[0]) {
		case "add":
			if (args.length > 3) {
				String password = args[2];
				String[] roles = Arrays.copyOfRange(args, 3, args.length);
				try {
					Access.addUser(username, password, roles);
				} catch (IOException e) {
					logger.warn("Error", e);
				} catch (UsernameAlreadyUsedException e) {
					logger.warn("Username already used", e);
				}
			} else {
				logger.warn("Missing parameters");
			}
			break;

		case "remove":
			try {
				Access.removeUser(username);
			} catch (IOException e) {
				logger.warn("Error", e);
			}
			break;

		default:
			logger.warn("Unkown command");
			break;
		}
	}

	@Override
	public String[] getHelp() {
		return new String[] { "add <username> <password> <role1> [<role2> ... <roleN>]",
				"remove <username>" };
	}

}
