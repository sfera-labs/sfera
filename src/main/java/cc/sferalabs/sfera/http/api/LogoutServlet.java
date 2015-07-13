package cc.sferalabs.sfera.http.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class LogoutServlet extends ApiServlet {

	public static final String PATH = ApiServlet.PATH + "logout";

	private static final Logger logger = LogManager.getLogger();

	@Override
	protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		HttpSession session = req.getSession(false);
		if (session != null) {
			String user = req.getRemoteUser();
			session.invalidate();
			resp.setStatus(HttpServletResponse.SC_OK);
			logger.info("Logout: {}", user);
		}
	}

}
