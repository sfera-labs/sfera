/**
 * 
 */
package cc.sferalabs.sfera.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.ConnectionsSet;
import cc.sferalabs.sfera.web.api.http.servlets.ConnectServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpSessionDestroyer implements HttpSessionListener {

	private static final Logger logger = LoggerFactory.getLogger(HttpSessionDestroyer.class);

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.debug("Creted new session: {}", se.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		ConnectionsSet connections = (ConnectionsSet) session
				.getAttribute(ConnectServlet.SESSION_ATTR_CONNECTIONS);
		if (connections != null) {
			for (Connection connection : connections.values()) {
				connection.destroy();
			}
		}
		logger.debug("Session '{}' destroyed", se.getSession().getId());
	}
}
