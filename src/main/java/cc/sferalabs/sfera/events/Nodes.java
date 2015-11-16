/**
 * 
 */
package cc.sferalabs.sfera.events;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Nodes {

	private static final Map<String, Node> nodes = new HashMap<>();

	/**
	 * Adds the specified node to the collection of nodes accessible via the
	 * {@link #get(String)} method.
	 * 
	 * @param node
	 *            the node to add
	 * @throws IllegalArgumentException
	 *             if a node with the same ID has been already added
	 */
	public static void put(Node node) throws IllegalArgumentException {
		String id = node.getId();
		if (nodes.containsKey(id)) {
			throw new IllegalArgumentException("Node with same ID already added");
		}
		nodes.put(id, node);
	}

	/**
	 * Returns the node with the specified ID, or {@code null} if no node with
	 * the specified ID has been added.
	 * 
	 * @param id
	 *            the ID of the node to be returned
	 * @return the node with the specified ID, or {@code null} if no node with
	 *         the specified ID has been added
	 */
	public static Node get(String id) {
		return nodes.get(id);
	}

	/**
	 * Removes the node with the specified ID if it is present.
	 * 
	 * @param id
	 *            the ID of the node to be removed
	 * @return the removed node, or {@code null} if there was no node with the
	 *         specified ID
	 */
	public static Node remove(String id) {
		return nodes.remove(id);
	}

}
