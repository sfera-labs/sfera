package cc.sferalabs.sfera.http.api.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.http.HttpConnectionEventIdSpecListener;
import cc.sferalabs.sfera.http.api.rest.servlets.SubscribeServlet;

/**
 * Class handling a polling subscription requested via {@link SubscribeServlet}.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class PollingSubscription extends HttpConnectionEventIdSpecListener {

	private final BlockingQueue<Event> changes = new LinkedBlockingQueue<Event>();
	private long lastAckTs;
	private Map<String, Event> lastPolled = new HashMap<String, Event>();

	/**
	 * Constructs a PollingSubscription.
	 * 
	 * @param spec
	 *            specification of the event IDs matched by this subscription
	 * 
	 * @param connectionId
	 *            the connection ID
	 */
	PollingSubscription(String spec, String connectionId) {
		super(spec, connectionId);
		for (Event e : Bus.getCurrentState().values()) {
			process(e);
		}
	}

	/**
	 * <p>
	 * Returns a collection of all the events not acknowledged. An event is
	 * considered acknowledged if after being returned by this method a
	 * subsequent call is made with an {@code ack} value greater than the
	 * previous one.
	 * </p>
	 * <p>
	 * The collection will be empty if the timeout expires before any new event
	 * is collected.
	 * </p>
	 * 
	 * @param ack
	 *            the acknowledgement value
	 * @param timeout
	 *            how long to wait before giving up, in units of unit
	 * @param unit
	 *            a {@code TimeUnit} determining how to interpret the timeout
	 *            parameter
	 * @return a collection of all the events not acknowledged
	 * @throws InterruptedException
	 *             if interrupted while waiting
	 */
	public synchronized Collection<Event> pollChanges(long ack, long timeout, TimeUnit unit)
			throws InterruptedException {
		Map<String, Event> map;
		if (ack > lastAckTs) {
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

		lastAckTs = ack;
		lastPolled = map;

		return map.values();
	}

	@Override
	protected void handleEvent(Event event) {
		changes.add(event);
	}

}
