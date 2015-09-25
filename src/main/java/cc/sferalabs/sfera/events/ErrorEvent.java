package cc.sferalabs.sfera.events;

/**
 * Interface for events representing an error.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface ErrorEvent extends Event {

	/**
	 * Returns a description for the error.
	 * 
	 * @return a description for the error
	 */
	public String getErrorDescription();

}
