package cc.sferalabs.sfera.events;

/**
 * Abstract class for events with boolean value.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class BooleanEvent extends BaseEvent {

	private final Boolean value;

	/**
	 * Constructs a {@code BooleanEvent} event with the specified ID, source and
	 * value.
	 * 
	 * @param source
	 *            the source node
	 * @param id
	 *            the event ID
	 * @param value
	 *            the event value
	 */
	public BooleanEvent(Node source, String id, Boolean value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

}
