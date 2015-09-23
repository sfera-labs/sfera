/**
 * 
 */
package cc.sferalabs.sfera.core.events;

import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.events.StringEvent;

/**
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
abstract class SystemStringEvent extends StringEvent implements SystemEvent {

	/**
	 * 
	 * @param id
	 * @param value
	 */
	SystemStringEvent(String id, String value) {
		super(SystemNode.getInstance(), id, value);
	}

}
