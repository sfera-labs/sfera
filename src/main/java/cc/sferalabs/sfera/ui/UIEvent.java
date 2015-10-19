/**
 * 
 */
package cc.sferalabs.sfera.ui;

import cc.sferalabs.sfera.events.BaseEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class UIEvent extends BaseEvent {

	/**
	 * 
	 * @param uiNode
	 * @param id
	 */
	public UIEvent(UI uiNode, String id) {
		super(uiNode, id);
	}

}
