/**
 * 
 */
package cc.sferalabs.sfera.console;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface ConsoleCommandHandler {

	/**
	 * Processes the passed command.
	 * 
	 * @param cmd
	 *            the command text
	 * @param session
	 *            the {@link ConsoleSession} that issued the command
	 * @return the text to output to the console, or {@code null} if no output
	 *         is needed
	 */
	public String accept(String cmd, ConsoleSession session);

	/**
	 * Returns the key associated to this handler, i.e. the name of the command
	 * processed by this handler.
	 * 
	 * @return the key associated to this handler
	 */
	public String getKey();

	/**
	 * Parses the arguments part of a command of the form '-arg1 val1 -arg2
	 * "val with spaces"', returning a Map containing all the arguments mapped
	 * to the respective values.
	 * 
	 * @param arguments
	 *            the command arguments string
	 * @return the Map of the arguments and respective values
	 * @throws Exception
	 *             if the passed string has syntax errors
	 */
	public static Map<String, String> parseArguments(String arguments) throws Exception {
		Map<String, String> prms = new HashMap<>();
		if (arguments.length() == 0) {
			return prms;
		}

		final int normal = 0;
		final int inSingleQuotes = 1;
		final int inDoubleQuotes = 2;
		final int inArgName = 3;

		int state = normal;
		StringTokenizer tokens = new StringTokenizer(arguments, "-\"\' ", true);
		StringBuilder current = new StringBuilder();
		boolean closedQuotes = false;
		String currArg = null;

		while (tokens.hasMoreTokens()) {
			final String nextTok = tokens.nextToken();
			switch (state) {
			case inSingleQuotes:
				if ("\'".equals(nextTok)) {
					closedQuotes = true;
					state = normal;
				} else {
					current.append(nextTok);
				}
				break;

			case inDoubleQuotes:
				if ("\"".equals(nextTok)) {
					closedQuotes = true;
					state = normal;
				} else {
					current.append(nextTok);
				}
				break;

			case inArgName:
				if (" ".equals(nextTok)) {
					if (currArg != null) {
						prms.put(currArg, null);
					}
					currArg = current.toString();
					current = new StringBuilder();
					state = normal;
				} else {
					current.append(nextTok);
				}
				break;

			default:
				if ("\'".equals(nextTok)) {
					state = inSingleQuotes;
				} else if ("\"".equals(nextTok)) {
					state = inDoubleQuotes;
				} else if ("-".equals(nextTok)) {
					state = inArgName;
				} else if (" ".equals(nextTok)) {
					if (closedQuotes || current.length() != 0) {
						if (currArg != null) {
							prms.put(currArg, current.toString());
							currArg = null;
						} else {
							throw new Exception("Argument name missing");
						}
						current = new StringBuilder();
					}
				} else {
					current.append(nextTok);
				}
				closedQuotes = false;
				break;
			}
		}

		if (closedQuotes || current.length() != 0) {
			if (currArg != null) {
				prms.put(currArg, current.toString());
			} else {
				throw new Exception("Argument name missing");
			}
		}

		if (state == inSingleQuotes || state == inDoubleQuotes) {
			throw new Exception("Unbalanced quotes");
		}

		return prms;
	}

}
