package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.HttpEvent;
import cc.sferalabs.sfera.script.ScriptsEngine;

class Message {

	private static final Logger logger = LogManager.getLogger();
	private final String action;
	private final Map<String, String> parameterMap = new HashMap<>();

	/**
	 * 
	 * @param message
	 */
	Message(String message) {
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
	 * 
	 * @param socket
	 * @return
	 */
	public void process(ApiSocket socket) {
		WsResponse resp = new WsResponse(socket);
		try {
			resp.put("type", "response");
			resp.put("action", action);
			String id = parameterMap.get("id");
			resp.put("id", id);
			if (id == null) {
				resp.sendError("Param 'id' not found");
				return;
			}

			if (action.equals("subscribe")) {
				String nodes = parameterMap.get("nodes");
				if (nodes == null) {
					resp.sendError("Param 'nodes' not found");
					return;
				}

				if (socket.subscription != null) {
					socket.subscription.destroy();
				}
				socket.subscription = new WsSubscriber(socket.getRemote(), nodes);
				resp.sendResult("ok");

			} else if (action.equals("command")) {
				Object res = null;
				for (String command : parameterMap.keySet()) {
					if (command.indexOf('.') > 0) { // driver command
						String param = parameterMap.get(command);
						try {
							res = ScriptsEngine.executeDriverCommand(command, param,
									socket.getUserName());
							break;
						} catch (Exception e) {
							resp.sendError(e.getMessage());
							return;
						}
					}
				}
				resp.sendResult(res);

			} else if (action.equals("event")) {
				String eid = parameterMap.get("eid");
				if (eid == null) {
					resp.sendError("Param 'eid' not found");
					return;
				}
				String eval = parameterMap.get("eval");
				if (eval == null) {
					resp.sendError("Param 'eval' not found");
					return;
				}

				HttpEvent httpEvent = new HttpEvent(eid, eval, socket.getUserName(), resp);
				Bus.post(httpEvent);

			} else {
				resp.sendError("Unknown action");
			}
		} catch (Exception e) {
			logger.warn("Error processing message", e);
			try {
				resp.sendError("Server error: " + e.getMessage());
			} catch (IOException ioe) {
			}
		}
	}

}
