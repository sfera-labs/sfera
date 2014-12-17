package cc.sferalabs.sfera.drivers;

import java.util.concurrent.Future;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.Task;
import cc.sferalabs.sfera.core.TasksManager;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.util.logging.SystemLogger;

public abstract class Driver extends Task implements Node {

	private final String id;
	private boolean quit = false;
	private Future<?> future;
	private boolean enabled = true;

	protected final SystemLogger log;

	/**
	 * 
	 * @param id
	 */
	public Driver(String id) {
		super("driver." + id);
		this.id = id;
		this.log = SystemLogger.getLogger("driver." + id);
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
	public void enable() {
		enabled = true;
		start();
	}

	/**
	 * 
	 */
	public void disable() {
		enabled = false;
		quit();
	}

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
		if (enabled) {
			if (future == null) {
				quit = false;
				future = TasksManager.DEFAULT.submit(this);
			}
		}
	}

	@Override
	public void execute() {
		try {
			log.info("starting...");
			if (onInit(new Configuration(getId()))) {
				log.info("started");
			} else {
				log.warning("initialization failed");
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
							log.warning("driver interrupted");
							Thread.currentThread().interrupt();
						} else {
							log.debug("driver interrupted but not quitted");
						}
					}
				}
			} catch (Throwable t) {
				log.error("uncought exception in loop(): " + t);
			}

		} catch (InterruptedException t) {
			log.debug("initialization interrupted");
		} catch (Throwable t) {
			log.error("uncought exception in onInit(): " + t);
		}

		try {
			log.info("quitting...");
			onQuit();
			log.info("quitted");
		} catch (InterruptedException t) {
			log.debug("onQuit() interrupted");
		} catch (Throwable t) {
			log.error("uncought exception in onQuit(): " + t);
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		
		future = null;
		start();
	}
}
