package cc.sferalabs.sfera.http.api.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.http.SessionFilterEventIdSpecListener;

/**
 * Class handling a polling subscription requested via {@link SubscribeServletk}
 * .
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class PollingSubscription extends SessionFilterEventIdSpecListener {

	private final String id;
	private final BlockingQueue<Event> changes = new LinkedBlockingQueue<Event>();
	private long lastAckTs;
	private Map<String, Event> lastPolled = new HashMap<String, Event>();

	/**
	 * Constructs a PollingSubscription.
	 * 
	 * @param id
	 *            subscription ID, if {@code null} a random ID will be generated
	 * @param spec
	 *            specification of the event IDs matched by this subscription
	 * @param session
	 *            session ID
	 */
	PollingSubscription(String id, String spec, String session) {
		super(spec, session);
		this.id = id != null ? id : UUID.randomUUID().toString();
		for (Event e : Bus.getCurrentState().values()) {
			process(e);
		}
	}

	/**
	 * Returns the subscription ID
	 * 
	 * @return the subscription ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param ackTs
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized Collection<Event> pollChanges(long ackTs, long timeout, TimeUnit unit)
			throws InterruptedException {
		Map<String, Event> map;
		if (ackTs > lastAckTs) {
			map = new HashMap<String, Event>();
		} else {
			map = lastPolled;
			if (map.size() > 0) {
				timeout = 0;
			}
		}
		Event e = changes.poll(timeout, unit);
		while (e != null) {
			map.put(e.getId(), e);
			e = changes.poll();
		}

		lastAckTs = ackTs;
		lastPolled = map;

		return map.values();
	}

	@Override
	protected void handleEvent(Event event) {
		changes.add(event);
	}

}
