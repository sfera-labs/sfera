package cc.sferalabs.sfera.drivers;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.Task;
import cc.sferalabs.sfera.core.TasksManager;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.files.FilesWatcher;

public abstract class Driver extends Task implements Node {

	private final String id;
	private String configFile;
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
			future = TasksManager.getDefault().submit(this);
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
	 * @param configuration
	 * @throws InterruptedException
	 */
	protected void onConfigChange(Configuration configuration)
			throws InterruptedException {
		restart();
	}

	@Override
	protected void execute() {
		while (!quit) {
			Configuration config = null;
			String configWatcherId = null;
			try {
				logger.info("Starting...");
				config = new Configuration(configFile);
				try {
					configWatcherId = FilesWatcher.register(
							config.getRealPath(), this::onConfigFileModified,
							false);
				} catch (IOException e) {
					logger.error("Error watching config file", e);
				}
				if (onInit(config)) {
					logger.info("Started");
					try {
						while (!quit) {
							try {
								if (!loop()) {
									break;
								}
							} catch (InterruptedException ie) {
								if (quit) {
									logger.warn("Driver interrupted");
								} else {
									logger.debug("Driver interrupted but not quitted");
								}
							}
						}
					} catch (Throwable t) {
						logger.error("Exception in loop()", t);
					}
				} else {
					logger.warn("Initialization failed");
				}
			} catch (InterruptedException t) {
				logger.debug("Initialization interrupted");
			} catch (Throwable t) {
				logger.error("Exception in onInit()", t);
			}

			try {
				logger.info("Quitting...");
				if (configWatcherId != null) {
					FilesWatcher.unregister(config.getRealPath(),
							configWatcherId);
				}
				onQuit();
				logger.info("Quitted");
			} catch (InterruptedException t) {
				logger.debug("onQuit() interrupted");
			} catch (Throwable t) {
				logger.error("Exception in onQuit()", t);
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
				logger.debug("Configuration file deleted");
				return;
			}
			logger.info("Configuration changed");
			onConfigChange(config);
		} catch (Throwable t) {
			logger.error("Error in onConfigChange()", t);
		}
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

}
