package cc.sferalabs.sfera.core.events;

public class SystemStateEvent extends SystemEvent {

	public static final SystemStateEvent START = new SystemStateEvent("start");
	public static final SystemStateEvent READY = new SystemStateEvent("ready");
	public static final SystemStateEvent QUIT = new SystemStateEvent("quit");

	private String state;

	/**
	 * 
	 * @param state
	 */
	private SystemStateEvent(String state) {
		super("state");
		this.state = state;
	}

	@Override
	public String getValue() {
		return state;
	}

}
