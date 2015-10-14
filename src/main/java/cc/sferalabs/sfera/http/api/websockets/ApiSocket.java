package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;
import java.security.Principal;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.RemoteEvent;
import cc.sferalabs.sfera.script.ScriptsEngine;

/**
 * {@link WebSocketAdapter} to process API requests
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class ApiSocket extends WebSocketAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ApiSocket.class);
	ServletUpgradeRequest request;
	Principal user;
	WsEventListener subscription;

	/**
	 * 
	 * @param request
	 */
	ApiSocket(ServletUpgradeRequest request) {
		this.request = request;
		this.user = request.getUserPrincipal();
		logger.debug("Socket created");
	}

	/**
	 * 
	 * @return
	 */
	public String getUserName() {
		return user.getName();
	}

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		logger.debug("Socket Connected");
		OutgoingMessage msg = new OutgoingMessage("connection", this);
		try {
			msg.sendResult("ok");
		} catch (IllegalStateException | IOException e) {
			logger.warn("Error sending connection response", e);
		}
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		logger.debug("Received message: {} - User: {}", message, user.getName());
		IncomingMessage m = new IncomingMessage(message);
		try {
			process(m);
		} catch (Exception e) {
			logger.warn("Error processing message", e);
		}
	}

	/**
	 * Process the specified incoming message and sends a response.
	 * 
	 * @param message
	 *            the message to process
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private void process(IncomingMessage message) throws IOException {
		OutgoingMessage resp = new OutgoingMessage("response", this);
		try {
			String action = message.getAction();
			resp.put("action", action);
			String id = message.getParameter("id");
			if (id == null) {
				resp.sendError("Param 'id' not found");
				return;
			}
			resp.put("id", id);

			if (action.equals("subscribe")) {
				String spec = message.getParameter("nodes");
				if (subscription != null) {
					subscription.destroy();
				}
				subscription = new WsEventListener(this, spec);
				resp.sendResult("ok");

			} else if (action.equals("command")) {
				for (String command : message.getParameters()) {
					if (command.indexOf('.') > 0) { // driver command
						String param = message.getParameter(command);
						try {
							Object res = ScriptsEngine.executeDriverAction(command, param,
									getUserName());
							resp.sendResult(res);
							return;
						} catch (Exception e) {
							resp.sendError(e.getMessage());
							return;
						}
					}
				}

			} else if (action.equals("event")) {
				String eid = message.getParameter("eid");
				String eval = message.getParameter("eval");
				try {
					RemoteEvent remoteEvent = new RemoteEvent(eid, eval, getUserName(), resp);
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

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		if (subscription != null) {
			subscription.destroy();
			subscription = null;
		}
		logger.debug("Socket Closed: {} - {}", statusCode, reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		logger.warn("WebSocket error", cause);
		getSession().close(StatusCode.SERVER_ERROR, cause.getMessage());
	}

}
