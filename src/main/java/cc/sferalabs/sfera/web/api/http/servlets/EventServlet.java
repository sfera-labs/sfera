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

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.WebApiEvent;
import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.HttpResponse;

/**
 * <p>
 * API servlet handling requests for events triggering.
 * </p>
 * <p>
 * The triggered events will be instances of {@link WebApiEvent} and the source
 * node an {@link cc.sferalabs.sfera.web.api.WebNode HttpRemoteNode} instance.
 * </p>
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class EventServlet extends ConnectionRequiredApiServlet {

	public static final String PATH = ApiServlet.PATH + "event";

	private static final Logger logger = LoggerFactory.getLogger(EventServlet.class);

	@Override
	protected void processConnectionRequest(HttpServletRequest req, HttpResponse resp,
			Connection connection) throws ServletException, IOException {
		String id = req.getParameter("id");
		String value = req.getParameter("value");
		WebApiEvent ev;
		try {
			ev = new WebApiEvent(id, value, req, connection.getId());
		} catch (Exception e) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, e.getMessage()));
			return;
		}
		logger.info("Event: {} = {} User: {}", id, value, req.getRemoteUser());
		Bus.post(ev);
		resp.sendResult("ok");
	}

}
