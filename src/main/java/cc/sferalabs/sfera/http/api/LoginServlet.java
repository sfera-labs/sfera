package cc.sferalabs.sfera.http.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class LoginServlet extends ApiServlet {

	public static final String PATH = ApiServlet.PATH + "login";

	public static final String SESSION_ATTR_USERNAME = "user";
	private static final Logger logger = LogManager.getLogger();

	@Override
	protected void processRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		String user = req.getParameter("user");
		String password = req.getParameter("password");

		try {
			req.login(user, password);
			HttpSession session = req.getSession(true);
			session.setAttribute(SESSION_ATTR_USERNAME, user);
			resp.setStatus(HttpServletResponse.SC_OK);
			logger.info("Login: {}", user);
		} catch (ServletException e) {
			logger.warn(e.getMessage());
			HttpSession session = req.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}
