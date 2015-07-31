package cc.sferalabs.sfera.core.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TasksManager {

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

	/**
	 * 
	 * @param name
	 * @param task
	 */
	public static void execute(String name, Runnable task) {
		execute(Task.create(name, task));
	}
	
	/**
	 * 
	 * @param t
	 */
	public static void execute(Task t) {
		EXECUTOR_SERVICE.execute(t);
	}
	
	/**
	 * 
	 * @param name
	 * @param task
	 * @return
	 */
	public static Future<?> submit(String name, Runnable task) {
		return submit(Task.create(name, task));
	}
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	public static Future<?> submit(Task t) {
		return EXECUTOR_SERVICE.submit(t);
	}
	
	/**
	 * 
	 * @param name
	 * @param task
	 */
	public static Thread executeSystem(String name, Runnable task) {
		return executeSystem(Task.create(name, task));
	}
	
	/**
	 * 
	 * @param t
	 */
	public static Thread executeSystem(Task t) {
		Thread thread = new Thread(t);
		thread.start();
		return thread;
	}

	/**
	 * 
	 * @return
	 */
	public static ExecutorService getExecutorService() {
		return EXECUTOR_SERVICE;
	}

}
