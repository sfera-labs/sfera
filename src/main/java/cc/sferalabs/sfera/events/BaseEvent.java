package cc.sferalabs.sfera.events;

/**
 * Base {@link Event} implementation.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class BaseEvent implements Event {

	private final Node source;
	private final String id;
	private final long timestamp;

	/**
	 * Constructs a {@code BaseEvent} event with the specified ID and the
	 * specified {@code Node} as source.
	 * 
	 * @param source
	 *            the source node
	 * @param id
	 *            the event ID
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
