package cc.sferalabs.sfera.doc.taglets;

import java.util.Map;

/**
 * Taglet sfera.event_val
 * <p>
 * Usage:
 * <p>
 * <b>&#64;sfera.event_val</b> &lt;event_value&gt; &lt;description&gt;
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class EventValueTaglet extends BaseTaglet {

	@Override
	public String getName() {
		return "sfera.event_val";
	}

	@Override
	protected String getHeader() {
		return "Values:";
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
		EventValueTaglet tag = new EventValueTaglet();
		tagletMap.put(tag.getName(), tag);
	}

}