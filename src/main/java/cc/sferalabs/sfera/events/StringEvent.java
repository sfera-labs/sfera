package cc.sferalabs.sfera.events;

/**
 * Abstract class for events with textual value.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class StringEvent extends BaseEvent {

	private final String value;

	/**
	 * Constructs a {@code StringEvent} event with the specified ID, source and
	 * value.
	 * 
	 * @param source
	 *            the source node
	 * @param id
	 *            the event ID
	 * @param value
	 *            the event value
	 */
	public StringEvent(Node source, String id, String value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
