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
	 */
	CommPortException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	CommPortException(String message, Throwable cause) {
		super(message, cause);
	}
}
