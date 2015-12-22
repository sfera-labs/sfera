package cc.sferalabs.sfera.io.comm;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class CommPortException extends Exception {

	/**
	 * 
	 * @param message
	 *            the detail message
	 */
	CommPortException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	CommPortException(String message, Throwable cause) {
		super(message, cause);
	}
}
