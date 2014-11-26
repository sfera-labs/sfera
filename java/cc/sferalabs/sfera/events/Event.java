package cc.sferalabs.sfera.events;


public abstract class Event {
	
	private final Node source;
	private final String id;
	private final long timestamp;

	/**
	 * 
	 * @param source
	 * @param id
	 */
	public Event(Node source, String id) {
		this.timestamp = System.currentTimeMillis();
		this.source = source;
		this.id = source.getId() + "." + id;
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public Node getSource() {
		return source;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 * @return
	 */
	public abstract Object getValue();
}
