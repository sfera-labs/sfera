package com.homesystemsconsulting.drivers;

import java.util.concurrent.Future;

import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.core.TasksManager;
import com.homesystemsconsulting.events.Node;
import com.homesystemsconsulting.util.logging.SystemLogger;


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
	protected Driver(String id) {
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
	 * @return
	 * @throws InterruptedException
	 */
	protected abstract boolean onInit() throws InterruptedException;
	
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
	public void quit() {
		this.quit = true;
		if (future != null) {
			future.cancel(true);
		}
	}
	
	/**
	 * 
	 */
	public void startIfNotActive() {
		if (enabled) {
			if (future != null && !future.isDone()) {
				return;
			}
			
			quit = false;
			future = TasksManager.DEFAULT.submit(this);
		}
	}

	@Override
	public void execute() {
		try {
			log.info("starting...");
			if (onInit()) {
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
	}
}
