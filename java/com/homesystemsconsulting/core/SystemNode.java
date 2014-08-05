package com.homesystemsconsulting.core;

import com.homesystemsconsulting.events.Node;

public class SystemNode implements Node {

	public static final SystemNode INSTANCE = new SystemNode();
	
	private static OS_TYPE OS;
	
	@Override
	public String getId() {
		return "system";
	}

	public static void init() {
		// OS detection
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("linux")) {
			OS = OS_TYPE.LINUX;
		} else if (os.contains("mac") || os.contains("os x")) {
			OS = OS_TYPE.MAC;
		} else if (os.contains("windows")) {
			OS = OS_TYPE.WINDOWS;
		} else {
			throw new RuntimeException("Unsupported OS: " + os);
		}
	}
	
	public static OS_TYPE getOS() {
		return OS;
	}

	public enum OS_TYPE {
		LINUX, MAC, WINDOWS;
	}
}


