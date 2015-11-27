package cc.sferalabs.sfera.scheduler;

import cc.sferalabs.sfera.events.BaseEvent;
import cc.sferalabs.sfera.events.Node;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class SchedulerEvent extends BaseEvent {

	/**
	 * Node used by SchedulerEvents
	 */
	private static final Node SCHEDULER_NODE = new SchedulerNode();

	/**
	 *
	 */
	private static class SchedulerNode extends Node {
		public SchedulerNode() {
			super("scheduler");
		}
	}

	private final String value;

	/**
	 * @param id
	 * @param value
	 */
	SchedulerEvent(String id, String value) {
		super(SCHEDULER_NODE, id);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
