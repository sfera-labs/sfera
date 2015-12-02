package cc.sferalabs.sfera.http.api.rest.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.http.api.rest.Connection;
import cc.sferalabs.sfera.http.api.rest.ConnectionsSet;
import cc.sferalabs.sfera.http.api.rest.RestResponse;

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
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
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

	/**
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	static Connection getConnection(HttpServletRequest req, RestResponse resp)
			throws IllegalStateException, IOException {
		String cid = req.getParameter("cid");
		if (cid == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Connection ID not specified");
			return null;
		}
		HttpSession session = req.getSession(false);
		ConnectionsSet connections = (ConnectionsSet) session
				.getAttribute(SESSION_ATTR_CONNECTIONS);
		if (connections == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No connections instantiated");
			return null;
		}
		Connection connection = connections.get(cid);
		if (connection == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Connection '" + cid + "' not found");
			return null;
		}

		return connection;
	}

}
