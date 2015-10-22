package cc.sferalabs.sfera.http.api.rest;

/**
 * Abstract class to be extended by API servlets requiring users with roles of
 * 'admin' or 'user'.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public abstract class AuthorizedUserServlet extends AuthorizedApiServlet {

	private static final String[] roles = new String[] { "admin", "user" };

	@Override
	public String[] getRoles() {
		return roles;
	}

}
