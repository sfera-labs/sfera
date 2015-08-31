package cc.sferalabs.sfera.drivers;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Node;

public abstract class Driver extends Task implements Node {

	private final String id;
	private String configFile;
	private boolean quit = false;
	private Future<?> future;

	protected final Logger log;

	/**
	 * 
	 * @param id
	 */
	public Driver(String id) {
		super("driver." + id);
		this.id = id;
		this.log = LogManager.getLogger(getClass().getName() + "." + id);
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param configFile
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	/**
	 * 
	 */
	public synchronized void quit() {
		quit = true;
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
			future = TasksManager.submit(this);
		} else {
			throw new IllegalStateException("Running");
		}
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void restart() throws InterruptedException {
		quit();
		long giveUpTime = System.currentTimeMillis() + 300000;
		while (future != null) {
			if (System.currentTimeMillis() > giveUpTime) {
				break;
			}
			Thread.sleep(500);
		}
		start();
	}

	/**
	 * 
	 * @param config
	 */
	protected void onConfigChange(Configuration config) {
		try {
			restart();
		} catch (InterruptedException e) {
			log.warn("onConfigChange() interrupted");
		}
	}

	@Override
	protected void execute() {
		while (!quit) {
			Configuration config = null;
			String configWatcherId = null;
			try {
				log.info("Starting...");
				config = new Configuration(configFile);
				try {
					configWatcherId = FilesWatcher.register(config.getRealPath(),
							this::onConfigFileModified, false);
				} catch (IOException e) {
					log.error("Error watching config file", e);
				}
				if (onInit(config)) {
					log.info("Started");
					try {
						while (!quit) {
							try {
								if (!loop()) {
									break;
								}
							} catch (InterruptedException ie) {
								if (quit) {
									log.warn("Driver interrupted");
								} else {
									log.debug("Driver interrupted but not quitted");
								}
							}
						}
					} catch (Throwable t) {
						log.error("Exception in loop()", t);
					}
				} else {
					log.warn("Initialization failed");
				}
			} catch (InterruptedException t) {
				log.debug("Initialization interrupted");
			} catch (Throwable t) {
				log.error("Exception in onInit()", t);
			}

			try {
				log.info("Quitting...");
				if (configWatcherId != null) {
					FilesWatcher.unregister(config.getRealPath(), configWatcherId);
				}
				onQuit();
				// TODO set to UNKNOWN all driver's datapoints?
				log.info("Quitted");
			} catch (Throwable t) {
				log.error("Exception in onQuit()", t);
			}

			if (!quit) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
		}

		future = null;
	}

	/**
	 * 
	 */
	public void onConfigFileModified() {
		Configuration config = null;
		try {
			try {
				config = new Configuration(configFile);
			} catch (NoSuchFileException e) {
				log.debug("Configuration file deleted");
				return;
			}
			log.info("Configuration changed");
			onConfigChange(config);
		} catch (Throwable t) {
			log.error("Error in onConfigChange()", t);
		}
	}

	/**
	 * 
	 * @param config
	 * @return
	 * @throws InterruptedException
	 */
	protected abstract boolean onInit(Configuration config) throws InterruptedException;

	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	protected abstract boolean loop() throws InterruptedException;

	/**
	 * 
	 */
	protected abstract void onQuit();

}
