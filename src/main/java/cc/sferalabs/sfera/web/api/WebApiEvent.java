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

package cc.sferalabs.sfera.web.api;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.events.StringEvent;

/**
 * Event generated when requested via remote API.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class WebApiEvent extends StringEvent {

	private final HttpServletRequest request;
	private final String connectionId;

	/**
	 * Construct a RemoteEvent
	 * 
	 * @param id
	 *            the event id
	 * @param value
	 *            the event value
	 * @param request
	 *            the request associated with this event
	 * @param connectionId
	 *            the connection ID associated with this event
	 * @throws NullPointerException
	 *             if any of the parameters are {@code null}
	 */
	public WebApiEvent(String id, String value, HttpServletRequest request, String connectionId)
			throws NullPointerException {
		super(WebNode.getInstance(), Objects.requireNonNull(id, "id must not be null"), value);
		this.request = Objects.requireNonNull(request, "request must not be null");
		this.connectionId = connectionId;
	}

	/**
	 * Returns the user associated with this event.
	 * 
	 * @return the user
	 */
	public User getUser() {
		return Access.getUser(request.getRemoteUser());
	}

	/**
	 * Returns the HTTP request associated with this event.
	 * 
	 * @return the request
	 */
	public HttpServletRequest getHttpRequest() {
		return request;
	}

	/**
	 * Returns the connection ID associated with this event.
	 * 
	 * @return the connection ID associated with this event
	 */
	public String getConnectionId() {
		return connectionId;
	}

}
