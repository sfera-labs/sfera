package cc.sferalabs.sfera.scheduler;

import cc.sferalabs.sfera.events.BaseEvent;
import cc.sferalabs.sfera.events.Node;

public class SchedulerEvent extends BaseEvent {

	/**
	 * Node used by SchedulerEvents
	 */
	private static final Node SCHEDULER_NODE = new Node() {

		@Override
		public String getId() {
			return "scheduler";
		}
	};

	private final String value;

	/**
	 * 
	 * @param id
	 */
	public SchedulerEvent(String id, String value) {
		super(SCHEDULER_NODE, id);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
