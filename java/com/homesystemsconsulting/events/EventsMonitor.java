package com.homesystemsconsulting.events;

import java.util.EventListener;
import java.util.HashMap;

import com.google.common.eventbus.Subscribe;
import com.homesystemsconsulting.script.EventsScriptEngine;
import com.homesystemsconsulting.util.logging.SystemLogger;

public class EventsMonitor implements EventListener {

	public static final EventsMonitor INSTANCE = new EventsMonitor();
	
	private static final HashMap<String, Event> eventsMap = new HashMap<String, Event>();
	
	/**
	 * Private constructor for singleton instance
	 */
	private EventsMonitor() {}
	
	@Subscribe
    public void dispatchEvent(Event event) {
		String msg = event.getId();
		Object val = event.getValue();
		if (val != null) {
			msg += " = " + event.getValue();
		}
		SystemLogger.SYSTEM.event("events", msg);
		eventsMap.put(event.getId(), event);
		EventsScriptEngine.executeActionsTriggeredBy(event);
	}

	/**
	 * 
	 * @param eventId
	 * @return
	 */
	public static Object getEventValue(String eventId) {
		Event ev = eventsMap.get(eventId);
		if (ev == null) {
			return null;
		}
		return ev.getValue();
	}

}
