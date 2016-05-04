package cc.sferalabs.sfera.doc.taglets;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

/**
 * Abstract class for simple Taglets.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class BaseTaglet implements Taglet {

	/**
	 * @return the header for this taglet
	 */
	protected abstract String getHeader();

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
		return false;
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
		StringBuilder html = new StringBuilder("<br /><dt><b>" + getHeader() + "</b></dt><dd><dl>");
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

}