/**
 * 
 */
package cc.sferalabs.sfera.web.api;

import org.json.JSONObject;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ErrorMessage extends JSONObject {

	public static final ErrorMessage UNAUTHORIZED = new ErrorMessage(0, "Unauthorized");

	/**
	 * Constructor.
	 * 
	 * @param code
	 *            the error code
	 * @param message
	 *            the error message
	 */
	public ErrorMessage(int code, String message) {
		put("code", code);
		put("message", message);
	}

}
