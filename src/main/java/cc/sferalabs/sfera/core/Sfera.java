package cc.sferalabs.sfera.core;

import cc.sferalabs.sfera.logging.LoggerUtils;

/**
 * Entry point class containing the main method
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Sfera {

	/** Package prefix used by Sfera */
	public static final String BASE_PACKAGE = "cc.sferalabs.sfera";

	static {

	}

	/**
	 * The main method
	 * 
	 * @param args
	 *            program arguments
	 */
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		LoggerUtils.init();
		Thread.setDefaultUncaughtExceptionHandler(new SystemExceptionHandler());
		SystemNode.getInstance().start();
	}

}
