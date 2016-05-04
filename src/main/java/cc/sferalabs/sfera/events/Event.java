package cc.sferalabs.sfera.events;

/**
 * Base interface for all the events
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface Event {

	/**
	 * Returns the event ID
	 * 
	 * @return the event ID
	 */
	public String getId();

	/**
	 * Returns the event ID without the source ID prefix
	 * 
	 * @return the event ID without the source ID prefix
	 */
	public String getSubId();

	/**
	 * Returns the source node that generated this event
	 * 
	 * @return the source node
	 */
	public Object getSource();

	/**
	 * Returns the timestamp in milliseconds of the moment this event was
	 * created
	 * 
	 * @return the event creation timestamp
	 */
	public long getTimestamp();

	/**
	 * Returns the value of this event
	 * 
	 * @return the value of this event
	 */
	public Object getValue();

	/**
	 * <p>
	 * Returns a simple-form value used in trigger conditions' comparisons.
	 * </p>
	 * <p>
	 * For events with simple values (i.e. boolean, numeric or String values)
	 * this method should just return the value returned by {@link #getValue()}.
	 * </p>
	 * <p>
	 * Events with structured values (e.g. objects with several attributes)
	 * should return a simple value that represents the attribute of the
	 * structured value that is most likely used in trigger conditions in
	 * scripts.
	 * </p>
	 * <p>
	 * For instance, an event representing a received text message, whose value
	 * is an object with a {@code sender}, a {@code sentTime} and a
	 * {@code content} attribute, might have this method returning the
	 * {@code content} String, so that the following trigger condition can be
	 * used:
	 * </p>
	 * 
	 * <pre>
	 *     node.message == "hello" : { ... }
	 * </pre>
	 * 
	 * @return the simple-form value of this event
	 */
	public Object getSimpleValue();

}
