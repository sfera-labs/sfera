package cc.sferalabs.sfera.core.events;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class SystemStateEvent extends SystemStringEvent {

	public static final SystemStateEvent START = new SystemStateEvent("start");
	public static final SystemStateEvent READY = new SystemStateEvent("ready");
	public static final SystemStateEvent QUIT = new SystemStateEvent("quit");

	/**
	 * 
	 * @param state
	 */
	private SystemStateEvent(String state) {
		super("state", state);
	}

}
