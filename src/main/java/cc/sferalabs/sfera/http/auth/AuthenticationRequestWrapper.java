package cc.sferalabs.sfera.http.auth;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.UserIdentity;

import cc.sferalabs.sfera.http.api.LoginServlet;

public class AuthenticationRequestWrapper extends HttpServletRequestWrapper {

	private static HashLoginService loginService;

	/**
	 * 
	 * @param request
	 */
	public AuthenticationRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 
	 * @param loginService
	 */
	public static void setLoginService(HashLoginService loginService) {
		AuthenticationRequestWrapper.loginService = loginService;
	}

	/**
	 * 
	 * @return
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
