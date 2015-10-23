package cc.sferalabs.sfera.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.TriggerKey;

import cc.sferalabs.sfera.events.Bus;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class TriggerEventJob implements Job {

	@Override
	public void execute(JobExecutionContext context) {
		TriggerKey key = context.getTrigger().getKey();
		Bus.post(new SchedulerEvent(key.getGroup(), key.getName()));
	}
}
