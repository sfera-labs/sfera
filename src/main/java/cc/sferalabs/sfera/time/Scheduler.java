/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

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
		return newTrigger().withIdentity(Long.toString(counter.getAndIncrement()), id).usingJobData("id", id)
				.usingJobData("val", value);
	}

	/**
	 * Schedules an event to be triggered with the specified delay after this
	 * method has been called.
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
		Trigger trigger = newEventTrigger(id, value).startAt(futureDate(delay, IntervalUnit.MILLISECOND)).build();
		scheduleEvent(trigger);
	}

	/**
	 * Schedules a set of events to be triggered with an initial delay from this
	 * method call and a regular interval between events. Events will continue
	 * until explicitly {@link #cancel(String) cancelled}.
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
	public void repeat(String id, String value, int initialDelay, int interval) throws SchedulerException {
		Trigger trigger = newEventTrigger(id, value).startAt(futureDate(initialDelay, IntervalUnit.MILLISECOND))
				.withSchedule(simpleSchedule().withIntervalInMilliseconds(interval).repeatForever()).build();
		scheduleEvent(trigger);
	}

	/**
	 * Schedules a fixed number of events to be triggered with an initial delay
	 * from this method call and a regular interval between events. Events will
	 * continue until the specified number of events have been triggered or
	 * explicitly {@link #cancel(String) cancelled}.
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
	public void repeat(String id, String value, int initialDelay, int interval, int times) throws SchedulerException {
		Trigger trigger = newEventTrigger(id, value).startAt(futureDate(initialDelay, IntervalUnit.MILLISECOND))
				.withSchedule(simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(times - 1)).build();
		scheduleEvent(trigger);
	}

	/**
	 * Schedules events to be triggered following a cron expression.
	 * <p>
	 * The {@code cronExpression} String shall have the following format: <br>
	 * {@code 
	 * "<seconds> <minutes> <hours> <day-of-month> <month> <day-of-week> <year>"
	 * }
	 * <p>
	 * The fields are separated by spaces, the {@code <year>} field is optional.
	 * <p>
	 * Examples:<br>
	 * 
	 * Every 5 minutes: {@code "0 0/5 * * * ?"}<br>
	 * Every 5 minutes, at 10 seconds after the minute (e.g. 10:00:10 am,
	 * 10:05:10 am, etc.): {@code "10 0/5 * * * ?"}<br>
	 * At 10:30, 11:30, 12:30, and 13:30, on every Wednesday and Friday:
	 * {@code "0 30 10-13 ? * WED,FRI"}<br>
	 * Every half hour between the hours of 8 am and 10 am on the 5th and 20th
	 * of every month: {@code "0 0/30 8-9 5,20 * ?"}<br>
	 * 
	 * @param cronExpression
	 *            cron expression
	 * @param id
	 *            ID of the event to trigger
	 * @param value
	 *            value of the event to trigger
	 * @throws SchedulerException
	 *             if an error occurs
	 * @see <a href=
	 *      "http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html">http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html</a>
	 */
	public void addCronRule(String cronExpression, String id, String value) throws SchedulerException {
		Trigger trigger = newEventTrigger(id, value).withSchedule(cronSchedule(cronExpression)).build();
		scheduleEvent(trigger);
	}

	/**
	 * Cancel the previously scheduled events with the specified ID.
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

}
