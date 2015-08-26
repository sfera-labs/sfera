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

	protected final Logger log;
	private String configFile;
	private String configWatcherId;

	/**
	 * 
	 */
	public Application() {
		this.log = LogManager.getLogger(getClass().getName());
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
				log.info("Enabling...");
				try {
					configWatcherId = FilesWatcher.register(config.getRealPath(),
							thisApp::onConfigFileModified, false);
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
	private void onConfigFileModified() {
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
