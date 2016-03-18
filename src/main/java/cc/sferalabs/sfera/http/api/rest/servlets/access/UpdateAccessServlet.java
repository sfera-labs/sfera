package cc.sferalabs.sfera.http.api.rest.servlets.access;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.http.api.rest.MissingRequiredParamException;
import cc.sferalabs.sfera.http.api.rest.RestResponse;
import cc.sferalabs.sfera.http.api.rest.servlets.ApiServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class UpdateAccessServlet extends ApiServlet {

	public static final String PATH = ApiServlet.PATH + "access/update";

	private final static Logger logger = LoggerFactory.getLogger(UpdateAccessServlet.class);

	@Override
	protected void processRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		try {
			String username = getRequiredParam("username", req, resp);
			String password = req.getParameter("password");
			String roles = req.getParameter("roles");
			String[] rs = (roles == null) ? null : roles.split("\\s*,\\s*");

			if (!req.isUserInRole("admin")) {
				if (!username.equals(req.getRemoteUser())) {
					resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User mismatch");
					return;
				}
				if (roles != null) {
					resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cannot modify roles");
					return;
				}
				String oldPassword = getRequiredParam("old_password", req, resp);
				if (Access.authenticate(username, oldPassword) == null) {
					resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong password");
					return;
				}
			}

			Access.updateUser(username, password, rs);
			resp.sendResult("ok");

		} catch (MissingRequiredParamException e) {
		} catch (Exception e) {
			logger.error("Access update error", e);
			resp.sendError("Access update error: " + e);
		}
	}

}
