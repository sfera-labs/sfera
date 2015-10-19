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
	public Node getSource();

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

}
