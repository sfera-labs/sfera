/**
 * 
 */
package cc.sferalabs.sfera.ui;

import java.util.Objects;

import cc.sferalabs.sfera.events.StringEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class UISetEvent extends StringEvent {

	/**
	 * 
	 * @param id
	 * @param attribute
	 * @param value
	 */
	UISetEvent(String id, String attribute, String value) {
		super(UI.getInstance(), "set." + Objects.requireNonNull(id, "id must not be null") + "."
				+ Objects.requireNonNull(attribute, "attribute must not be null"), value);
	}

}
