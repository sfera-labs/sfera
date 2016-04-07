package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.http.RestResponse;

/**
 * Abstract {@link ApiServlet} class extension to be extended by servlets
 * handling requests that need user authorization.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public abstract class AuthorizedApiServlet extends ApiServlet {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizedApiServlet.class);

	/**
	 * @return an array containing the user roles required by this servlet for
	 *         requests to be authorized
	 */
	public abstract String[] getRoles();

	@Override
	protected void processRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		if (isAuthorized(req)) {
			processAuthorizedRequest(req, resp);
		} else {
			logger.warn("Unauthorized API request from {}: {}", req.getRemoteHost(),
					req.getRequestURI());
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}
	}

	/**
	 * Returns a boolean indicating whether the specified request is authorized.
	 * 
	 * @param req
	 *            the request to be authorized
	 * 
	 * @return {@code true} if the request is authorized, {@code false}
	 *         otherwise.
	 */
	protected boolean isAuthorized(HttpServletRequest req) {
		for (String role : getRoles()) {
			if (req.isUserInRole(role)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Processes the authorized API request.
	 * 
	 * @param req
	 *            an {@link HttpServletRequest} object that contains the request
	 *            the client has made of the servlet
	 * @param resp
	 *            an {@link HttpServletResponse} object that contains the
	 *            response the servlet sends to the client
	 * 
	 * @throws ServletException
	 *             if the request could not be handled
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	abstract protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException;

}
