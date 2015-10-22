package cc.sferalabs.sfera.http.api.websockets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.http.SessionFilterEventIdSpecListener;

class WsEventListener extends SessionFilterEventIdSpecListener {

	private static final Logger logger = LoggerFactory.getLogger(WsEventListener.class);

	private final ApiSocket socket;

	/**
	 * 
	 * @param socket
	 * @param eventIdSpec
	 */
	WsEventListener(ApiSocket socket, String eventIdSpec) {
		super(eventIdSpec, socket.getHttpRequest().getSession().getId());
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
			OutgoingMessage m = new OutgoingMessage("event", socket);
			for (Event e : events) {
				eventsMap.put(e.getId(), e.getValue());
			}
			m.put("events", eventsMap);
			m.send();
		} catch (Exception e) {
			logger.warn("Error sending events " + eventsMap, e);
			Session session = socket.getSession();
			if (session != null) {
				session.close();
			}
		}
	}

}
