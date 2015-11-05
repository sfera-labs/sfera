package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
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
import cc.sferalabs.sfera.http.api.JsonMessage;
import cc.sferalabs.sfera.script.ScriptsEngine;

/**
 * {@link WebSocketAdapter} to process API requests
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ApiSocket extends WebSocketAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ApiSocket.class);

	private static final String PING_STRING = "&";

	private final HttpServletRequest httpRequest;
	private WsEventListener subscription;
	private final Task pingTask;
	private final long pingInterval;
	private final long pongTimeout;

	/**
	 * 
	 * @param request
	 * @param pingInterval
	 * @param pongTimeout
	 */
	ApiSocket(ServletUpgradeRequest request, long pingInterval, long pongTimeout) {
		this.httpRequest = request.getHttpServletRequest();
		this.pingInterval = pingInterval;
		this.pongTimeout = pongTimeout;
		this.pingTask = new PingTask(this, pingInterval);
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
		try {
			OutgoingWsMessage resp = new OutgoingWsMessage("connection", this);
			resp.put("pingInterval", pingInterval);
			resp.put("pongTimeout", pongTimeout);
			resp.send();
			ping();
			logger.debug("Socket connected - Host: {}", httpRequest.getRemoteHost());
		} catch (Exception e) {
			logger.warn("Connection error", e);
		}
	}

	/**
	 * 
	 */
	void ping() {
		try {
			send(PING_STRING);
		} catch (Exception e) {
			getSession().close(1002, "Ping error: " + e.getMessage());
		}
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		logger.debug("Received message: '{}' - Host: {} User: {}", message,
				this.httpRequest.getRemoteHost(), httpRequest.getRemoteUser());

		if (message.equals(PING_STRING)) {
			TasksManager.execute(pingTask);
			return;
		}

		try {
			JsonMessage m = new JsonMessage(message);
			process(m);
		} catch (Exception e) {
			logger.warn("Error processing message '" + message + "'", e);
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
	private void process(JsonMessage message) throws IOException {
		OutgoingWsMessage resp = new OutgoingWsMessage("response", this);
		try {
			Object tag = message.get("tag");
			if (tag == null) {
				resp.sendError("Attribute 'tag' not found");
				return;
			}
			String action = message.get("action");
			if (action == null) {
				resp.sendError("Attribute 'action' not found");
				return;
			}
			resp.put("tag", tag);
			resp.put("action", action);

			switch (action) {
			case "subscribe":
				String nodes = message.get("nodes");
				if (subscription != null) {
					subscription.destroy();
				}
				subscription = new WsEventListener(this, nodes);
				resp.sendResult("ok");
				break;

			case "command":
				String cmd = message.get("cmd");
				if (cmd == null) {
					resp.sendError("Attribute 'cmd' not found");
					return;
				}
				String[] cmd_prm = cmd.split("=");
				Object res = null;
				try {
					String param = cmd_prm.length == 1 ? null : cmd_prm[1];
					res = ScriptsEngine.executeDriverAction(cmd_prm[0], param,
							httpRequest.getRemoteUser());
				} catch (Exception e) {
					resp.sendError(e.getMessage());
				}
				resp.sendResult(res);
				break;

			case "event":
				String id = message.get("id");
				if (id == null) {
					resp.sendError("Attribute 'id' not found");
					return;
				}
				String value = message.get("value");
				try {
					HttpApiEvent remoteEvent = new HttpApiEvent(id, value, httpRequest, resp);
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
			logger.warn("Error processing WebSocket message", e);
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
		if (pingTask != null) {
			pingTask.interrupt();
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
	synchronized void send(String text) throws IOException {
		logger.debug("Sending: '{}' - Host: {}", text, httpRequest.getRemoteHost());
		RemoteEndpoint remote = getRemote();
		if (remote != null) {
			remote.sendString(text);
		}
	}

}
