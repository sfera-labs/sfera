package cc.sferalabs.sfera.core.services;

/**
 * The {@code Task} class should be implemented by any class whose instances are
 * intended to be executed by the {@link TasksManager}. A {@code Task} is a
 * {@link Runnable} which sets the name of the {@link Thread} by which it is
 * executed. It is useful for debugging reasons,
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Task implements Runnable {

	private final String name;

	/**
	 * Construct a {@code Task} with the specified name to be assigned to the
	 * {@link Thread} that will execute it.
	 * 
	 * @param name
	 *            the name to assign to the executing thread
	 */
	public Task(String name) {
		this.name = name;
	}

	/**
	 * Factory method to construct a new {@code Task} that will run the
	 * specified {@code Runnable} when executed
	 * 
	 * @param name
	 *            the name to pass to the {@code Task} constructor
	 * @param task
	 *            the {@code Runnable} to be run
	 * @return the constructed task
	 */
	public static Task create(String name, Runnable task) {
		return new Task(name) {

			@Override
			protected void execute() {
				task.run();
			}
		};
	}

	/**
	 * Returns the task name
	 * 
	 * @return the task name
	 */
	public String getName() {
		return name;
	}

	@Override
	public void run() {
		Thread thread = Thread.currentThread();
		try {
			thread.setName(name);
			execute();
		} finally {
			thread.setName("TERMINATED-" + name);
		}
	}

	/**
	 * This method must be implemented by the classes extending {@code Task}. It
	 * is called when the task is executed by a thread instantiated by the
	 * {@link TasksManager}.
	 */
	protected abstract void execute();

}
