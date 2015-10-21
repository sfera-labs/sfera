package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.HttpApiEvent;
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

	private static final String PING_STRING = "&";

	private final HttpServletRequest httpRequest;
	private WsEventListener subscription;
	private final Task pingTask;

	/**
	 * 
	 * @param request
	 */
	ApiSocket(ServletUpgradeRequest request) {
		this.httpRequest = request.getHttpServletRequest();
		this.pingTask = new PingTask(this);
		logger.debug("Socket created - Host: {}", this.httpRequest.getRemoteHost());
	}

	/**
	 * @return the httpRequest
	 */
	HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		String host = httpRequest.getRemoteHost();
		logger.debug("Socket connected - Host: {}", host);
		ping();
	}

	/**
	 * 
	 */
	void ping() {
		try {
			send(PING_STRING);
		} catch (IOException e) {
			getSession().close(1002, "Ping error");
		}
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		logger.debug("Received message: {} - Host: {} User: {}", message,
				this.httpRequest.getRemoteHost(), httpRequest.getRemoteUser());

		if (message.equals(PING_STRING)) {
			TasksManager.execute(pingTask);
			return;
		}

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

			switch (action) {
			case "subscribe":
				String spec = message.getParameter("nodes");
				if (subscription != null) {
					subscription.destroy();
				}
				subscription = new WsEventListener(this, spec);
				resp.sendResult("ok");
				break;

			case "command":
				for (String command : message.getParameters()) {
					if (command.indexOf('.') > 0) { // driver command
						String param = message.getParameter(command);
						try {
							Object res = ScriptsEngine.executeDriverAction(command, param,
									httpRequest.getRemoteUser());
							resp.sendResult(res);
						} catch (Exception e) {
							resp.sendError(e.getMessage());
						}
						break;
					}
				}
				break;

			case "event":
				String eid = message.getParameter("eid");
				String eval = message.getParameter("eval");
				try {
					HttpApiEvent remoteEvent = new HttpApiEvent(eid, eval, httpRequest, resp);
					Bus.post(remoteEvent);
				} catch (Exception e) {
					resp.sendError(e.getMessage());
				}
				break;

			default:
				resp.sendError("Unknown action");
				break;
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
		logger.debug("Socket Closed: [{}] {} - Host: {}", statusCode, reason,
				httpRequest.getRemoteHost());
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		logger.warn("WebSocket error - Host: " + httpRequest.getRemoteHost(), cause);
		getSession().close(StatusCode.SERVER_ERROR, cause.getMessage());
	}

	/**
	 * @param text
	 * @throws IOException
	 */
	void send(String text) throws IOException {
		logger.debug("Sending: '{}' - Host: {}", text, httpRequest.getRemoteHost());
		getRemote().sendString(text);
	}

}
