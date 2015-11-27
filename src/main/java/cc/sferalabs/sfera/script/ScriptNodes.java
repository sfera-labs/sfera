/**
 * 
 */
package cc.sferalabs.sfera.script;

import java.util.ArrayList;
import java.util.List;

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
		ScriptsEngine.putObjectInGlobalScope(id, node);
		nodes.add(wrap);
	}

	/**
	 * 
	 */
	public synchronized static void clear() {
		for (ScriptNodeWrapper wrap : nodes) {
			wrap.destroy();
		}
		nodes.clear();
	}

}
