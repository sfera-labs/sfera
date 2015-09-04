package cc.sferalabs.sfera.events;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface Event {

	/**
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 
	 * @return
	 */
	public Node getSource();

	/**
	 * 
	 * @return
	 */
	public long getTimestamp();

	/**
	 * 
	 * @return
	 */
	public Object getValue();

}
