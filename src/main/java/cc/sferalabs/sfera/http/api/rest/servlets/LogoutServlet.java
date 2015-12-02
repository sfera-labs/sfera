package cc.sferalabs.sfera.http.api.rest.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.http.api.rest.RestResponse;

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
		String user = req.getRemoteUser();
		req.logout();
		resp.sendResult("ok");
		logger.info("Logout: {}", user);
	}

}
