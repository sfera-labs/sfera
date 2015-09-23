package cc.sferalabs.sfera.http;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class HttpServerException extends Exception {

	/**
	 * Constructs a HttpServerException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public HttpServerException(String message) {
		super(message);
	}

	/**
	 * Constructs a HttpServerException with the specified cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public HttpServerException(Throwable cause) {
		super(cause);
	}

}
