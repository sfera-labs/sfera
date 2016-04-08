package cc.sferalabs.sfera.web.api.http.servlets.access;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.UsernameAlreadyUsedException;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;
import cc.sferalabs.sfera.web.api.http.servlets.ApiServlet;
import cc.sferalabs.sfera.web.api.http.servlets.AuthorizedAdminApiServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class AddAccessServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "access/add";

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			String username = getRequiredParameter("username", req, resp);
			String password = getRequiredParameter("password", req, resp);
			String roles = getRequiredParameter("roles", req, resp);
			String[] rs = roles.split("\\s*,\\s*");
			Access.addUser(username, password, rs);
			resp.sendResult("ok");

		} catch (MissingRequiredParamException e) {
		} catch (UsernameAlreadyUsedException e) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, e.getMessage()));
		}
	}

}
