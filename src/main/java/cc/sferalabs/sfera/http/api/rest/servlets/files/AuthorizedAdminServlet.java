package cc.sferalabs.sfera.http.api.rest.servlets.files;

import cc.sferalabs.sfera.http.api.rest.servlets.AuthorizedApiServlet;

/**
 * Abstract class to be extended by API servlets requiring users with 'admin'
 * role.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public abstract class AuthorizedAdminServlet extends AuthorizedApiServlet {

	private static final String[] roles = new String[] { "admin" };

	@Override
	public String[] getRoles() {
		return roles;
	}

}
