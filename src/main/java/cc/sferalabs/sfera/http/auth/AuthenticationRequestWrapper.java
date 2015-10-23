package cc.sferalabs.sfera.http.auth;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.UserIdentity;

import cc.sferalabs.sfera.http.api.rest.LoginServlet;

/**
 * {@link HttpServletRequestWrapper} for authenticating requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class AuthenticationRequestWrapper extends HttpServletRequestWrapper {

	private static HashLoginService loginService;

	/**
	 * Construct a AuthenticationRequestWrapper.
	 * 
	 * @param request
	 *            the wrapped {@code HttpServletRequest}
	 */
	public AuthenticationRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * Sets the login service to be used.
	 * 
	 * @param loginService
	 *            the login service
	 */
	public static void setLoginService(HashLoginService loginService) {
		AuthenticationRequestWrapper.loginService = loginService;
	}

	/**
	 * @return the user identity
	 */
	private UserIdentity getUser() {
		if (loginService == null) {
			throw new IllegalStateException("login service not set");
		}
		HttpServletRequest request = (HttpServletRequest) getRequest();
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		String userName = (String) session.getAttribute(LoginServlet.SESSION_ATTR_USERNAME);
		if (userName == null) {
			return null;
		}
		return loginService.getUsers().get(userName);
	}

	@Override
	public boolean isUserInRole(String role) {
		UserIdentity user = getUser();
		if (user == null) {
			return false;
		}
		return user.isUserInRole(role, null);
	}

	@Override
	public Principal getUserPrincipal() {
		UserIdentity user = getUser();
		if (user == null) {
			return null;
		}
		return user.getUserPrincipal();
	}

	@Override
	public String getRemoteUser() {
		Principal p = getUserPrincipal();
		return p == null ? null : p.getName();
	}
}
