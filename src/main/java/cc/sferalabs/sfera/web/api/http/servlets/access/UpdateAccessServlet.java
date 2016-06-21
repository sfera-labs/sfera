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

package cc.sferalabs.sfera.web.api.http.servlets.access;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.UserNotFoundException;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;
import cc.sferalabs.sfera.web.api.http.servlets.ApiServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class UpdateAccessServlet extends ApiServlet {

	public static final String PATH = ApiServlet.PATH + "access/update";

	@Override
	protected void processRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		try {
			String username = getRequiredParameter("username", req, resp);
			String password = req.getParameter("password");
			String roles = req.getParameter("roles");
			String[] rs = (roles == null) ? null : roles.split("\\s*,\\s*");

			if (!req.isUserInRole("admin")) {
				if (!username.equals(req.getRemoteUser()) || roles != null) {
					// non-admins can only update themselves and cannot change
					// their roles
					resp.sendErrors(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.UNAUTHORIZED);
					return;
				}
				String oldPassword = getRequiredParameter("old_password", req, resp);
				if (Access.authenticate(username, oldPassword) == null) {
					// wrong password
					resp.sendErrors(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.UNAUTHORIZED);
					return;
				}
			}

			Access.updateUser(username, password, rs);
			resp.sendResult("ok");

		} catch (MissingRequiredParamException e) {
		} catch (UserNotFoundException e) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, e.getMessage()));
		}
	}

}
