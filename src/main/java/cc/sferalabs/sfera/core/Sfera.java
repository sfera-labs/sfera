package cc.sferalabs.sfera.core;

import java.nio.file.Files;
import java.nio.file.Path;

public class Sfera {

	public static final String BASE_PACKAGE = "cc.sferalabs.sfera";

	static {
		Path log4j2Config = Configuration.getConfigDir().resolve("log4j2.xml");
		if (Files.exists(log4j2Config)) {
			System.setProperty("log4j.configurationFile", log4j2Config.toString());
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		Thread.setDefaultUncaughtExceptionHandler(new SystemExceptionHandler());
		SystemNode.getInstance().start();
	}

}
