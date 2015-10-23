package cc.sferalabs.sfera.apps;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.EventListener;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Bus;

/**
 * Skeleton for the implementation of applications.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 * 
 */
public abstract class Application implements EventListener {

	protected final Logger log;
	private String configFile;
	private UUID configWatcherId;

	/**
	 * Construct an Application
	 */
	protected Application() {
		this.log = LoggerFactory.getLogger(getClass().getName());
	}

	/**
	 * Sets the path of the application's configuration file
	 * 
	 * @param configFile
	 *            the path of the configuration file
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	/**
	 * Enables this application in a separate thread. This is the entry point of
	 * the application life-cycle
	 * 
	 * @throws IOException
	 *             if an I/O error occurs loading the configuration
	 */
	public synchronized void enable() throws IOException {
		final Configuration config = new Configuration(configFile);
		final Application thisApp = this;
		TasksManager.submit(new Task("App " + getClass().getName() + " enable") {

			@Override
			protected void execute() {
				log.info("Enabling...");
				try {
					configWatcherId = FilesWatcher.register(config.getRealPath(),
							thisApp::relaodConfiguration, false);
				} catch (Exception e) {
					log.error("Error watching config file", e);
				}
				try {
					onEnable(config);
					Bus.register(thisApp);
					log.info("Enabled");
				} catch (Throwable t) {
					log.error("Initialization error", t);
				}
			}
		});
	}

	/**
	 * Disables this application in a separate thread
	 */
	public synchronized void disable() {
		final Application thisApp = this;
		TasksManager.submit(new Task("App " + getClass().getName() + " disable") {

			@Override
			protected void execute() {
				try {
					Bus.unregister(thisApp);
					doDisable();
				} catch (Throwable t) {
					log.error("Error in onDisable()", t);
				}
			}
		});
	}

	/**
	 *  
	 */
	private void doDisable() {
		log.info("Disabling...");
		FilesWatcher.unregister(Configuration.getPath(configFile), configWatcherId);
		try {
			onDisable();
			log.info("Disabled");
		} catch (Throwable t) {
			log.error("Error while disabling", t);
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private synchronized void restart() throws IOException {
		doDisable();
		enable();
	}

	/**
	 * 
	 */
	private void relaodConfiguration() {
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
	 * Callback method called when the configuration of this application is
	 * modified. The default implementation restarts the application.
	 * 
	 * @param configuration
	 *            the new configuration
	 */
	protected void onConfigChange(Configuration configuration) throws InterruptedException {
		try {
			restart();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Callback method called when the application is enabled.
	 * 
	 * @param config
	 *            the configuration
	 */
	protected abstract void onEnable(Configuration config);

	/**
	 * Callback method called when the application is disabled.
	 */
	protected abstract void onDisable();

}
