package cc.sferalabs.sfera.scheduler;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Set;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.AutoStartService;

/* TODO remove
 import static org.quartz.JobBuilder.*;
 import static org.quartz.TriggerBuilder.*;
 import static org.quartz.SimpleScheduleBuilder.*;
 import static org.quartz.CronScheduleBuilder.*;
 import static org.quartz.CalendarIntervalScheduleBuilder.*;
 import static org.quartz.JobKey.*;
 import static org.quartz.TriggerKey.*;
 import static org.quartz.DateBuilder.*;
 import static org.quartz.impl.matchers.KeyMatcher.*;
 import static org.quartz.impl.matchers.GroupMatcher.*;
 import static org.quartz.impl.matchers.AndMatcher.*;
 import static org.quartz.impl.matchers.OrMatcher.*;
 import static org.quartz.impl.matchers.EverythingMatcher.*;
 */

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class EventsScheduler implements AutoStartService {

	private static final Logger logger = LoggerFactory.getLogger(EventsScheduler.class);

	private static Scheduler scheduler;

	@Override
	public void init() throws Exception {
		SchedulerFactory schedFact = new StdSchedulerFactory();
		scheduler = schedFact.getScheduler();
		scheduler.start();
	}

	@Override
	public void quit() throws Exception {
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}

	/**
	 * 
	 * @param trigger
	 */
	private static void sheduleEvent(Trigger trigger) {
		String name = trigger.getKey().getName();
		String group = trigger.getKey().getGroup();
		try {
			JobDetail eventJob = newJob(TriggerEventJob.class).withIdentity(name, group).build();
			scheduler.scheduleJob(eventJob, trigger);
		} catch (SchedulerException e) {
			logger.error("Error scheduling job: " + name + " = " + group, e);
		}
	}

	/**
	 * 
	 * @param id
	 * @param value
	 * @return
	 */
	private static TriggerBuilder<Trigger> newEventTrigger(String id, String value) {
		return newTrigger().withIdentity(value, id);
	}

	/**
	 * 
	 * @param id
	 *            ID of the event to cancel
	 * @param value
	 *            value of the event to cancel
	 */
	public static void cancel(String id, String value) {
		try {
			scheduler.unscheduleJob(new TriggerKey(value, id));
		} catch (SchedulerException e) {
			logger.error("Error unscheduling job: " + id + " = " + value, e);
		}
	}

	/**
	 * 
	 * @param id
	 *            ID of the event to cancel
	 */
	public static void cancel(String id) {
		try {
			Set<TriggerKey> keys = scheduler.getTriggerKeys(GroupMatcher.groupEquals(id));
			scheduler.unscheduleJobs(new ArrayList<TriggerKey>(keys));
		} catch (SchedulerException e) {
			logger.error("Error unscheduling jobs: " + id, e);
		}
	}

	/**
	 * 
	 * @param id
	 *            ID of the event to trigger
	 * @param value
	 *            value of the event to trigger
	 * @param delay
	 *            delay in seconds after which the event will be triggered
	 */
	public static void delay(String id, String value, int delay) {
		Trigger trigger = newEventTrigger(id, value).startAt(futureDate(delay, IntervalUnit.SECOND))
				.build();
		sheduleEvent(trigger);
	}

	/**
	 * 
	 * @param id
	 *            ID of the event to trigger
	 * @param value
	 *            value of the event to trigger
	 * @param initialDelay
	 *            delay in seconds after which the first event will be triggered
	 * @param interval
	 *            interval in seconds of the subsequent events
	 */
	public static void repeat(String id, String value, int initialDelay, int interval) {
		Trigger trigger = newEventTrigger(id, value)
				.startAt(futureDate(initialDelay, IntervalUnit.SECOND))
				.withSchedule(simpleSchedule().withIntervalInSeconds(interval).repeatForever())
				.build();
		sheduleEvent(trigger);
	}

	/**
	 * 
	 * @param id
	 *            ID of the event to trigger
	 * @param value
	 *            value of the event to trigger
	 * @param initialDelay
	 *            delay in seconds after which the first event will be triggered
	 * @param interval
	 *            interval in seconds of the subsequent events
	 * @param times
	 *            number of triggered events
	 */
	public static void repeat(String id, String value, int initialDelay, int interval, int times) {
		Trigger trigger = newEventTrigger(id, value)
				.startAt(futureDate(initialDelay, IntervalUnit.SECOND))
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(interval).withRepeatCount(times - 1))
				.build();
		sheduleEvent(trigger);
	}

}
