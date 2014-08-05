package com.homesystemsconsulting.util.logging.levels;

import java.util.logging.Level;

public class WarningLevel extends Level {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8748619297518426092L;

	/**
	 * 
	 */
	public WarningLevel() {
		super("WARNING", Level.WARNING.intValue() + 1);
	}
}
