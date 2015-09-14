package cc.sferalabs.sfera.http.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public abstract class AuthorizedApiServlet extends ApiServlet {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizedApiServlet.class);

	@Override
	protected void processRequest(HttpServletRequest req, RestResponse resp) throws Exception {
		if (isAuthorized(req)) {
			processAuthorizedRequest(req, resp);
		} else {
			logger.warn("Unauthorized API request from {}: {}", req.getRemoteHost(),
					req.getRequestURI());
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private boolean isAuthorized(HttpServletRequest req) {
		return req.isUserInRole("admin") || req.isUserInRole("api");
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	abstract protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws Exception;

}
