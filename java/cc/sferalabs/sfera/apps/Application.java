package cc.sferalabs.sfera.apps;

import cc.sferalabs.sfera.util.logging.SystemLogger;


public abstract class Application {
	
	protected final SystemLogger log;
	
	/**
	 * 
	 * @param id
	 */
	public Application(String id) {
		log = SystemLogger.getLogger("app." + id);
	}

	/**
	 * 
	 * @return
	 */
	public abstract boolean onRemove();
}
