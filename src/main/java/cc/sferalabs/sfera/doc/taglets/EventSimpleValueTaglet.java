package cc.sferalabs.sfera.doc.taglets;

import java.util.Map;

/**
 * Taglet sfera.event_val_simple
 * <p>
 * Usage:
 * <p>
 * <b>&#64;sfera.event_val_simple</b> &lt;event_simple_value&gt; &lt;description&gt;
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class EventSimpleValueTaglet extends BaseTaglet {

	@Override
	public String getName() {
		return "sfera.event_val_simple";
	}

	@Override
	protected String getHeader() {
		return "Simple Values:";
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
		EventSimpleValueTaglet tag = new EventSimpleValueTaglet();
		tagletMap.put(tag.getName(), tag);
	}

}