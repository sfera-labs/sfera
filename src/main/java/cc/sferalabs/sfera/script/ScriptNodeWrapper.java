/**
 * 
 */
package cc.sferalabs.sfera.script;

import cc.sferalabs.sfera.events.Node;

/**
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptNodeWrapper extends Node {

	private final Object scriptNode;

	/**
	 * 
	 */
	public ScriptNodeWrapper(String id, Object scriptNode) {
		super(id);
		this.scriptNode = scriptNode;
	}

	/**
	 * @return the scriptNode
	 */
	public Object getScriptNode() {
		return scriptNode;
	}

}
