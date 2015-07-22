package cc.sferalabs.sfera.http.api.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.sferalabs.sfera.events.Event;

@SuppressWarnings("serial")
public class StateServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "state/*";

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		long ts;
		try {
			ts = Long.parseLong(req.getParameter("ts"));
		} catch (NumberFormatException nfe) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Timestamp not provided");
			return;
		}

		HttpSession session = req.getSession(false);
		SubscriptionsSet subscriptions = (SubscriptionsSet) session
				.getAttribute(SubscribeServlet.SESSION_ATTR_SUBSCRIPTIONS);
		if (subscriptions == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String uri = req.getRequestURI();
		String subId = uri.substring(req.getServletPath().length() + 1);
		PollingSubscriber subscription = (subId == null) ? null : subscriptions.get(subId);
		if (subscription == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Collection<Event> changes = subscription.pollChanges(ts, 20, TimeUnit.SECONDS);
		RestResponse rr = new RestResponse(resp);
		rr.put("timestamp", System.currentTimeMillis());
		Map<String, Object> nodes = new HashMap<>();
		for (Event ev : changes) {
			nodes.put(ev.getId(), ev.getValue());
		}
		rr.put("nodes", nodes);
		rr.send();
	}

}
