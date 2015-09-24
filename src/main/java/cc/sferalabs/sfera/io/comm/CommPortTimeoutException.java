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
	 */
	CommPortTimeoutException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	CommPortTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
}
