/**
 * 
 */
package cc.sferalabs.sfera.ui;

import java.util.Objects;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class UISetEvent extends UIEvent {

	/**
	 * 
	 * @param id
	 * @param attribute
	 * @param value
	 */
	UISetEvent(String id, String attribute, Object value) {
		super("set." + Objects.requireNonNull(id, "id must not be null") + "."
				+ Objects.requireNonNull(attribute, "attribute must not be null"), value);
	}

}
