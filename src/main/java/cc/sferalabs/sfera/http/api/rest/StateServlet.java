package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.sferalabs.sfera.events.Event;

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

		HttpSession session = req.getSession(false);
		SubscriptionsSet subscriptions = (SubscriptionsSet) session
				.getAttribute(SubscribeServlet.SESSION_ATTR_SUBSCRIPTIONS);
		if (subscriptions == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No subscriptions");
			return;
		}

		String uri = req.getRequestURI();
		String subId = uri.substring(req.getServletPath().length() + 1);
		PollingSubscription subscription = (subId == null) ? null : subscriptions.get(subId);
		if (subscription == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Subscription not found");
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
