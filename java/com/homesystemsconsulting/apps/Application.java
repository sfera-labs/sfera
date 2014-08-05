package com.homesystemsconsulting.apps;

import com.homesystemsconsulting.util.logging.SystemLogger;


public abstract class Application {
	
	protected final SystemLogger log;
	
	public Application() {
		log = SystemLogger.getLogger("app." + this.getClass().getSimpleName());
	}

	public abstract boolean onInit();

	public abstract boolean onQuit();
}
