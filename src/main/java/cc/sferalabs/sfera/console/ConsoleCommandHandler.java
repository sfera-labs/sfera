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
	 * @param cmd
	 *            the command text
	 */
	public void accept(String cmd);

	/**
	 * Returns an Strings array containing a help text for each accepted
	 * command.
	 * 
	 * @return the help text lines
	 */
	public String[] getHelp();
}
