package cc.sferalabs.sfera.core;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default system exception handler
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class SystemExceptionHandler implements UncaughtExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(SystemExceptionHandler.class);

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.error("Uncaught exception in thread: " + t.getName(), e);
	}
}
