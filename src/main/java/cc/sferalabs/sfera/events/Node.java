package cc.sferalabs.sfera.events;

/**
 * Class representing a Node.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Node {

	private final String id;

	/**
	 * Constructs a {@code Node} with the specified ID and adds it to the list
	 * of existing nodes.
	 * 
	 * @param id
	 *            the node ID
	 * @throws IllegalArgumentException
	 *             if a node with the same ID has been already created
	 */
	public Node(String id) throws IllegalArgumentException {
		this.id = id;
		Nodes.put(this);
	}

	/**
	 * Returns the node ID
	 * 
	 * @return the node ID
	 */
	public final String getId() {
		return id;
	}

	/**
	 * Removes the node from the list of existing nodes.
	 */
	public void destroy() {
		Nodes.remove(id);
	}
}
