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

/**
 * 
 */
package cc.sferalabs.sfera.web;

import java.util.EventListener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import cc.sferalabs.sfera.access.AccessChangeEvent;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.ConnectionsSet;
import cc.sferalabs.sfera.web.api.http.servlets.ConnectServlet;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpSessionDestroyer implements HttpSessionListener, EventListener {

	private static final Logger logger = LoggerFactory.getLogger(HttpSessionDestroyer.class);

	private HttpSession session;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.debug("Creted new session: {}", se.getSession().getId());
		this.session = se.getSession();
		Bus.register(this);
	}

	@Subscribe
	public void onAccessChangeEvent(AccessChangeEvent e) {
		if (session != null) {
			String username = (String) session.getAttribute(AuthenticationRequestWrapper.SESSION_ATTR_USERNAME);
			if (username != null && username.equals(e.getValue())) {
				try {
					session.invalidate();
				} catch (Exception ex) {
				}
			}
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		Bus.unregister(this);
		HttpSession session = se.getSession();
		ConnectionsSet connections = (ConnectionsSet) session.getAttribute(ConnectServlet.SESSION_ATTR_CONNECTIONS);
		if (connections != null) {
			for (Connection connection : connections.values()) {
				connection.destroy();
			}
		}
		logger.debug("Session '{}' destroyed", se.getSession().getId());
	}
}
