package cc.sferalabs.sfera.http.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class SubscribeServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "subscribe";

	public static final String SESSION_ATTR_SUBSCRIPTIONS = "subscriptions";
	private static final Logger logger = LoggerFactory.getLogger(SubscribeServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
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
		PollingSubscription subscription = (id == null) ? null : subscriptions.get(id);
		if (subscription == null) {
			subscription = new PollingSubscription();
			id = subscription.getId();
			subscriptions.put(id, subscription);
			logger.debug("Creted new subscription for session '{}' with ID: {}", session.getId(),
					id);
		}
		subscription.setIdSpec(nodes);
		logger.debug("Subscribed: session '{}' subscription '{}' nodes: {}", session.getId(), id,
				nodes);

		resp.put("id", id);
		resp.send();
	}

}
