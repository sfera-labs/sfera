package cc.sferalabs.sfera.web.api.http.servlets.access;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.UserNotFoundException;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;
import cc.sferalabs.sfera.web.api.http.servlets.ApiServlet;

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

	@Override
	protected void processRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			String username = getRequiredParameter("username", req, resp);
			String password = req.getParameter("password");
			String roles = req.getParameter("roles");
			String[] rs = (roles == null) ? null : roles.split("\\s*,\\s*");

			if (!req.isUserInRole("admin")) {
				if (!username.equals(req.getRemoteUser()) || roles != null) {
					// non-admins can only update themselves and cannot change
					// their roles
					resp.sendErrors(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.UNAUTHORIZED);
					return;
				}
				String oldPassword = getRequiredParameter("old_password", req, resp);
				if (Access.authenticate(username, oldPassword) == null) {
					// wrong password
					resp.sendErrors(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.UNAUTHORIZED);
					return;
				}
			}

			Access.updateUser(username, password, rs);
			resp.sendResult("ok");

		} catch (MissingRequiredParamException e) {
		} catch (UserNotFoundException e) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, e.getMessage()));
		}
	}

}
