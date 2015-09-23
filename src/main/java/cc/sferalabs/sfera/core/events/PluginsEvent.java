package cc.sferalabs.sfera.core.events;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class PluginsEvent extends SystemStringEvent {

	public static final PluginsEvent RELOAD = new PluginsEvent("reload");

	/**
	 * 
	 * @param event
	 */
	private PluginsEvent(String event) {
		super("plugins", event);
	}

}
