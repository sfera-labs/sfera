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
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.scripts.ScriptsEngine;

/**
 * Utility class representing the system events bus.
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Bus {

	private static final Logger logger = LoggerFactory.getLogger(Bus.class);

	private static final SubscriberExceptionHandler SUBSCRIBER_EXCEPTION_HANDLER = new SubscriberExceptionHandler() {

		@Override
		public void handleException(Throwable exception, SubscriberExceptionContext context) {
			Class<?> listenerClass = context.getSubscriber().getClass();
			String method = context.getSubscriberMethod().getName();
			String event = context.getEvent().getClass().getSimpleName();

			Logger logger = LoggerFactory.getLogger(listenerClass);
			logger.error(
					"Error dispatching event '" + event + "' to '" + listenerClass.getSimpleName() + "." + method + "'",
					exception);
		}
	};

	private static final EventBus EVENT_BUS = new AsyncEventBus(TasksManager.getTasksExecutorService(),
			SUBSCRIBER_EXCEPTION_HANDLER);
	private static final Map<String, Event> EVENTS_MAP = new HashMap<String, Event>();

	static {
		try {
			ScriptsEngine.putTypeInGlobalScope(Bus.class);
		} catch (ScriptException e) {
			logger.error("Error adding Bus to script engine global scope", e);
		}
	}

	/**
	 * Registers the specified listener to the event bus.
	 * 
	 * @param listener
	 *            the listener to register
	 */
	public static void register(EventListener listener) {
		EVENT_BUS.register(listener);
	}

	/**
	 * Unregisters the specified listener to the event bus.
	 * 
	 * @param listener
	 *            the listener to unregister
	 */
	public static void unregister(EventListener listener) {
		EVENT_BUS.unregister(listener);
	}

	/**
	 * Posts the specified event to the bus.
	 * 
	 * @param event
	 *            the event to post
	 */
	public static void post(Event event) {
		EVENTS_MAP.put(event.getId(), event);
		EVENT_BUS.post(event);
		logger.info("Event: {} = {}", event.getId(), event.getValue());
	}

	/**
	 * Posts the specified event to the bus only if the last event with the same ID
	 * that was posted had a different value or there was no such event. The
	 * comparison of the values is done by means of the {@code equals()} method.
	 * 
	 * @param event
	 *            the event to post
	 */
	public static void postIfChanged(Event event) {
		if (valueChanged(event)) {
			post(event);
		}
	}

	/**
	 * Checks whether the value of the specified event corresponds to the last
	 * posted one.
	 * 
	 * @param event
	 *            the event to check
	 * 
	 * @return {@code true} if the value of the specified events is different from
	 *         the last posted event with same ID or if there was no such previous
	 *         event; {@code false} otherwise.
	 */
	public static boolean valueChanged(Event event) {
		Object currVal = getValueOf(event.getId());
		Object newVal = event.getValue();

		if (currVal == null) {
			if (newVal != null) {
				return true;
			}
		} else if (!currVal.equals(newVal)) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the value of the last event that was posted on the bus with the
	 * specified ID.
	 * 
	 * @param <T>
	 *            the class to cast the returned value to
	 * @param id
	 *            the event ID
	 * @return the value of the last event that was posted on the bus with the
	 *         specified ID
	 * 
	 * @throws ClassCastException
	 *             if attempting to cast the returned value to an incompatible class
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueOf(String id) throws ClassCastException {
		Event ev = EVENTS_MAP.get(id);
		if (ev == null) {
			return null;
		}
		return (T) ev.getValue();
	}

	/**
	 * Returns the last event that was posted on the bus with the specified ID.
	 * 
	 * @param id
	 *            the event ID
	 * @return the last event that was posted on the bus with the specified ID
	 */
	public static Event getEvent(String id) {
		return EVENTS_MAP.get(id);
	}

	/**
	 * Returns a map with all the events ID posted mapped to the last corresponding
	 * event instance posted.
	 * 
	 * @return a map with all the events ID posted mapped to the last corresponding
	 *         event instance posted
	 */
	public static Map<String, Event> getCurrentState() {
		return new HashMap<>(EVENTS_MAP);
	}
}
