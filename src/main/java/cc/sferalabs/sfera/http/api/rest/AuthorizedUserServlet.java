package cc.sferalabs.sfera.http.api.rest;

@SuppressWarnings("serial")
public abstract class AuthorizedUserServlet extends AuthorizedApiServlet {

	private static final String[] roles = new String[] { "admin", "user" };

	@Override
	public String[] getRoles() {
		return roles;
	}

}
