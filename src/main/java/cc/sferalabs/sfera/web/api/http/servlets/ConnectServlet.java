package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.ConnectionsSet;
import cc.sferalabs.sfera.web.api.http.HttpResponse;

/**
 * API servlet handlig connection requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class ConnectServlet extends AuthorizedUserServlet {

	public static final String PATH = ApiServlet.PATH + "connect";

	public static final String SESSION_ATTR_CONNECTIONS = "connections";
	private static final Logger logger = LoggerFactory.getLogger(ConnectServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		String sessionId = session.getId();
		ConnectionsSet connections = (ConnectionsSet) session
				.getAttribute(SESSION_ATTR_CONNECTIONS);
		if (connections == null) {
			connections = new ConnectionsSet();
			session.setAttribute(SESSION_ATTR_CONNECTIONS, connections);
			logger.debug("Created new connections set for session '{}'", sessionId);
		}
		Connection connection = new Connection();
		connections.put(connection);
		String cid = connection.getId();
		logger.debug("Connected - session '{}' connection '{}'", sessionId, cid);
		resp.send("cid", cid);
	}

}
