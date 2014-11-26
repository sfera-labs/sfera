package cc.sferalabs.sfera.drivers.webserver;

public class MalformedRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3326807028723261640L;

	public MalformedRequestException(String message) {
		super(message);
	}

	public MalformedRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
