package cc.sferalabs.sfera.apps;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.EventListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.services.FilesWatcher;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Bus;

public abstract class Application implements EventListener {

	protected final Logger logger;
	private String configFile;
	private String configWatcherId;

	/**
	 * 
	 */
	public Application() {
		this.logger = LogManager.getLogger(getClass().getName());
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
	 * @throws IOException
	 */
	public synchronized void enable() throws IOException {
		final Configuration config = new Configuration(configFile);
		final Application thisApp = this;
		TasksManager.submit(new Task("App " + getClass().getName() + " enable") {

			@Override
			protected void execute() {
				logger.info("Enabling...");
				try {
					configWatcherId = FilesWatcher.register(config.getRealPath(),
							thisApp::onConfigFileModified, false);
				} catch (Exception e) {
					logger.error("Error watching config file", e);
				}
				try {
					onEnable(config);
					Bus.register(thisApp);
					logger.info("Enabled");
				} catch (Throwable t) {
					logger.error("Initialization error", t);
				}
			}
		});
	}

	/**
	 * 
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
					logger.error("Error in onDisable()", t);
				}
			}
		});
	}

	/**
	 * 
	 */
	private void doDisable() {
		logger.info("Disabling...");
		FilesWatcher.unregister(Configuration.getPath(configFile), configWatcherId);
		try {
			onDisable();
			logger.info("Disabled");
		} catch (Throwable t) {
			logger.error("Error while disabling", t);
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
	private void onConfigFileModified() {
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
	 */
	protected void onConfigChange(Configuration configuration) throws InterruptedException {
		try {
			restart();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param config
	 */
	protected abstract void onEnable(Configuration config);

	/**
	 * 
	 */
	protected abstract void onDisable();

}
