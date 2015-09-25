package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class LoginServlet extends ApiServlet {

	public static final String PATH = ApiServlet.PATH + "login";

	public static final String SESSION_ATTR_USERNAME = "user";
	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	@Override
	protected void processRequest(HttpServletRequest req, RestResponse resp) throws ServletException, IOException {
		String user = req.getParameter("user");
		String password = req.getParameter("password");

		try {
			req.login(user, password);
			HttpSession session = req.getSession(true);
			session.setAttribute(SESSION_ATTR_USERNAME, user);
			resp.sendResult("ok");
			logger.info("Login: {}", user);
		} catch (ServletException e) {
			logger.warn(e.getMessage());
			HttpSession session = req.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
	}

}
