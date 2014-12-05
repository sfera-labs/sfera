package cc.sferalabs.sfera.events;

import cc.sferalabs.sfera.util.logging.SystemLogger;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class EventsSubscriberExceptionHandler implements
		SubscriberExceptionHandler {

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		String listener = context.getSubscriber().getClass().getSimpleName();
		String method = context.getSubscriberMethod().getName();
		String event = context.getEvent().getClass().getSimpleName();
		SystemLogger.SYSTEM.error("events", "Uncought exception dispatching event '" + event + "' to '" + listener + "." + method + "': " + exception);
	}

}
