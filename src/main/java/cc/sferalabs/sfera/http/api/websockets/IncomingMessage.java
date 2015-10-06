package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.RemoteEvent;
import cc.sferalabs.sfera.script.ScriptsEngine;

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
	 * Process this incoming message and sends a response.
	 * 
	 * @param socket
	 *            the WebSocket to send the response to
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	void process(ApiSocket socket) throws IOException {
		OutgoingMessage resp = new OutgoingMessage("response", socket);
		try {
			resp.put("action", action);
			String id = parameterMap.get("id");
			if (id == null) {
				resp.sendError("Param 'id' not found");
				return;
			}
			resp.put("id", id);

			if (action.equals("subscribe")) {
				String spec = parameterMap.get("spec");
				if (socket.subscription != null) {
					socket.subscription.destroy();
				}
				socket.subscription = new WsEventListener(socket, spec);
				resp.sendResult("ok");

			} else if (action.equals("command")) {
				for (String command : parameterMap.keySet()) {
					if (command.indexOf('.') > 0) { // driver command
						String param = parameterMap.get(command);
						try {
							Object res = ScriptsEngine.executeDriverAction(command, param,
									socket.getUserName());
							resp.sendResult(res);
							return;
						} catch (Exception e) {
							resp.sendError(e.getMessage());
							return;
						}
					}
				}

			} else if (action.equals("event")) {
				String eid = parameterMap.get("eid");
				String eval = parameterMap.get("eval");
				try {
					RemoteEvent remoteEvent = new RemoteEvent(eid, eval, socket.getUserName(),
							resp);
					Bus.post(remoteEvent);
				} catch (Exception e) {
					resp.sendError(e.getMessage());
				}

			} else {
				resp.sendError("Unknown action");
			}
		} catch (IOException e) {
			resp.sendError("Server error: " + e.getMessage());
			throw e;
		}
	}

}
