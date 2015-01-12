package cc.sferalabs.sfera.io.comm;

public class CommPortTimeoutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7877472625857220541L;

	/**
	 * 
	 * @param message
	 */
	public CommPortTimeoutException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public CommPortTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
}
