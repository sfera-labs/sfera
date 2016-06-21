/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.drivers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.util.files.FilesWatcher;

/**
 * Abstract class to be implemented to create drivers.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Driver extends Node {

	private String configFile;
	private DriverTask driverExecutor = new DriverTask();
	private volatile boolean quit = false;
	private Future<?> future;

	protected final Logger log;

	/**
	 *
	 * @author Giampiero Baggiani
	 *
	 * @version 1.0.0
	 *
	 */
	private class DriverTask extends Task {

		/**
		 * @param name
		 */
		public DriverTask() {
			super("driver." + getId());
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
								"Driver config reload", this::reloadConfiguration, false, false);
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
										log.debug("Driver interrupted");
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
						FilesWatcher.unregister(configWatcherId);
					}
					onQuit();
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

	}

	/**
	 * Constructs a {@code Driver}
	 * 
	 * @param id
	 *            the driver ID
	 */
	protected Driver(String id) {
		super(id);
		this.log = LoggerFactory.getLogger(getClass().getName() + "." + id);
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
			log.debug("Interrupting driver...");
			future.cancel(true);
		}
	}

	/**
	 * Stars the driver in a separate process.
	 */
	public synchronized void start() {
		if (future == null) {
			quit = false;
			future = TasksManager.submit(driverExecutor);
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
		try {
			waitTermination(30000);
		} catch (TimeoutException e) {
		}
		start();
	}

	/**
	 * Waits for the termination of the driver task. If the timeout expires
	 * before termination a TimeoutException is thrown.
	 * 
	 * @param timeout
	 *            timeout in milliseconds
	 * @throws InterruptedException
	 *             if interrupted
	 * @throws TimeoutException
	 *             if the timeout expires
	 */
	void waitTermination(long timeout) throws InterruptedException, TimeoutException {
		long giveUpTime = System.currentTimeMillis() + timeout;
		while (future != null) {
			if (System.currentTimeMillis() > giveUpTime) {
				throw new TimeoutException();
			}
			Thread.sleep(500);
		}
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
	 * @throws IOException
	 *             if an I/O error occurs creating the directory
	 */
	protected Path getDriverGlobalDataDir() throws IOException {
		Path path = Paths.get("data/drivers/", getClass().getPackage().getName());
		Files.createDirectories(path);
		return path;
	}

	/**
	 * Returns the path to the directory to be used to store data this driver
	 * instance.
	 * 
	 * @return the path to the directory to be used to store data this driver
	 *         instance
	 * @throws IOException
	 *             if an I/O error occurs creating the directory
	 */
	protected Path getDriverInstanceDataDir() throws IOException {
		Path path = getDriverGlobalDataDir().resolve(getId());
		Files.createDirectories(path);
		return path;
	}

}
