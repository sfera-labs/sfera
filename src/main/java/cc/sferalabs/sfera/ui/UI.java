/**
 * 
 */
package cc.sferalabs.sfera.ui;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class UI implements Node {

	private static final UI INSTANCE = new UI();

	/**
	 * @return
	 */
	static UI getInstance() {
		return INSTANCE;
	}

	@Override
	public final String getId() {
		return "ui";
	}

	/**
	 * 
	 * @param id
	 *            ID of the addressed components
	 * @param attribute
	 *            attribute to set
	 * @param value
	 *            value to assign
	 */
	public static void set(String id, String attribute, String value) {
		Bus.post(new UISetEvent(id, attribute, value));
	}

}
