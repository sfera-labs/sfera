package cc.sferalabs.sfera.scheduler;

import cc.sferalabs.sfera.core.Task;
import cc.sferalabs.sfera.events.Bus;

public class ScheduledEventTask extends Task {
	
	private final String id;

	public ScheduledEventTask(String id) {
		super("ScheduledEventTask:" + id);
		this.id = id;
	}

	@Override
	public void execute() {
		Bus.post(new SchedulerEvent(id));
	}

}
