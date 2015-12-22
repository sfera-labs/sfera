package cc.sferalabs.sfera.io.comm;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class CommPortTimeoutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7877472625857220541L;

	/**
	 * 
	 * @param message
	 *            the detail message
	 */
	CommPortTimeoutException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	CommPortTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
}
