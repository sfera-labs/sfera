package cc.sferalabs.sfera.http.api.websockets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.http.HttpConnectionEventIdSpecListener;

/**
 * Class handling the delivery of events for WebSocket connections that have
 * subscribed.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class WsEventListener extends HttpConnectionEventIdSpecListener {

	private final ApiSocket socket;

	/**
	 * 
	 * @param socket
	 * @param eventIdSpec
	 * @param connectionId
	 */
	WsEventListener(ApiSocket socket, String eventIdSpec, String connectionId) {
		super(eventIdSpec, connectionId);
		this.socket = socket;

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
