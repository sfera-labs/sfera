package cc.sferalabs.sfera.drivers;

import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.Task;
import cc.sferalabs.sfera.core.TasksManager;
import cc.sferalabs.sfera.events.Node;

public abstract class Driver extends Task implements Node {

	private final String id;
	private boolean quit = false;
	private Future<?> future;

	protected final Logger logger;

	/**
	 * 
	 * @param id
	 */
	public Driver(String id) {
		super("driver." + id);
		this.id = id;
		this.logger = LogManager.getLogger(getClass().getName() + "." + id);
	}

	/**
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param configuration
	 * @return
	 * @throws InterruptedException
	 */
	protected abstract boolean onInit(Configuration configuration)
			throws InterruptedException;

	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	protected abstract boolean loop() throws InterruptedException;

	/**
	 * 
	 * @throws InterruptedException
	 */
	protected abstract void onQuit() throws InterruptedException;

	/**
	 * 
	 */
	public synchronized void quit() {
		this.quit = true;
		if (future != null) {
			future.cancel(true);
		}
	}

	/**
	 * 
	 */
	public synchronized void start() {
		if (future == null) {
			quit = false;
			future = TasksManager.DEFAULT.submit(this);
		}
	}

	@Override
	protected void execute() {
		while (!quit) {
			try {
				logger.info("Starting...");
				if (onInit(new Configuration(getId()))) {
					logger.info("Started");
				} else {
					logger.warn("Initialization failed");
					quit = true;
				}

				try {
					while (!quit) {
						try {
							if (!loop()) {
								break;
							}
						} catch (InterruptedException ie) {
							if (quit) {
								logger.warn("Driver interrupted");
								Thread.currentThread().interrupt();
							} else {
								logger.debug("Driver interrupted but not quitted");
							}
						}
					}
				} catch (Throwable t) {
					logger.error("Uncought exception in loop()", t);
				}

			} catch (InterruptedException t) {
				logger.debug("Initialization interrupted");
			} catch (Throwable t) {
				logger.error("Uncought exception in onInit()", t);
			}

			try {
				logger.info("Quitting...");
				onQuit();
				logger.info("Quitted");
			} catch (InterruptedException t) {
				logger.debug("onQuit() interrupted");
			} catch (Throwable t) {
				logger.error("Uncought exception in onQuit()", t);
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}

		future = null;
	}
}
