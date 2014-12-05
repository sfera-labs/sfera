package cc.sferalabs.sfera.util.logging.levels;

import java.util.logging.Level;

public class DebugLevel extends Level {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4492276257389561803L;

	/**
	 * 
	 */
	public DebugLevel() {
		super("DEBUG", Level.FINER.intValue() + 1);
	}
}
