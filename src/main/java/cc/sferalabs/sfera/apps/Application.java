package cc.sferalabs.sfera.apps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	 * @return
	 */
	public abstract boolean onRemove();
}
