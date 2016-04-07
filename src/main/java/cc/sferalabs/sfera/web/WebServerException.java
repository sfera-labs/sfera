package cc.sferalabs.sfera.web;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class WebServerException extends Exception {

	/**
	 * Constructs a HttpServerException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public WebServerException(String message) {
		super(message);
	}

	/**
	 * Constructs a HttpServerException with the specified cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public WebServerException(Throwable cause) {
		super(cause);
	}

}
