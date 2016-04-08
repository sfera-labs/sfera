package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.HttpResponse;

/**
 * <p>
 * API servlet handling user login.
 * </p>
 * <p>
 * The request requires the parameters 'user' and 'password'.
 * </p>
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class LoginServlet extends ApiServlet {

	public static final String PATH = ApiServlet.PATH + "login";

	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	@Override
	protected void processRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		String user = req.getParameter("user");
		String password = req.getParameter("password");

		try {
			req.login(user, password);
			logger.info("Login: {}", user);
			resp.sendResult("ok");
		} catch (ServletException e) {
			logger.warn(e.getMessage());
			HttpSession session = req.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			resp.sendErrors(HttpServletResponse.SC_UNAUTHORIZED,
					new ErrorMessage(0, e.getMessage()));
		}
	}

}
