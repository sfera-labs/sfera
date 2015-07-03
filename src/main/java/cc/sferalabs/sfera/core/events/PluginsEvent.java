package cc.sferalabs.sfera.core.events;


public class PluginsEvent extends SystemEvent {

	public static final PluginsEvent RELOAD = new PluginsEvent("reload");
	
	private final String event;

	/**
	 * 
	 * @param event
	 */
	private PluginsEvent(String event) {
		super("plugins");
		this.event = event;
	}

	@Override
	public String getValue() {
		return event;
	}

}
