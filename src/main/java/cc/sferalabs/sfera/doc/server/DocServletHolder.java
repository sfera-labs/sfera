package cc.sferalabs.sfera.doc.server;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class DocServletHolder extends ServletHolder {

	static final DocServletHolder INSTANCE = new DocServletHolder();

	/**
	 * 
	 */
	private DocServletHolder() {
		super(DefaultServlet.class);
		setInitParameter("resourceBase", ".");
	}

}
