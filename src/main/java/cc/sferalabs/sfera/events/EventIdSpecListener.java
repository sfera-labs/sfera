package cc.sferalabs.sfera.events;

import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import java.util.Objects;

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

	private final String spec;

	/**
	 * Construct an EventIdSpecListener with the specified specification and
	 * registers itself to the Bus.
	 * 
	 * @param spec
	 *            the event ID specification
	 */
	public EventIdSpecListener(String spec) {
		Bus.register(this);
		this.spec = Objects.requireNonNull(spec, "spec must not be null");
	}

	@Subscribe
	public void process(Event event) {
		if (matches(event)) {
			handleEvent(event);
		}
	}

	/**
	 * Returns a boolean representing whether the specified event matches the
	 * specification.
	 * 
	 * @param event
	 *            the event to match
	 * @return {@code true} if the event matches the specification,
	 *         {@code false} otherwise.
	 */
	protected boolean matches(Event event) {
		String eventId = event.getId();
		if (spec.equals("*")) {
			return true;
		}

		// TODO implement spec matching

		String[] ns = spec.split(",");
		List<String> nodes = Arrays.asList(ns);
		return nodes.contains(eventId);
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
