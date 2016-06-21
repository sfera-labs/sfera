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

package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.HttpResponse;

/**
 * Abstract {@link ApiServlet} class extension to be extended by servlets
 * handling requests that need user authorization.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public abstract class AuthorizedApiServlet extends ApiServlet {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizedApiServlet.class);

	/**
	 * @return an array containing the user roles required by this servlet for
	 *         requests to be authorized
	 */
	public abstract String[] getRoles();

	@Override
	protected void processRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		if (isAuthorized(req)) {
			processAuthorizedRequest(req, resp);
		} else {
			logger.warn("Unauthorized API request from {}: {}", req.getRemoteHost(),
					req.getRequestURI());
			resp.sendErrors(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.UNAUTHORIZED);
		}
	}

	/**
	 * Returns a boolean indicating whether the specified request is authorized.
	 * 
	 * @param req
	 *            the request to be authorized
	 * 
	 * @return {@code true} if the request is authorized, {@code false}
	 *         otherwise.
	 */
	protected boolean isAuthorized(HttpServletRequest req) {
		for (String role : getRoles()) {
			if (req.isUserInRole(role)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Processes the authorized API request.
	 * 
	 * @param req
	 *            an {@link HttpServletRequest} object that contains the request
	 *            the client has made of the servlet
	 * @param resp
	 *            an {@link HttpServletResponse} object that contains the
	 *            response the servlet sends to the client
	 * 
	 * @throws ServletException
	 *             if the request could not be handled
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	abstract protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException;

}
