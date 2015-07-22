package cc.sferalabs.sfera.events;

import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

import com.google.common.eventbus.Subscribe;

public abstract class NodesSpecSubscriber implements EventListener {

	private String spec;

	/**
	 * 
	 */
	public NodesSpecSubscriber() {
		Bus.register(this);
	}

	/**
	 * 
	 * @param spec
	 */
	public void setNodesSpec(String spec) {
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
		return matches(event.getId());
	}

	/**
	 * 
	 * @param eventId
	 * @return
	 */
	protected boolean matches(String eventId) {
		if (spec == null) {
			return false;
		}

		// TODO implement spec matching

		if (spec.equals("*")) {
			return true;
		}

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
