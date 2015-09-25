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

	private final Double value;

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
	public NumberEvent(Node source, String id, Double value) {
		super(source, id);
		this.value = value;
	}

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
	public NumberEvent(Node source, String id, Integer value) {
		this(source, id, value == null ? null : value.doubleValue());
	}

	@Override
	public Double getValue() {
		return value;
	}

}
