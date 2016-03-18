package cc.sferalabs.sfera.http;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;

/**
 * {@link HttpServletRequestWrapper} for authenticating requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class AuthenticationRequestWrapper extends HttpServletRequestWrapper {

	private static final String SESSION_ATTR_USERNAME = "user";

	private User user;

	/**
	 * Construct a AuthenticationRequestWrapper.
	 * 
	 * @param request
	 *            the wrapped {@code HttpServletRequest}
	 */
	public AuthenticationRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getAuthType() {
		return "API";
	}

	/**
	 * @return the user
	 */
	private User getUser() {
		if (user != null) {
			return user;
		}
		HttpSession session = getSession(false);
		if (session == null) {
			return null;
		}
		String username = (String) session.getAttribute(SESSION_ATTR_USERNAME);
		if (username == null) {
			return null;
		}
		user = Access.getUser(username);
		return user;
	}

	@Override
	public void login(String username, String password) throws ServletException {
		user = Access.authenticate(username, password);
		if (user == null) {
			throw new ServletException("Authentication failed for username '" + username + "'");
		}
		HttpSession session = getSession(true);
		session.setAttribute(SESSION_ATTR_USERNAME, username);
	}

	@Override
	public void logout() throws ServletException {
		user = null;
		HttpSession session = getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@Override
	public boolean isUserInRole(String role) {
		User user = getUser();
		if (user == null) {
			return false;
		}
		return user.isInRole(role);
	}

	@Override
	public Principal getUserPrincipal() {
		User user = getUser();
		if (user == null) {
			return null;
		}

		return new Principal() {

			@Override
			public String getName() {
				return user.getUsername();
			}
		};
	}

	@Override
	public String getRemoteUser() {
		User user = getUser();
		return user == null ? null : user.getUsername();
	}
}
