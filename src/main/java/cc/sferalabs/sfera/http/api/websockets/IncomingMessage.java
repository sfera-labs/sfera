package cc.sferalabs.sfera.http.api.websockets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class representing an incoming WebSocket message.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class IncomingMessage {

	private final String action;
	private final Map<String, String> parameterMap = new HashMap<>();

	/**
	 * Construct an IncomingMessage parsing the specified message.
	 * 
	 * @param message
	 *            the message to parse
	 */
	IncomingMessage(String message) {
		message = message.trim();
		int qm = message.indexOf('?');
		if (qm < 0) {
			action = message;
		} else {
			action = message.substring(0, qm).trim();
			if (message.length() > qm + 1) {
				String[] params = message.substring(qm + 1).split("&");
				for (String param : params) {
					param = param.trim();
					if (!param.isEmpty()) {
						int eq = param.indexOf('=');
						String p;
						String v;
						if (eq < 0) {
							p = param;
							v = null;
						} else {
							p = param.substring(0, eq).trim();
							if (param.length() > eq + 1) {
								v = param.substring(eq + 1).trim();
							} else {
								v = "";
							}
						}
						parameterMap.put(p, v);
					}
				}
			}
		}
	}

	/**
	 * @return the action
	 */
	String getAction() {
		return action;
	}

	/**
	 * Returns the value of the specified parameter, or {@code null} if there is
	 * no such parameter.
	 * 
	 * @param the
	 *            parameter name
	 * @return the parameter value, or {@code null} if there is no such
	 *         parameter
	 */
	String getParameter(String name) {
		return parameterMap.get(name);
	}
	
	/**
	 * @return the set of available parameters
	 */
	Set<String> getParameters() {
		return parameterMap.keySet();
	}

}
