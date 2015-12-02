/**
 * 
 */
package cc.sferalabs.sfera.ui;

import cc.sferalabs.sfera.events.ObjectEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class UIEvent extends ObjectEvent {

	private final String connectionId;

	/**
	 * 
	 * @param id
	 *            event ID
	 * @param value
	 *            event value
	 * @param connectionId
	 *            connection ID
	 */
	public UIEvent(String id, Object value, String connectionId) {
		super(UI.getInstance(), id, value);
		this.connectionId = connectionId;
	}

	/**
	 * @return the connection ID
	 */
	public String getConnectionId() {
		return connectionId;
	}

}
