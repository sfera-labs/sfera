package com.homesystemsconsulting.util.logging.levels;

import java.util.logging.Level;

public class VerboseLevel extends Level {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5763061640738559057L;

	/**
	 * 
	 */
	public VerboseLevel() {
		super("VERBOSE", Level.FINEST.intValue() + 1);
	}
}
