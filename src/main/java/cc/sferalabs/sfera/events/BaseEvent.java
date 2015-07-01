package cc.sferalabs.sfera.events;

public abstract class BaseEvent implements Event {

	private final Node source;
	private final String id;
	private final long timestamp;

	/**
	 * 
	 * @param source
	 * @param id
	 */
	public BaseEvent(Node source, String id) {
		this.timestamp = System.currentTimeMillis();
		this.source = source;
		this.id = source.getId() + "." + id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Node getSource() {
		return source;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}
}
