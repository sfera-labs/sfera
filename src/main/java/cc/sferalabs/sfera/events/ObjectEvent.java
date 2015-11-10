package cc.sferalabs.sfera.events;

/**
 * Abstract class for events with a general Object as value.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class ObjectEvent extends BaseEvent {

	private final Object value;

	/**
	 * Constructs a {@code ObjectEvent} event with the specified ID, source and
	 * value.
	 * 
	 * @param source
	 *            the source node
	 * @param id
	 *            the event ID
	 * @param value
	 *            the event value
	 */
	public ObjectEvent(Node source, String id, Object value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}

}
