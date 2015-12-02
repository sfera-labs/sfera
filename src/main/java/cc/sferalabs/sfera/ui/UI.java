/**
 * 
 */
package cc.sferalabs.sfera.ui;

import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class UI extends Node implements AutoStartService {

	private static UI INSTANCE;

	/**
	 * @param id
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
	 * @return
	 */
	static UI getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param componentId
	 *            ID of the addressed components
	 * @param attribute
	 *            attribute to set
	 * @param value
	 *            value to assign
	 */
	public void set(String componentId, String attribute, Object value) {
		Bus.post(new UISetEvent(componentId, attribute, value, null));
	}

	/**
	 * 
	 * @param componentId
	 * @param attribute
	 * @param value
	 * @param connectionId
	 */
	public void set(String componentId, String attribute, Object value, String connectionId) {
		Bus.post(new ConnectionUISetEvent(componentId, attribute, value, connectionId));
	}

}
