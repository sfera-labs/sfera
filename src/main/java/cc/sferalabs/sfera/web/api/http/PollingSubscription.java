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

package cc.sferalabs.sfera.web.api.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.web.ConnectionEventIdSpecListener;
import cc.sferalabs.sfera.web.api.http.servlets.SubscribeServlet;

/**
 * Class handling a polling subscription requested via {@link SubscribeServlet}.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class PollingSubscription extends ConnectionEventIdSpecListener {

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
