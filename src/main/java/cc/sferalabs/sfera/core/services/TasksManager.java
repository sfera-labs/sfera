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

package cc.sferalabs.sfera.core.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Utility class to execute {@link Task Tasks} monitored by Sfera life cycle.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class TasksManager {

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

	/**
	 * Creates a {@link Task} object with the specified name and {@code Runnable}
	 * task and executes it.
	 * 
	 * @param name
	 *            the name of the task to create
	 * @param task
	 *            the {@code Runnable} to execute
	 */
	public static void execute(String name, Runnable task) {
		execute(Task.create(name, task));
	}

	/**
	 * Executes the specified task.
	 * 
	 * @param task
	 *            the task to execute
	 */
	public static void execute(Task task) {
		EXECUTOR_SERVICE.execute(task);
	}

	/**
	 * Creates a {@link Task} object with the specified name and {@code Runnable}
	 * task and submits it for execution. Returns a {@code Future} representing that
	 * task.
	 * 
	 * @param name
	 *            the name of the task to create
	 * @param task
	 *            the {@code Runnable} to execute
	 * @return a {@code Future} representing pending completion of the task
	 */
	public static Future<?> submit(String name, Runnable task) {
		return submit(Task.create(name, task));
	}

	/**
	 * Submits the specified task for execution and returns a {@code Future}
	 * representing that task.
	 * 
	 * @param task
	 *            the task to execute
	 * @return a {@code Future} representing pending completion of the task
	 */
	public static Future<?> submit(Task task) {
		return EXECUTOR_SERVICE.submit(task);
	}

	/**
	 * Creates a {@link Task} object with the specified name and {@code Runnable}
	 * task end executes it at system level. Sfera life cycle will interrupt this
	 * task only after all the regular tasks have been interrupted.
	 * 
	 * @param name
	 *            the name of the task to create
	 * @param task
	 *            the {@code Runnable} to execute
	 * @return the {@code Thread} executing the task
	 */
	public static Thread executeSystem(String name, Runnable task) {
		return executeSystem(Task.create(name, task));
	}

	/**
	 * Executes the specified task at system level. Sfera life cycle will not
	 * interrupt this task.
	 * 
	 * @param task
	 *            the task to execute
	 * @return the {@code Thread} executing the task
	 */
	public static Thread executeSystem(Task task) {
		Thread thread = new Thread(task);
		thread.start();
		return thread;
	}

	/**
	 * Returns the {@link ExecutorService} used to execute tasks.
	 * 
	 * @return the {@link ExecutorService} used to execute tasks.
	 */
	public static ExecutorService getTasksExecutorService() {
		return EXECUTOR_SERVICE;
	}

	/**
	 * Attempts to stop all actively executing tasks and halts the processing of
	 * waiting tasks. There are no guarantees beyond best-effort attempts to stop
	 * processing actively executing tasks.
	 */
	public static void shutdownTasksNow() {
		EXECUTOR_SERVICE.shutdownNow();
	}

	/**
	 * Blocks until all tasks have completed execution after a shutdown request, or
	 * the timeout occurs, or the current thread is interrupted, whichever happens
	 * first.
	 * 
	 * @param timeout
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the timeout argument
	 * @return {@code true} if tasks execution terminated and {@code false} if the
	 *         timeout elapsed before termination
	 * @throws InterruptedException
	 *             if interrupted while waiting
	 */
	public static boolean awaitTasksTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return EXECUTOR_SERVICE.awaitTermination(timeout, unit);
	}

	/**
	 * Blocks until the specified future completed, or the timeout occurs, or the
	 * current thread is interrupted, whichever happens first.
	 * 
	 * @param task
	 *            the task to wait for
	 * @param timeoutMillis
	 *            the maximum time to wait (in milliseconds)
	 * @param checkIntervalMillis
	 *            the interval (in milliseconds) between subsequent task completion
	 *            checks
	 * @return {@code true} if task execution terminated and {@code false} if the
	 *         timeout elapsed before termination
	 * @throws InterruptedException
	 *             if interrupted while waiting
	 */
	public static boolean awaitTermination(Future<?> task, long timeoutMillis, long checkIntervalMillis)
			throws InterruptedException {
		long giveUpTime = System.currentTimeMillis() + timeoutMillis;
		while (!task.isDone()) {
			if (System.currentTimeMillis() > giveUpTime) {
				return false;
			}
			Thread.sleep(checkIntervalMillis);
		}
		return true;
	}

}
