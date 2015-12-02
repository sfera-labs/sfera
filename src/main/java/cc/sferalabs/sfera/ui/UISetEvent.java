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
	 * @param componentId
	 * @param attribute
	 * @param value
	 * @param connectionId
	 */
	UISetEvent(String componentId, String attribute, Object value, String connectionId) {
		// TODO
		// super("set." + (connectionId == null ? "global" : connectionId)
		// + Objects.requireNonNull(componentId, "componentId must not be null")
		// + "."
		// + Objects.requireNonNull(attribute, "attribute must not be null"),
		// value,
		// connectionId);

		super("set." + Objects.requireNonNull(componentId, "componentId must not be null") + "."
				+ Objects.requireNonNull(attribute, "attribute must not be null"), value,
				connectionId);
	}

}
