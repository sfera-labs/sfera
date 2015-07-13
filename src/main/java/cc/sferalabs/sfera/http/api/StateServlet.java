package cc.sferalabs.sfera.http.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import cc.sferalabs.sfera.events.Event;

@SuppressWarnings("serial")
public class StateServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "state/*";

	@SuppressWarnings("unchecked")
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
		Subscription subscription = (subId == null) ? null : subscriptions.get(subId);
		if (subscription == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Map<String, Event> changes = subscription.pollChanges(ts, 5, TimeUnit.SECONDS);
		JSONObject obj = new JSONObject();
		obj.put("timestamp", System.currentTimeMillis());
		JSONObject nodes = new JSONObject();
		for (Entry<String, Event> change : changes.entrySet()) {
			nodes.put(change.getKey(), change.getValue().getValue());
		}
		obj.put("nodes", nodes);

		StringWriter out = new StringWriter();
		obj.writeJSONString(out);

		resp.setContentType("application/json");
		resp.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = resp.getWriter();
		writer.write(out.toString());
	}

}
