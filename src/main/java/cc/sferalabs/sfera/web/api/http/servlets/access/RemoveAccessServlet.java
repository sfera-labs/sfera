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
import cc.sferalabs.sfera.web.api.http.servlets.AuthorizedAdminApiServlet;

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

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			String username = getRequiredParameter("username", req, resp);
			Access.removeUser(username);
			resp.sendResult("ok");
		} catch (MissingRequiredParamException e) {
		} catch (UserNotFoundException e) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, e.getMessage()));
		}
	}

}
