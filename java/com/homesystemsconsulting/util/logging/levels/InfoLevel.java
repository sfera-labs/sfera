package com.homesystemsconsulting.util.logging.levels;

import java.util.logging.Level;

public class InfoLevel extends Level {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6157158601202308552L;

	/**
	 * 
	 */
	public InfoLevel() {
		super("INFO", Level.INFO.intValue() + 1);
	}
}
