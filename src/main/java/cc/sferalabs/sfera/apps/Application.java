package cc.sferalabs.sfera.apps;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EventListener;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.util.files.FilesWatcher;

/**
 * Skeleton for the implementation of applications.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 * 
 */
public abstract class Application implements AutoStartService, EventListener {

	protected final Logger log;
	private String configFile;
	private UUID configWatcherId;
	private boolean enabled;

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
	 * Initializes this application in a separate thread. This is the entry
	 * point of the application life-cycle.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs loading the configuration
	 */
	@Override
	public synchronized void init() throws IOException {
		final Configuration config = new Configuration(configFile);
		final Application thisApp = this;
		TasksManager.submit(new Task("App " + getClass().getName() + " enable") {

			@Override
			protected void execute() {
				log.info("Enabling...");
				try {
					configWatcherId = FilesWatcher.register(config.getRealPath(),
							"App config reload", thisApp::relaodConfiguration, false, false);
				} catch (Exception e) {
					log.error("Error watching config file", e);
				}
				try {
					onEnable(config);
					Bus.register(thisApp);
					enabled = true;
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
	@Override
	public synchronized void quit() {
		final Application thisApp = this;
		TasksManager.submit(new Task("App " + getClass().getName() + " quit") {

			@Override
			protected void execute() {
				try {
					Bus.unregister(thisApp);
					doDisable();
				} catch (Throwable t) {
					log.error("Error while disabling", t);
				}
			}
		});
	}

	/**
	 *  
	 */
	private void doDisable() {
		log.info("Disabling...");
		FilesWatcher.unregister(configWatcherId);
		onDisable();
		enabled = false;
		log.info("Disabled");
	}

	/**
	 * Returns whether this app is enabled or not.
	 * 
	 * @return whether this app is enabled or not
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 
	 * @throws IOException
	 */
	private synchronized void restart() throws IOException {
		doDisable();
		init();
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
	 * @param config
	 *            the new configuration
	 */
	protected void onConfigChange(Configuration config) {
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

	/**
	 * Returns the path to the directory to be used to store data for this
	 * application.
	 * 
	 * @return the path to the directory to be used to store data for this
	 *         application
	 */
	protected Path getAppDataDir() {
		return Paths.get("data/apps/", getClass().getPackage().getName());
	}

}
