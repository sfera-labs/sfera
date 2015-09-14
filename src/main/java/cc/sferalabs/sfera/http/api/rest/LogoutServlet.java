package cc.sferalabs.sfera.http.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class LogoutServlet extends ApiServlet {

	public static final String PATH = ApiServlet.PATH + "logout";

	private static final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

	@Override
	protected void processRequest(HttpServletRequest req, RestResponse resp) throws Exception {
		HttpSession session = req.getSession(false);
		if (session != null) {
			String user = req.getRemoteUser();
			session.invalidate();
			resp.sendResult("ok");
			logger.info("Logout: {}", user);
		}
	}

}
