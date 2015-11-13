package cc.sferalabs.sfera.events;

/**
 * Abstract class for events with numeric value.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class NumberEvent extends BaseEvent {

	private final Number value;

	/**
	 * Constructs a {@code NumberEvent} event with the specified ID, source and
	 * value.
	 * 
	 * @param source
	 *            the source node
	 * @param id
	 *            the event ID
	 * @param value
	 *            the event value
	 */
	public NumberEvent(Node source, String id, Number value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public Number getValue() {
		return value;
	}

}
