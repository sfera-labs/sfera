package cc.sferalabs.sfera.drivers.webserver;

public class NotImplementedRequestMethodException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5643922044877969686L;

	public NotImplementedRequestMethodException(String message) {
		super(message);
	}

	public NotImplementedRequestMethodException(String message, Throwable cause) {
		super(message, cause);
	}
}
