package cc.sferalabs.sfera.http.api.rest.servlets.access;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.http.api.rest.MissingRequiredParamException;
import cc.sferalabs.sfera.http.api.rest.RestResponse;
import cc.sferalabs.sfera.http.api.rest.servlets.ApiServlet;
import cc.sferalabs.sfera.http.api.rest.servlets.AuthorizedAdminApiServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class RemoveAccessServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "access/remove";

	private final static Logger logger = LoggerFactory.getLogger(RemoveAccessServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		try {
			String username = getRequiredParam("username", req, resp);
			Access.removeUser(username);
			resp.sendResult("ok");
		} catch (MissingRequiredParamException e) {
		} catch (Exception e) {
			logger.error("Access remove error", e);
			resp.sendError("Access remove error: " + e);
		}
	}

}
