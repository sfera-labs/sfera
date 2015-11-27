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
	 * 
	 */
	public Node(String id) {
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
	 * 
	 */
	public void destroy() {
		Nodes.remove(id);
	}
}
