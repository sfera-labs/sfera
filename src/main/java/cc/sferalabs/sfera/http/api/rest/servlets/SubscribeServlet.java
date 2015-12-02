package cc.sferalabs.sfera.http.api.rest.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.http.api.rest.Connection;
import cc.sferalabs.sfera.http.api.rest.RestResponse;

/**
 * API servlet handlig subscription requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class SubscribeServlet extends AuthorizedUserServlet {

	public static final String PATH = ApiServlet.PATH + "subscribe";

	private static final Logger logger = LoggerFactory.getLogger(SubscribeServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		Connection connection = ConnectServlet.getConnection(req, resp);
		if (connection == null) {
			return;
		}

		String nodes = req.getParameter("nodes");
		if (nodes == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nodes not specified");
			return;
		}
		connection.subscribe(nodes);
		logger.debug("Subscribed - session '{}' connection '{}' nodes: {}",
				req.getSession(false).getId(), connection.getId(), nodes);
		resp.sendResult("ok");
	}

}
