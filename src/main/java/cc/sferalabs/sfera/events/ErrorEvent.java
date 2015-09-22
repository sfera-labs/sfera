package cc.sferalabs.sfera.events;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface ErrorEvent extends Event {

	/**
	 * 
	 * @return
	 */
	public String getErrorDescription();

}
