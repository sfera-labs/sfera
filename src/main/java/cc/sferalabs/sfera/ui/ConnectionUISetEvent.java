/**
 * 
 */
package cc.sferalabs.sfera.ui;

import java.util.Objects;

import cc.sferalabs.sfera.http.HttpConnectionEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ConnectionUISetEvent extends UISetEvent implements HttpConnectionEvent {

	/**
	 * Construct a ConnectionUISetEvent
	 * 
	 * @param componentId
	 *            the component ID
	 * @param attribute
	 *            the attribute to set
	 * @param value
	 *            the value to assign
	 * @param connectionId
	 *            the connection ID
	 */
	ConnectionUISetEvent(String componentId, String attribute, Object value, String connectionId) {
		super(componentId, attribute, value,
				Objects.requireNonNull(connectionId, "connectionId must not be null"));
	}

}