/**
 * 
 */
package cc.sferalabs.sfera.access;

import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.ObjectEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class AccessChangeEvent extends ObjectEvent {

	/**
	 * @param source
	 * @param id
	 * @param value
	 */
	public AccessChangeEvent(Node source) {
		super(source, "change", null);
	}

}
