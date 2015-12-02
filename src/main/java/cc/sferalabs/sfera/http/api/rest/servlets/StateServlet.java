package cc.sferalabs.sfera.http.api.rest.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.http.api.rest.Connection;
import cc.sferalabs.sfera.http.api.rest.PollingSubscription;
import cc.sferalabs.sfera.http.api.rest.RestResponse;

/**
 * <p>
 * API servlet handling state requests.
 * </p>
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class StateServlet extends AuthorizedUserServlet {

	public static final String PATH = ApiServlet.PATH + "state/*";

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		Connection connection = ConnectServlet.getConnection(req, resp);
		if (connection == null) {
			return;
		}

		long ack;
		try {
			ack = Long.parseLong(req.getParameter("ack"));
		} catch (NumberFormatException nfe) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "'ack' not provided");
			return;
		}
		long timeout;
		try {
			timeout = Long.parseLong(req.getParameter("timeout"));
			if (timeout < 0) {
				timeout = 0;
			}
		} catch (NumberFormatException nfe) {
			timeout = 0;
		}

		PollingSubscription subscription = connection.getSubscription();
		if (subscription == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Not subscribed");
			return;
		}

		try {
			Collection<Event> changes = subscription.pollChanges(ack, timeout, TimeUnit.SECONDS);
			Map<String, Object> nodes = new HashMap<>();
			for (Event ev : changes) {
				nodes.put(ev.getId(), ev.getValue());
			}
			resp.sendResult(nodes);
		} catch (InterruptedException e) {
			resp.sendError("Interrupted");
		}
	}

}
