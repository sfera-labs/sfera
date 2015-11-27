/**
 * 
 */
package cc.sferalabs.sfera.script;

import java.util.ArrayList;
import java.util.List;

import cc.sferalabs.sfera.events.Nodes;

/**
 * TODO delete
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class ScriptNodes {

	private static List<ScriptNodeWrapper> nodes = new ArrayList<>();

	/**
	 * @param id
	 * @param node
	 */
	public synchronized static void put(String id, Object node) {
		ScriptNodeWrapper wrap = new ScriptNodeWrapper(id, node);
		Nodes.put(wrap, false);
		ScriptsEngine.putObjectInGlobalScope(id, node);
		nodes.add(wrap);
	}

	/**
	 * 
	 */
	public synchronized static void clear() {
		for (ScriptNodeWrapper wrap : nodes) {
			Nodes.remove(wrap.getId());
		}
		nodes.clear();
	}

}
