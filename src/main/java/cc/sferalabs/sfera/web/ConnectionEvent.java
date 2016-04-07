/**
 * 
 */
package cc.sferalabs.sfera.web;

import cc.sferalabs.sfera.events.Event;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface ConnectionEvent extends Event {

	/**
	 * Returns the connection ID.
	 * 
	 * @return the connection ID
	 */
	String getConnectionId();

}
