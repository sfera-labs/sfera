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
	 * Returns the key associated to this handler, i.e. the name of the command
	 * processed by this handler.
	 * 
	 * @return the key associated to this handler
	 */
	public String getKey();

}
