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

package cc.sferalabs.sfera.web.api.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.web.api.http.servlets.ApiServlet;

/**
 * Implementation of {@link WebSocketServlet} for handling of API requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class ApiWebSocketServlet extends WebSocketServlet {

	public static final String PATH = ApiServlet.PATH + "websocket";

	@Override
	public void configure(WebSocketServletFactory factory) {
		long pingInterval = SystemNode.getConfiguration().get("ws_ping_interval", 10000l);
		long respTimeout = SystemNode.getConfiguration().get("ws_response_timeout", 5000l);
		factory.setCreator(new ApiSocketCreator(pingInterval, respTimeout));
		factory.getPolicy().setIdleTimeout(pingInterval + respTimeout);
	}

}
