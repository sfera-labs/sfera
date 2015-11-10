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

	/**
	 * 
	 * @param id
	 * @param value
	 */
	public UIEvent(String id, Object value) {
		super(UI.getInstance(), id, value);
	}

}
