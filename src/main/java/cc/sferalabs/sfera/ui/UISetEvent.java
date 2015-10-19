/**
 * 
 */
package cc.sferalabs.sfera.ui;

import cc.sferalabs.sfera.events.StringEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class UISetEvent extends StringEvent {

	private static final UI UI_NODE = new UI();

	/**
	 * 
	 * @param id
	 * @param attribute
	 * @param value
	 */
	UISetEvent(String id, String attribute, String value) {
		super(UI_NODE, "set." + id + "." + attribute, value);
	}

}
