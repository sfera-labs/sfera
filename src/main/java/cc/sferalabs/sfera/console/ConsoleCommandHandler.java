/**
 * 
 */
package cc.sferalabs.sfera.console;

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
	 * @return the text to output to the console, or {@code null} if no output
	 *         is needed
	 */
	public String accept(String cmd);

	/**
	 * Returns a String array containing a help text for each accepted command.
	 * 
	 * @return the help text lines
	 */
	public String[] getHelp();
}
