package cc.sferalabs.sfera.http.api.websockets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.EventIdSpecListener;

class WsEventListener extends EventIdSpecListener {

	private static final Logger logger = LogManager.getLogger();

	private final ApiSocket socket;

	/**
	 * 
	 * @param socket
	 * @param eventIdSpec
	 */
	WsEventListener(ApiSocket socket, String eventIdSpec) {
		super(eventIdSpec);
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
		List<Event> evs = new ArrayList<>();
		evs.add(event);
		sendEvents(evs);
	}

	/**
	 * 
	 * @param events
	 */
	private void sendEvents(List<Event> events) {
		try {
			WsMessage m = new WsMessage("event", socket);
			Map<String, Object> eventsMap = new HashMap<>();
			for (Event e : events) {
				eventsMap.put(e.getId(), e.getValue());
			}
			m.put("events", eventsMap);
			logger.debug("Sending events: {}", eventsMap);
			m.send();
		} catch (Exception e) {
			logger.warn("Error sending event", e);
			socket.getSession().close();
		}
	}

}
