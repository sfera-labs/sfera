package cc.sferalabs.sfera.time;

import cc.sferalabs.sfera.events.BaseEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class SchedulerEvent extends BaseEvent {

	private final String value;

	/**
	 * @param id
	 * @param value
	 */
	SchedulerEvent(String id, String value) {
		super(Scheduler.getInstance(), id);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
