package cc.sferalabs.sfera.http.api.rest.servlets.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
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
public class ListUsersServlet extends AuthorizedAdminApiServlet {

	public static final String PATH = ApiServlet.PATH + "access/users";

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		Set<User> users = Access.getUsers();
		List<JSONObject> array = new ArrayList<>();
		for (User u : users) {
			JSONObject obj = new JSONObject();
			obj.put("username", u.getUsername());
			obj.put("roles", u.getRoles());
			array.add(obj);
		}

		resp.sendResult(array);
	}

}
