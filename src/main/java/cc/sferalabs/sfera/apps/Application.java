package cc.sferalabs.sfera.apps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Configuration;

public abstract class Application {
	
	protected final Logger logger;
	
	/**
	 * 
	 */
	public Application() {
		this.logger = LogManager.getLogger(getClass().getName());
	}
	
	/**
	 * 
	 * @param configuration
	 */
	public abstract void onEnable(Configuration configuration);

	/**
	 * 
	 */
	public abstract void onDisable();
}
