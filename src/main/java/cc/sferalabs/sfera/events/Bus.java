package cc.sferalabs.sfera.events;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;

import cc.sferalabs.sfera.core.services.TasksManager;

/**
 * Utility class representing the system events bus.
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Bus {

	private static final SubscriberExceptionHandler SUBSCRIBER_EXCEPTION_HANDLER = new EventsSubscriberExceptionHandler();
	private static final EventBus EVENT_BUS = new AsyncEventBus(
			TasksManager.getTasksExecutorService(), SUBSCRIBER_EXCEPTION_HANDLER);
	private static final Map<String, Event> EVENTS_MAP = new HashMap<String, Event>();

	private static final Logger logger = LoggerFactory.getLogger(Bus.class);

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
	 * Posts the specified event to the bus only if the last event with the same
	 * ID that was posted had a different value or there was no such event. The
	 * comparison of the values is done by means of the {@code equals()} method.
	 * 
	 * @param event
	 *            the event to post
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
	 *             if attempting to cast the returned value to an incompatible
	 *             class
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
	 * Returns a map with all the events ID posted mapped to the last
	 * corresponding event instance posted.
	 * 
	 * @return a map with all the events ID posted mapped to the last
	 *         corresponding event instance posted
	 */
	public static Map<String, Event> getCurrentState() {
		return new HashMap<>(EVENTS_MAP);
	}
}
