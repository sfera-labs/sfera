/**
 * 
 */
package cc.sferalabs.sfera.http;

import cc.sferalabs.sfera.events.Event;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface HttpConnectionEvent extends Event {

	/**
	 * @return
	 */
	String getConnectionId();

}
