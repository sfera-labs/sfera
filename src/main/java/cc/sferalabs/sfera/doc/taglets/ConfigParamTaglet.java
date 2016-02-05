package cc.sferalabs.sfera.doc.taglets;

import java.util.Map;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

/**
 * Taglet sfera.config
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ConfigParamTaglet implements Taglet {

	private static final String NAME = "sfera.config";
	private static final String HEADER = "Configuration parameters:";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean inConstructor() {
		return false;
	}

	@Override
	public boolean inField() {
		return false;
	}

	@Override
	public boolean inMethod() {
		return false;
	}

	@Override
	public boolean inOverview() {
		return true;
	}

	@Override
	public boolean inPackage() {
		return false;
	}

	@Override
	public boolean inType() {
		return false;
	}

	@Override
	public boolean isInlineTag() {
		return false;
	}

	@Override
	public String toString(Tag tag) {
		return toString(new Tag[] { tag });
	}

	@Override
	public String toString(Tag[] tags) {
		if (tags.length == 0) {
			return null;
		}
		StringBuilder html = new StringBuilder("<br /><dt><b>" + HEADER + "</b></dt><dd><dl>");
		for (Tag tag : tags) {
			String text = tag.text().trim();
			if (text.length() > 0) {
				String param = text.split("\\s+")[0];
				String descr = "";
				if (text.length() > param.length()) {
					descr = text.substring(param.length()).trim();
				}
				html.append("<dt><code>").append(param).append("</code></dt>");
				html.append("<dd>").append(descr).append("</dd>");
			}
		}
		html.append("</dl></dd>");
		return html.toString();
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
		Taglet t = (Taglet) tagletMap.get(tag.getName());
		if (t != null) {
			tagletMap.remove(tag.getName());
		}
		tagletMap.put(tag.getName(), tag);
	}

}