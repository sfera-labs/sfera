package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * API servlet handling user logout.
 * </p>
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class LogoutServlet extends ApiServlet {

	public static final String PATH = ApiServlet.PATH + "logout";

	private static final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

	@Override
	protected void processRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			String user = req.getRemoteUser();

			SubscriptionsSet subscriptions = (SubscriptionsSet) session
					.getAttribute(SubscribeServlet.SESSION_ATTR_SUBSCRIPTIONS);
			if (subscriptions != null) {
				for (PollingSubscription ps : subscriptions.values()) {
					ps.destroy();
				}
			}
			session.invalidate();

			resp.sendResult("ok");
			logger.info("Logout: {}", user);
		}
	}

}
