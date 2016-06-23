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
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.ConnectionsSet;
import cc.sferalabs.sfera.web.api.http.HttpResponse;

/**
 * API servlet handlig connection requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class ConnectServlet extends AuthenticatedUserServlet {

	public static final String PATH = ApiServlet.PATH + "connect";

	public static final String SESSION_ATTR_CONNECTIONS = "connections";
	private static final Logger logger = LoggerFactory.getLogger(ConnectServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		String sessionId = session.getId();
		ConnectionsSet connections = (ConnectionsSet) session
				.getAttribute(SESSION_ATTR_CONNECTIONS);
		if (connections == null) {
			connections = new ConnectionsSet();
			session.setAttribute(SESSION_ATTR_CONNECTIONS, connections);
			logger.debug("Created new connections set for session '{}'", sessionId);
		}
		Connection connection = new Connection();
		connections.put(connection);
		String cid = connection.getId();
		logger.debug("Connected - session '{}' connection '{}'", sessionId, cid);
		resp.send("cid", cid);
	}

}
