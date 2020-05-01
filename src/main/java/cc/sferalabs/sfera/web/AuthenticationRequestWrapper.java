/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.web;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;

/**
 * {@link HttpServletRequestWrapper} adding authentication information.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class AuthenticationRequestWrapper extends HttpServletRequestWrapper {

	static final String SESSION_ATTR_USERNAME = "user";

	private String savedUsername;

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
		String username = getRemoteUser();
		if (username == null) {
			return null;
		}

		return Access.getUser(username);
	}

	@Override
	public void login(String username, String password) throws ServletException {
		savedUsername = null;
		User user = Access.authenticate(username, password);
		if (user == null) {
			throw new ServletException("Authentication failed for username '" + username + "'");
		}
		savedUsername = username;
		HttpSession session = getSession(true);
		session.setAttribute(SESSION_ATTR_USERNAME, username);
	}

	@Override
	public void logout() throws ServletException {
		savedUsername = null;
		HttpSession session = getSession(false);
		if (session != null) {
			session.removeAttribute(SESSION_ATTR_USERNAME);
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
		String username = getRemoteUser();
		if (username == null) {
			return null;
		}

		return new Principal() {

			@Override
			public String getName() {
				return username;
			}
		};
	}

	@Override
	public String getRemoteUser() {
		if (savedUsername == null) {
			HttpSession session = getSession(false);
			if (session == null) {
				return null;
			}
			savedUsername = (String) session.getAttribute(SESSION_ATTR_USERNAME);
		}
		return savedUsername;
	}

}
