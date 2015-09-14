package cc.sferalabs.sfera.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.SystemNode;

public abstract class LazyService implements Service {

	private static final Logger logger = LoggerFactory.getLogger(LazyService.class);

	/**
	 * 
	 */
	protected LazyService() {
		SystemNode.addToLifeCycle(this);
		logger.debug("Service {} instatiated", getClass().getSimpleName());
	}

}
