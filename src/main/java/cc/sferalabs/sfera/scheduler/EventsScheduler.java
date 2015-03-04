package cc.sferalabs.sfera.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.SferaService;
import cc.sferalabs.sfera.script.ScriptsEngine;

public class EventsScheduler implements SferaService {
	
	//TODO handle scheduling an event with same ID more than once, maybe it makes sense to keep both schedulers... maybe not...

	private static final ScheduledExecutorService SCHEDULER = Executors
			.newScheduledThreadPool(5);
	
	private static final Logger logger = LogManager.getLogger();

	@Override
	public void init() throws Exception {
		ScriptsEngine.putTypeInGlobalScope(this.getClass());
		logger.debug("Events Scheduler initiated");
	}

	@Override
	public void quit() throws Exception {
		SCHEDULER.shutdownNow();
		logger.debug("Events Scheduler quitted");
	}

	/**
	 * 
	 * @param id
	 * @param delay
	 * @param unit
	 * @return
	 */
	public static ScheduledFuture<?> schedule(String id, long delay,
			TimeUnit unit) {
		return SCHEDULER.schedule(new ScheduledEventTask(id), delay, unit);
	}

	/**
	 * 
	 * @param id
	 * @param delay
	 * @return
	 */
	public static ScheduledFuture<?> schedule(String id, long delay) {
		return schedule(id, delay, TimeUnit.SECONDS);
	}

	/**
	 * 
	 * @param id
	 * @param initialDelay
	 * @param delay
	 * @param unit
	 * @return
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(String id,
			long initialDelay, long delay, TimeUnit unit) {
		return SCHEDULER.scheduleWithFixedDelay(new ScheduledEventTask(id),
				initialDelay, delay, unit);
	}

	/**
	 * 
	 * @param id
	 * @param initialDelay
	 * @param delay
	 * @return
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(String id,
			long initialDelay, long delay) {
		return scheduleAtFixedRate(id, initialDelay, delay, TimeUnit.SECONDS);
	}
}
