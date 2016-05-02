package cc.sferalabs.sfera.time;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
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
import cc.sferalabs.sfera.events.Node;

/**
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Scheduler extends Node implements AutoStartService {

	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

	private static final AtomicLong counter = new AtomicLong(0);
	private static org.quartz.Scheduler quartzScheduler;
	private static Scheduler instance;

	/**
	 * 
	 */
	public Scheduler() {
		super("scheduler");
		instance = this;
	}

	/**
	 * @return the Scheduler instance
	 */
	public static Scheduler getInstance() {
		return instance;
	}

	@Override
	public void init() throws Exception {
		// The quartz scheduler is only initialized when the first job is
		// scheduled (see getScheduler()).
		// This is an AutoStartService just because we need to add the
		// "scheduler" node, which is done in the constructor.
	}

	@Override
	public void quit() throws Exception {
		destroy();
		if (quartzScheduler != null) {
			quartzScheduler.shutdown();
		}
		instance = null;
	}

	/**
	 * 
	 * @return
	 */
	private static org.quartz.Scheduler getScheduler() {
		if (quartzScheduler != null) {
			return quartzScheduler;
		}

		synchronized (instance) {
			if (quartzScheduler == null) {
				try {
					SchedulerFactory schedFact = new StdSchedulerFactory();
					quartzScheduler = schedFact.getScheduler();
					quartzScheduler.start();
				} catch (SchedulerException e) {
					logger.error("Error instantiating Scheduler", e);
				}
			}
			return quartzScheduler;
		}

	}

	/**
	 * 
	 * @param trigger
	 * @throws SchedulerException
	 */
	private static void scheduleEvent(Trigger trigger) throws SchedulerException {
		JobDetail eventJob = newJob(TriggerEventJob.class).build();
		getScheduler().scheduleJob(eventJob, trigger);
	}

	/**
	 * 
	 * @param id
	 * @param value
	 * @return
	 */
	private static TriggerBuilder<Trigger> newEventTrigger(String id, String value) {
		return newTrigger().withIdentity(Long.toString(counter.getAndIncrement()), id)
				.usingJobData("id", id).usingJobData("val", value);
	}

	/**
	 * 
	 * @param id
	 *            ID of the event(s) to cancel
	 */
	public void cancel(String id) {
		try {
			org.quartz.Scheduler sched = getScheduler();
			Set<TriggerKey> keys = sched.getTriggerKeys(GroupMatcher.groupEquals(id));
			sched.unscheduleJobs(new ArrayList<TriggerKey>(keys));
		} catch (SchedulerException e) {
			logger.error("Error canceling event jobs: " + id, e);
		}
	}

	/**
	 * 
	 * @param id
	 *            ID of the event to trigger
	 * @param value
	 *            value of the event to trigger
	 * @param delay
	 *            delay in milliseconds after which the event will be triggered
	 * @throws SchedulerException
	 *             if an error occurs
	 */
	public void delay(String id, String value, int delay) throws SchedulerException {
		Trigger trigger = newEventTrigger(id, value)
				.startAt(futureDate(delay, IntervalUnit.MILLISECOND)).build();
		scheduleEvent(trigger);
	}

	/**
	 * 
	 * @param id
	 *            ID of the event to trigger
	 * @param value
	 *            value of the event to trigger
	 * @param initialDelay
	 *            delay in milliseconds after which the first event will be
	 *            triggered
	 * @param interval
	 *            interval in milliseconds of the subsequent events
	 * @throws SchedulerException
	 *             if an error occurs
	 */
	public void repeat(String id, String value, int initialDelay, int interval)
			throws SchedulerException {
		Trigger trigger = newEventTrigger(id, value)
				.startAt(futureDate(initialDelay, IntervalUnit.MILLISECOND))
				.withSchedule(simpleSchedule().withIntervalInMilliseconds(interval).repeatForever())
				.build();
		scheduleEvent(trigger);
	}

	/**
	 * 
	 * @param id
	 *            ID of the event to trigger
	 * @param value
	 *            value of the event to trigger
	 * @param initialDelay
	 *            delay in milliseconds after which the first event will be
	 *            triggered
	 * @param interval
	 *            interval in milliseconds of the subsequent events
	 * @param times
	 *            number of triggered events
	 * @throws SchedulerException
	 *             if an error occurs
	 */
	public void repeat(String id, String value, int initialDelay, int interval, int times)
			throws SchedulerException {
		Trigger trigger = newEventTrigger(id, value)
				.startAt(futureDate(initialDelay, IntervalUnit.MILLISECOND))
				.withSchedule(simpleSchedule().withIntervalInMilliseconds(interval)
						.withRepeatCount(times - 1))
				.build();
		scheduleEvent(trigger);
	}

	/**
	 * @param cronExpression
	 *            cron expression
	 * @param id
	 *            ID of the event to trigger
	 * @param value
	 *            value of the event to trigger
	 * @throws SchedulerException
	 *             if an error occurs
	 */
	public void addCronRule(String cronExpression, String id, String value)
			throws SchedulerException {
		Trigger trigger = newEventTrigger(id, value).withSchedule(cronSchedule(cronExpression))
				.build();
		scheduleEvent(trigger);
	}

}
