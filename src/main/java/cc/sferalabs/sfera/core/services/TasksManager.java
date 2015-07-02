package cc.sferalabs.sfera.core.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TasksManager {

	private static final TasksManager DEFAULT = new TasksManager(
			Executors.newCachedThreadPool());

	private final ExecutorService executorService;

	/**
	 * 
	 * @param executorService
	 */
	private TasksManager(ExecutorService executorService) {
		this.executorService = executorService;
	}

	/**
	 * 
	 * @return
	 */
	public static TasksManager getDefault() {
		return DEFAULT;
	}

	/**
	 * 
	 * @return
	 */
	public static TasksManager newTasksManager(ExecutorService executorService) {
		return new TasksManager(executorService);
	}

	/**
	 * 
	 * @param name
	 * @param task
	 * @return
	 */
	public Future<?> submit(String name, Runnable task) {
		return submit(Task.create(name, task));
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	public Future<?> submit(Task t) {
		return executorService.submit(t);
	}

	/**
	 * 
	 * @param name
	 * @param task
	 */
	public void execute(String name, Runnable task) {
		execute(Task.create(name, task));
	}

	/**
	 * 
	 * @param t
	 */
	public void execute(Task t) {
		executorService.execute(t);
	}

	/**
	 * 
	 * @return
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}
}
