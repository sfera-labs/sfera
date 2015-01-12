package cc.sferalabs.sfera.io.comm;

public class CommPortException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7492995860952339470L;

	/**
	 * 
	 * @param message
	 */
	public CommPortException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public CommPortException(String message, Throwable cause) {
		super(message, cause);
	}
}
