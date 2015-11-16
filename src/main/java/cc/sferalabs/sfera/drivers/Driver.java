package cc.sferalabs.sfera.drivers;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Node;

/**
 * Abstract class to be implemented to create drivers.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Driver extends Task implements Node {

	private final String id;
	private String configFile;
	private volatile boolean quit = false;
	private Future<?> future;

	protected final Logger log;

	/**
	 * Contruct a Driver
	 * 
	 * @param id
	 *            the driver ID
	 */
	protected Driver(String id) {
		super("driver." + id);
		this.id = id;
		this.log = LoggerFactory.getLogger(getClass().getName() + "." + id);
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Sets the path of the configuration file relative to the configuration
	 * directory.
	 * 
	 * @param configFile
	 *            the relative configuration file path
	 */
	void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	/**
	 * Interrupts the driver process.
	 */
	public synchronized void quit() {
		quit = true;
		if (future != null) {
			future.cancel(true);
		}
	}

	/**
	 * Stars the driver in a separate process.
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
	 * Gracefully restarts the driver process.
	 * 
	 * @throws InterruptedException
	 *             if interrupted while waiting for the driver to quit
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
	 * <p>
	 * Callback method called when the driver configuration has changed.
	 * </p>
	 * <p>
	 * The default implementation simple restarts the driver. Subclasses can
	 * override this method to optimize the handling of configuration change.
	 * </p>
	 * 
	 * @param config
	 *            the new configuration
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
			UUID configWatcherId = null;
			try {
				log.info("Starting...");
				config = new Configuration(configFile);
				try {
					configWatcherId = FilesWatcher.register(config.getRealPath(),
							this::reloadConfiguration, false);
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
	private void reloadConfiguration() {
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
			log.error("Error reloading configuration", t);
		}
	}

	/**
	 * Callback method called on driver initialization.
	 * 
	 * @param config
	 *            the driver configuration
	 * @return {@code true} if the initialization is successful, {@code false}
	 *         otherwise
	 * @throws InterruptedException
	 *             if interrupted while executing
	 */
	protected abstract boolean onInit(Configuration config) throws InterruptedException;

	/**
	 * Callback method continuously called after a successful initialization
	 * until {@code false} is returned.
	 * 
	 * @return {@code true} if this method has to be called again, {@code false}
	 *         to quit the drivers
	 * @throws InterruptedException
	 *             if interrupted while executing
	 */
	protected abstract boolean loop() throws InterruptedException;

	/**
	 * Callback method called when the driver is about to quit.
	 */
	protected abstract void onQuit();

	/**
	 * Returns the path to the directory to be used to store data for all
	 * instances of this driver.
	 * 
	 * @return the path to the directory to be used to store data for all
	 *         instances of this driver
	 */
	protected Path getDriverGlobalDataDir() {
		return Paths.get("data/drivers/", getClass().getPackage().getName());
	}

	/**
	 * Returns the path to the directory to be used to store data this driver
	 * instance.
	 * 
	 * @return the path to the directory to be used to store data this driver
	 *         instance
	 */
	protected Path getDriverInstanceDataDir() {
		return getDriverGlobalDataDir().resolve(getId());
	}

}
