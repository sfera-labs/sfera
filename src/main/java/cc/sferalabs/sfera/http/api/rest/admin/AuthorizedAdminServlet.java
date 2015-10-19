package cc.sferalabs.sfera.http.api.rest.admin;

import cc.sferalabs.sfera.http.api.rest.ApiServlet;
import cc.sferalabs.sfera.http.api.rest.AuthorizedApiServlet;

@SuppressWarnings("serial")
public abstract class AuthorizedAdminServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "admin/";

	private static final String[] roles = new String[] { "admin" };

	@Override
	public String[] getRoles() {
		return roles;
	}

}
