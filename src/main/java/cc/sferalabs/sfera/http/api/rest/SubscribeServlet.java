package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public static final String SESSION_ATTR_SUBSCRIPTIONS = "subscriptions";
	private static final Logger logger = LoggerFactory.getLogger(SubscribeServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		String id = req.getParameter("id");
		String nodes = req.getParameter("nodes");
		if (nodes == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nodes not specified");
			return;
		}

		HttpSession session = req.getSession(false);
		String sessionId = session.getId();
		SubscriptionsSet subscriptions = (SubscriptionsSet) session
				.getAttribute(SESSION_ATTR_SUBSCRIPTIONS);
		if (subscriptions == null) {
			subscriptions = new SubscriptionsSet();
			session.setAttribute(SESSION_ATTR_SUBSCRIPTIONS, subscriptions);
			logger.debug("Created new subscriptions set for session '{}'", sessionId);
		}
		PollingSubscription subscription = new PollingSubscription(id, nodes);
		PollingSubscription prev = subscriptions.put(subscription);
		if (prev != null) {
			prev.destroy();
		}
		id = subscription.getId();
		logger.debug("Subscribed - session '{}' subscription '{}' nodes: {}", sessionId, id, nodes);
		resp.send("id", id);
	}

}
