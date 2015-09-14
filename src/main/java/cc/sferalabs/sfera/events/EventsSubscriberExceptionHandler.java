package cc.sferalabs.sfera.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class EventsSubscriberExceptionHandler implements SubscriberExceptionHandler {

	@Override
	public void handleException(Throwable exception, SubscriberExceptionContext context) {
		Class<?> listenerClass = context.getSubscriber().getClass();
		String method = context.getSubscriberMethod().getName();
		String event = context.getEvent().getClass().getSimpleName();

		Logger logger = LoggerFactory.getLogger(listenerClass);
		logger.error("Error dispatching event '" + event + "' to '" + listenerClass.getSimpleName()
				+ "." + method + "'", exception);
	}

}
