/**
 * 
 */
package cc.sferalabs.sfera.ui;

import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;

/**
 * Class representing the UI node.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class UI extends Node implements AutoStartService {

	private static UI INSTANCE;

	/**
	 * 
	 */
	public UI() {
		super("ui");
	}

	@Override
	public void init() throws Exception {
		INSTANCE = this;
	}

	@Override
	public void quit() throws Exception {
	}

	/**
	 * Returns the UI node instance.
	 * 
	 * @return the UI node instance
	 */
	static UI getInstance() {
		return INSTANCE;
	}

	/**
	 * Sets the specified attribute of the specified component to the specified
	 * value.
	 * 
	 * @param componentId
	 *            ID of the addressed component
	 * @param attribute
	 *            attribute to set
	 * @param value
	 *            value to assign
	 */
	public void set(String componentId, String attribute, Object value) {
		Bus.post(new UISetEvent(componentId, attribute, value, null));
	}

	/**
	 * Sets the specified attribute of the specified component to the specified
	 * value only for the specified connection.
	 * 
	 * @param componentId
	 *            ID of the addressed component
	 * @param attribute
	 *            attribute to set
	 * @param value
	 *            value to assign
	 * @param connectionId
	 *            ID of the addressed connection
	 */
	public void set(String componentId, String attribute, Object value, String connectionId) {
		Bus.post(new ConnectionUISetEvent(componentId, attribute, value, connectionId));
	}

}
