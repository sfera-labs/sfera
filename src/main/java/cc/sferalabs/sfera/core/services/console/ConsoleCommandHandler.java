/**
 * 
 */
package cc.sferalabs.sfera.core.services.console;

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
	 */
	public void accept(String cmd);
	
	/**
	 * @return
	 */
	public String[] getHelp();
}
