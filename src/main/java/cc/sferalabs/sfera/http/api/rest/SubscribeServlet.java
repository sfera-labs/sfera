package cc.sferalabs.sfera.http.api.rest;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class SubscribeServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "subscribe";

	public static final String SESSION_ATTR_SUBSCRIPTIONS = "subscriptions";
	private static final Logger logger = LogManager.getLogger();

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String id = req.getParameter("id");
		String nodes = req.getParameter("nodes");

		HttpSession session = req.getSession(false);
		SubscriptionsSet subscriptions = (SubscriptionsSet) session
				.getAttribute(SESSION_ATTR_SUBSCRIPTIONS);
		if (subscriptions == null) {
			subscriptions = new SubscriptionsSet();
			session.setAttribute(SESSION_ATTR_SUBSCRIPTIONS, subscriptions);
			logger.debug("Creted new subscriptions set for session '{}'", session.getId());
		}
		PollingSubscriber subscription = (id == null) ? null : subscriptions.get(id);
		if (subscription == null) {
			subscription = new PollingSubscriber();
			id = subscription.getId();
			subscriptions.put(id, subscription);
			logger.debug("Creted new subscription for session '{}' with ID: {}", session.getId(),
					id);
		}
		subscription.setNodesSpec(nodes);
		logger.debug("Subscribed: session '{}' subscription '{}' nodes: {}", session.getId(), id,
				nodes);

		resp.setContentType("application/json");
		resp.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = resp.getWriter();
		writer.write("{\"id\":\"" + id + "\"}");
	}

}
