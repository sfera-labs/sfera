package cc.sferalabs.sfera.http;

public class HttpServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4472448159551400407L;

	/**
	 * 
	 * @param message
	 */
	public HttpServerException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public HttpServerException(Throwable cause) {
		super(cause);
	}

}
