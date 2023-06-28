package cc.sferalabs.sfera.events;

public abstract class SubNode extends Node {

	private final Node parent;
	private final String subId;

	/**
	 * Constructs a {@code SubNode} with the specified parent {@code Node} and
	 * sub-ID and adds it to the list of existing nodes.
	 * 
	 * @param parent
	 * 			the parent node
	 * @param subId 
	 * 			the node sub-ID
	 * @throws IllegalArgumentException
	 * 			if a node with the same ID has been already created
	 */
	public SubNode(Node parent, String subId) throws IllegalArgumentException {
		super(parent.getId(), subId);
		this.parent = parent;
		this.subId = subId;
	}

	/**
	 * Returns the parent Node.
	 * 
	 * @return the parent Node
	 */
	public Node getParent() {
		return parent;
	}
	
	/**
	 * Returns this {@code SubNode}'s sub-ID.
	 * 
	 * @return this {@code SubNode}'s sub-ID
	 */
	public String getSubId() {
		return subId;
	}

}
