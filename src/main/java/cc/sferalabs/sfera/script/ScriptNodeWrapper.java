/**
 * 
 */
package cc.sferalabs.sfera.script;

import cc.sferalabs.sfera.events.Node;

/**
 * TODO delete
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptNodeWrapper implements Node {

	private final String id;
	private final Object scriptNode;

	/**
	 * 
	 */
	public ScriptNodeWrapper(String id, Object scriptNode) {
		this.id = id;
		this.scriptNode = scriptNode;
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * @return the scriptNode
	 */
	public Object getScriptNode() {
		return scriptNode;
	}

}
