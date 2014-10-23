package com.homesystemsconsulting.events;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.homesystemsconsulting.core.TasksManager;
import com.homesystemsconsulting.util.logging.SystemLogger;

public abstract class Bus {
	
	private static final SubscriberExceptionHandler SUBSCRIBER_EXCEPTION_HANDLER = new EventsSubscriberExceptionHandler();
	private static final EventBus EVENT_BUS = new AsyncEventBus(TasksManager.DEFAULT.getExecutorService(), SUBSCRIBER_EXCEPTION_HANDLER);
	private static final Map<String, Event> EVENTS_MAP = new HashMap<String, Event>();
	
	/**
	 * 
	 * @param listener
	 */
	public static void register(EventListener listener) {
		EVENT_BUS.register(listener);
	}
	
	/**
	 * 
	 * @param event
	 */
	public static void post(Event event) {
		EVENTS_MAP.put(event.getId(), event);
		EVENT_BUS.post(event);
		String msg = event.getId();
		Object val = event.getValue();
		if (val != null) {
			msg += " = " + event.getValue();
		}
		SystemLogger.SYSTEM.event("events", msg);
	}
	
	/**
	 * 
	 * @param event
	 */
	public static void postIfChanged(Event event) {
		Object currVal = getValueOf(event.getId());
		Object newVal = event.getValue();
		
		if (currVal == null) {
			if (newVal != null) {
				post(event);
			}
		} else if (!currVal.equals(newVal)) {
			post(event);
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Object getValueOf(String id) {
		Event ev = EVENTS_MAP.get(id);
		if (ev == null) {
			return null;
		}
		return ev.getValue();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Map<String, Event> getCurrentState() {
		return new HashMap<>(EVENTS_MAP);
	}
}
