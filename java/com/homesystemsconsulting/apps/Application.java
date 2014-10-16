package com.homesystemsconsulting.apps;

import com.homesystemsconsulting.util.logging.SystemLogger;


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
