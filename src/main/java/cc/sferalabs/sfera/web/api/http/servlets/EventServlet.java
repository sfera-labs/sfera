package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.web.api.WebApiEvent;
import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.RestResponse;

/**
 * <p>
 * API servlet handling requests for events triggering.
 * </p>
 * <p>
 * The triggered events will be instances of {@link WebApiEvent} and the source
 * node an {@link cc.sferalabs.sfera.web.api.WebNode HttpRemoteNode} instance.
 * </p>
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class EventServlet extends AuthorizedUserServlet {

	public static final String PATH = ApiServlet.PATH + "event";

	private static final Logger logger = LoggerFactory.getLogger(EventServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		Connection connection = ConnectServlet.getConnection(req, resp);
		if (connection == null) {
			return;
		}
		String id = req.getParameter("id");
		String value = req.getParameter("value");
		WebApiEvent remoteEvent;
		try {
			remoteEvent = new WebApiEvent(id, value, req, connection.getId());
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		logger.info("Event: {} = {} User: {}", id, value, req.getRemoteUser());
		Bus.post(remoteEvent);
		resp.sendResult("ok");
	}

}
