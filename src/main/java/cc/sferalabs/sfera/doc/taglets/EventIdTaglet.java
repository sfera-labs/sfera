package cc.sferalabs.sfera.doc.taglets;

import java.util.Map;

/**
 * Taglet sfera.event_id
 * <p>
 * Usage:
 * <p>
 * <b>&#64;sfera.event_id</b> &lt;event_id&gt; &lt;optional description for
 * parametrid IDs&gt;
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class EventIdTaglet extends SimpleTaglet {

	@Override
	public String getName() {
		return "sfera.event_id";
	}

	@Override
	protected String getHeader() {
		return "Event ID:";
	}

	@Override
	public boolean inType() {
		return true;
	}

	/**
	 * Register this Taglet.
	 * 
	 * @param tagletMap
	 *            the map to register this tag to.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void register(Map tagletMap) {
		EventIdTaglet tag = new EventIdTaglet();
		tagletMap.put(tag.getName(), tag);
	}

}