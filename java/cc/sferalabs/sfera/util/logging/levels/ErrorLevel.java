package cc.sferalabs.sfera.util.logging.levels;

import java.util.logging.Level;

public class ErrorLevel extends Level {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1324680434397082883L;

	/**
	 * 
	 */
	public ErrorLevel() {
		super("ERROR", Level.SEVERE.intValue() + 1);
	}
}
