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
public class UISessionSetEvent extends UISetEvent {

	private final String session;

	/**
	 * 
	 * @param id
	 * @param attribute
	 * @param value
	 * @param session
	 */
	UISessionSetEvent(String id, String attribute, String value, String session) {
		super(id, attribute, value);
		this.session = Objects.requireNonNull(session, "session must not be null");
	}

	/**
	 * @return the session
	 */
	public String getSession() {
		return session;
	}

}
