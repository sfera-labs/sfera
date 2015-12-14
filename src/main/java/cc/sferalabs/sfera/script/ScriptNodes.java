/**
 * 
 */
package cc.sferalabs.sfera.script;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class ScriptNodes {

	private static List<ScriptNodeWrapper> nodes = new ArrayList<>();

	/**
	 * Adds the specified script node to the collection of available nodes,
	 * wrapped in a {@link ScriptNodeWrapper}.
	 * <p>
	 * This method is solely called from sfera.js.
	 * </p>
	 * 
	 * @param id
	 *            the node ID
	 * @param node
	 *            the script node to be added
	 */
	public synchronized static void put(String id, Object node) {
		ScriptNodeWrapper wrap = new ScriptNodeWrapper(id, node);
		ScriptsEngine.putObjectInGlobalScope(id, node);
		nodes.add(wrap);
	}

	/**
	 * Destroys all the added nodes and clears the list.
	 */
	public synchronized static void clear() {
		for (ScriptNodeWrapper wrap : nodes) {
			wrap.destroy();
		}
		nodes.clear();
	}

}
