package cc.sferalabs.sfera.doc.taglets;

import java.util.Map;

/**
 * Taglet sfera.config
 * <p>
 * Usage:
 * <p>
 * <b>&#64;sfera.config</b> &lt;param_name&gt; &lt;description&gt;
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ConfigParamTaglet extends BaseTaglet {

	@Override
	public String getName() {
		return "sfera.config";
	}

	@Override
	protected String getHeader() {
		return "Configuration parameters:";
	}

	@Override
	public boolean inOverview() {
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
		ConfigParamTaglet tag = new ConfigParamTaglet();
		tagletMap.put(tag.getName(), tag);
	}

}