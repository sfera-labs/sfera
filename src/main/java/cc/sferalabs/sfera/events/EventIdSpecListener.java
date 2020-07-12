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

package cc.sferalabs.sfera.events;

import java.util.EventListener;
import java.util.Objects;
import java.util.function.Predicate;

import com.google.common.eventbus.Subscribe;

/**
 * Abstract class for event listeners wanting to subscribe for events whose IDs
 * match a specific template (specification).
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class EventIdSpecListener implements EventListener {

	private final Predicate<Event> predicate;

	/**
	 * Construct an EventIdSpecListener with the specified specification and
	 * registers itself to the Bus.
	 * 
	 * @param spec
	 *            the event ID specification
	 */
	public EventIdSpecListener(String spec) {
		this.predicate = EventsUtil.getEventIdSpecMatchingPredicate(
				Objects.requireNonNull(spec, "spec must not be null"));
		Bus.register(this);
	}

	@Subscribe
	public void process(Event event) {
		if (matches(event)) {
			handleEvent(event);
		}
	}

	/**
	 * Returns a boolean representing whether the specified event has an ID
	 * matching the specification.
	 * 
	 * @param event
	 *            the event to test
	 * @return {@code true} if the event's ID matches the specification,
	 *         {@code false} otherwise.
	 */
	protected boolean matches(Event event) {
		if (predicate == null) {
			return false;
		}
		return predicate.test(event);
	}

	/**
	 * Clears the resources used by this listener.
	 */
	public void destroy() {
		Bus.unregister(this);
	}

	/**
	 * Handle the events matching the specification
	 * 
	 * @param event
	 *            the event to handle
	 */
	protected abstract void handleEvent(Event event);

}
