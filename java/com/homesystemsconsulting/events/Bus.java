package com.homesystemsconsulting.events;

import java.util.EventListener;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.homesystemsconsulting.core.TasksManager;

public abstract class Bus {
	
	private static final SubscriberExceptionHandler subscriberExceptionHandler = new EventsSubscriberExceptionHandler();
	private static final EventBus eventBus = new AsyncEventBus(TasksManager.DEFAULT.getExecutorService(), subscriberExceptionHandler);
	
	public static void register(EventListener listener) {
		eventBus.register(listener);
	}
	
	public static void post(Event event) {
		eventBus.post(event);
	}
	
	public static void postIfChanged(Event event) {
		Object currVal = EventsMonitor.getEventValue(event.getId());
		Object newVal = event.getValue();
		
		if (currVal == null) {
			if (newVal != null) {
				post(event);
			}
		} else if (!currVal.equals(newVal)) {
			post(event);
		}
	}
}
