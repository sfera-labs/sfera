/**
 * 
 */
package cc.sferalabs.sfera.ui;

import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.Nodes;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class UI implements AutoStartService, Node {

	private static final UI INSTANCE = new UI();

	@Override
	public void init() throws Exception {
		Nodes.put(INSTANCE);
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
	public void set(String id, String attribute, Object value) {
		Bus.post(new UISetEvent(id, attribute, value));
	}

}
