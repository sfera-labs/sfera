package cc.sferalabs.sfera.core;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemExceptionHandler implements UncaughtExceptionHandler {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.error("Uncaught exception in thread: " + t.getName(), e);
	}
}
