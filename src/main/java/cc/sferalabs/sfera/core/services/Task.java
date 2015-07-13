package cc.sferalabs.sfera.core.services;

public abstract class Task implements Runnable {

	private final String name;

	/**
	 * 
	 * @param name
	 */
	public Task(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param name
	 * @param task
	 * @return
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
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	@Override
	public void run() {
		try {
			Thread.currentThread().setName(name);
			execute();
		} finally {
			Thread.currentThread().setName("TERMINATED-" + Thread.currentThread().getName());
		}
	}

	/**
	 * 
	 */
	protected abstract void execute();

}
