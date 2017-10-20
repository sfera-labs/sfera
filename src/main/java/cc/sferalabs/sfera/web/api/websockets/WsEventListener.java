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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.web.ConnectionEventIdSpecListener;

/**
 * Class handling the delivery of events for WebSocket connections that have
 * subscribed.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class WsEventListener extends ConnectionEventIdSpecListener {

	private final ApiSocket socket;

	/**
	 * 
	 * @param socket
	 *            the WS socket
	 * @param eventIdSpec
	 *            the event ID specification
	 * @param connectionId
	 *            the connection ID to be used as filter
	 */
	WsEventListener(ApiSocket socket, String eventIdSpec, String connectionId) {
		super(eventIdSpec, connectionId);
		this.socket = socket;
	}

	/**
	 * 
	 */
	void sendCurrentSate() {
		List<Event> evs = new ArrayList<>();
		for (Event e : Bus.getCurrentState().values()) {
			if (matches(e)) {
				evs.add(e);
			}
		}
		if (!evs.isEmpty()) {
			sendEvents(evs);
		}
	}

	@Override
	protected void handleEvent(Event event) {
		sendEvents(Arrays.asList(event));
	}

	/**
	 * 
	 * @param events
	 */
	private void sendEvents(List<Event> events) {
		Map<String, Object> eventsMap = new HashMap<>();
		try {
			OutgoingWsMessage m = new OutgoingWsMessage("event", socket);
			for (Event e : events) {
				eventsMap.put(e.getId(), e.getValue());
			}
			m.send("nodes", eventsMap);
		} catch (Exception e) {
			socket.onWebSocketError(e);
		}
	}

}
