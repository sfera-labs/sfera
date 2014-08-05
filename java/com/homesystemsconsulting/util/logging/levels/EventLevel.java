package com.homesystemsconsulting.util.logging.levels;

import java.util.logging.Level;

public class EventLevel extends Level {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2092518942651591053L;

	/**
	 * 
	 */
	public EventLevel() {
		super("EVENT", Level.FINE.intValue() + 1);
	}
}
