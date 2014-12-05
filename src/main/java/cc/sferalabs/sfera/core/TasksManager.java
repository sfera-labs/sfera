package cc.sferalabs.sfera.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TasksManager {
	
	public static final TasksManager DEFAULT = new TasksManager(Executors.newCachedThreadPool());
	
	private final ExecutorService executorService;
	
	/**
	 * 
	 * @param executorService
	 */
	public TasksManager(ExecutorService executorService) {
		this.executorService = executorService;
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
