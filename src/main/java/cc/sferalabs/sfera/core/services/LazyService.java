package cc.sferalabs.sfera.core.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.SystemNode;

public abstract class LazyService implements Service {
	
	private static final Logger logger = LogManager.getLogger();
	
	/**
	 * 
	 */
	protected LazyService() {
		SystemNode.addToLifeCycle(this);
		logger.debug("Service {} instatiated", getClass().getSimpleName());
	}

}
