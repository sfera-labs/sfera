/**
 * 
 */
package cc.sferalabs.sfera.script;

import cc.sferalabs.sfera.events.Node;

/**
 * Wrapper for nodes defined in scripts.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptNodeWrapper extends Node {

	private final Object scriptNode;

	/**
	 * Constructs a ScriptNodeWrapper.
	 * 
	 * @param id
	 *            the node ID
	 * @param scriptNode
	 *            the script node to wrap
	 */
	public ScriptNodeWrapper(String id, Object scriptNode) {
		super(id);
		this.scriptNode = scriptNode;
	}

	/**
	 * Returns the script node.
	 * 
	 * @return the script node
	 */
	public Object getScriptNode() {
		return scriptNode;
	}

}
