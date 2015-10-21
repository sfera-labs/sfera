package cc.sferalabs.sfera.events;

import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

import com.google.common.eventbus.Subscribe;

public abstract class EventIdSpecListener implements EventListener {

	private final String spec;

	/**
	 * 
	 * @param spec
	 */
	public EventIdSpecListener(String spec) {
		Bus.register(this);
		this.spec = spec;
	}

	@Subscribe
	public void process(Event event) {
		if (matches(event)) {
			handleEvent(event);
		}
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	protected boolean matches(Event event) {
		String eventId = event.getId();
		if (spec == null || spec.equals("*")) {
			return true;
		}

		// TODO implement spec matching

		String[] ns = spec.split(",");
		List<String> nodes = Arrays.asList(ns);
		return nodes.contains(eventId);
	}

	/**
	 * 
	 */
	public void destroy() {
		Bus.unregister(this);
	}

	/**
	 * 
	 * @param event
	 */
	protected abstract void handleEvent(Event event);

}
