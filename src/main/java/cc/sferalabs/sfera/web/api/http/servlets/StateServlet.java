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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.PollingSubscription;

/**
 * <p>
 * API servlet handling state requests.
 * </p>
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class StateServlet extends ConnectionRequiredApiServlet {

	public static final String PATH = ApiServlet.PATH + "state";

	@Override
	protected void processConnectionRequest(HttpServletRequest req, HttpResponse resp,
			Connection connection) throws ServletException, IOException {
		long ack;
		try {
			ack = Long.parseLong(req.getParameter("ack"));
		} catch (NumberFormatException nfe) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, "Parameter 'ack' not provided"));
			return;
		}
		long timeout;
		try {
			timeout = Long.parseLong(req.getParameter("timeout"));
			if (timeout < 0) {
				timeout = 0;
			}
		} catch (NumberFormatException nfe) {
			timeout = 0;
		}

		PollingSubscription subscription = connection.getSubscription();
		if (subscription == null) {
			resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
					new ErrorMessage(0, "Not subscribed"));
			return;
		}

		try {
			Collection<Event> changes = subscription.pollChanges(ack, timeout, TimeUnit.SECONDS);
			Map<String, Object> nodes = new HashMap<>();
			for (Event ev : changes) {
				nodes.put(ev.getId(), ev.getValue());
			}
			resp.sendResult(nodes);
		} catch (InterruptedException e) {
			resp.sendServerError("Interrupted");
		}
	}

}
