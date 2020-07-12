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

import java.util.Objects;

import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.EventIdSpecListener;

/**
 * Extension of {@link EventIdSpecListener} filtering {@link ConnectionEvent}
 * events.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class ConnectionEventIdSpecListener extends EventIdSpecListener {

	private final String connectionId;

	/**
	 * Construct a HttpConnectionEventIdSpecListener.
	 * 
	 * @param spec
	 *            the event ID specification
	 * @param connectionId
	 *            the connection ID to be used to filter {@link ConnectionEvent}
	 *            events
	 */
	protected ConnectionEventIdSpecListener(String spec, String connectionId) {
		super(spec);
		this.connectionId = Objects.requireNonNull(connectionId, "connectionId must not be null");
	}

	/**
	 * Returns the connection ID
	 * 
	 * @return the connection ID
	 */
	public String getConnectionId() {
		return connectionId;
	}

	@Override
	protected boolean matches(Event event) {
		if (event.isLoacal()) {
			return false;
		}
		if (event instanceof ConnectionEvent) {
			if (connectionId == null) {
				return false;
			}
			if (!connectionId.equals(((ConnectionEvent) event).getConnectionId())) {
				return false;
			}
		}
		return super.matches(event);
	}

}