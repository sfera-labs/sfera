package com.homesystemsconsulting.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import com.homesystemsconsulting.util.logging.SystemLogger;

public class SystemExceptionHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		SystemLogger.SYSTEM.error("exceptionHandler", "Uncaught exception in thread '" + t.getName() + "': " + getStackTraceString(e));
		e.printStackTrace();
	}
	
	/**
	 * 
	 * @param e
	 * @return
	 */
	public static String getStackTraceString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString().replaceAll("\\s+", " ");
	}

}
