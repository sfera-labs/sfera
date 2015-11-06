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
	private WsEventListener nodesSubscription;
	private WsFileWatcher filesSubscription;
	private final Task pingTask;
	private final long pingInterval;
	private final long respTimeout;

	/**
	 * 
	 * @param request
	 * @param pingInterval
	 * @param respTimeout
	 */
	ApiSocket(ServletUpgradeRequest request, long pingInterval, long respTimeout) {
		this.httpRequest = request.getHttpServletRequest();
		this.pingInterval = pingInterval;
		this.respTimeout = respTimeout;
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
			String host = httpRequest.getRemoteHost();
			if (isAuthorized(httpRequest)) {
				OutgoingWsMessage resp = new OutgoingWsMessage("connection", this);
				resp.put("pingInterval", pingInterval);
				resp.put("responseTimeout", respTimeout);
				resp.send();
				ping();
				logger.debug("Socket connected - Host: {}", host);
			} else {
				logger.warn("Unauthorized WebSocket connection from {}", host);
				OutgoingWsMessage resp = new OutgoingWsMessage("connection", this);
				resp.sendError("Unauthorized");
				Thread.sleep(respTimeout);
				closeSocket(StatusCode.POLICY_VIOLATION, "Unauthorized");
			}
		} catch (Exception e) {
			onWebSocketError(new Exception("Connection error", e));
		}

	}

	/**
	 * @param req
	 * @return
	 */
	private boolean isAuthorized(HttpServletRequest req) {
		return req.isUserInRole("admin") || req.isUserInRole("user");
	}

	/**
	 * 
	 */
	void ping() {
		try {
			send(PING_STRING);
		} catch (Exception e) {
			onWebSocketError(e);
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
				String files = message.get("files");
				boolean ok = false;
				if (nodes != null) {
					if (nodesSubscription != null) {
						nodesSubscription.destroy();
					}
					nodesSubscription = new WsEventListener(this, nodes);
					ok = true;
				}
				if (files != null) {
					if (filesSubscription != null) {
						filesSubscription.destroy();
					}
					filesSubscription = new WsFileWatcher(this, files);
					ok = true;
				}
				if (ok) {
					resp.sendResult("ok");
				} else {
					resp.sendError("Missing attributes");
				}
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
		if (nodesSubscription != null) {
			nodesSubscription.destroy();
			nodesSubscription = null;
		}
		if (filesSubscription != null) {
			filesSubscription.destroy();
			filesSubscription = null;
		}
		if (pingTask != null) {
			pingTask.interrupt();
		}
		logger.debug("Socket Closed: [{}] {} - Host: {}", statusCode, reason,
				httpRequest.getRemoteHost());
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		logger.warn("WebSocket error - Host: " + httpRequest.getRemoteHost(), cause);
		closeSocket(StatusCode.PROTOCOL, cause.getMessage());
	}

	/**
	 * 
	 * @param statusCode
	 * @param reason
	 */
	private void closeSocket(int statusCode, String reason) {
		Session session = getSession();
		if (session != null) {
			session.close(statusCode, reason);
		}
	}

	/**
	 * @param text
	 * @throws IOException
	 */
	synchronized void send(String text) throws IOException {
		RemoteEndpoint remote = getRemote();
		if (remote != null) {
			logger.debug("Sending: '{}' - Host: {}", text, httpRequest.getRemoteHost());
			remote.sendString(text);
		}
	}

}
