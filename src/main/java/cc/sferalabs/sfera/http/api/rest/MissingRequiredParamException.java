/**
 * 
 */
package cc.sferalabs.sfera.http.api.rest;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class MissingRequiredParamException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6730622870037416881L;

	/**
	 * 
	 * @param paramName
	 *            the required parameter name
	 */
	public MissingRequiredParamException(String paramName) {
		super("Param '" + paramName + "' not specified");
	}

}
