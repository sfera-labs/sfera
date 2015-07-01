package cc.sferalabs.sfera.events;

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
