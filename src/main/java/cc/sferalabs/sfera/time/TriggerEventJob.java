package cc.sferalabs.sfera.time;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

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
		String id = context.getTrigger().getJobDataMap().getString("id");
		String val = context.getTrigger().getJobDataMap().getString("val");
		Bus.post(new SchedulerEvent(id, val));
	}
}
