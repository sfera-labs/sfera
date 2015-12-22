package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.UpgradeHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.CommandExecutor;
import cc.sferalabs.sfera.http.api.JsonMessage;
import cc.sferalabs.sfera.http.api.RemoteApiEvent;

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
	private static final AtomicLong count = new AtomicLong(77);

	private static final String PING_STRING = "&";

	final String hostname;
	private final HttpServletRequest originalRequest;
	private final String connectionId;
	private final String user;
	private final boolean isAuthorized;

	private WsEventListener nodesSubscription;
	private WsFileWatcher filesSubscription;
	private final Task pingTask;
	private final long pingInterval;
	private final long respTimeout;

	/**
	 * Construct an ApiSocket
	 * 
	 * @param request
	 *            the request details
	 * @param pingInterval
	 *            the ping messages interval
	 * @param respTimeout
	 *            the responses timeout
	 */
	ApiSocket(ServletUpgradeRequest request, long pingInterval, long respTimeout) {
		this.originalRequest = ((UpgradeHttpServletRequest) request.getHttpServletRequest())
				.getHttpServletRequest();
		this.hostname = request.getRemoteHostName();
		String connectionId = originalRequest.getParameter("connectionId");
		String sessionId = originalRequest.getSession().getId();
		if (connectionId != null) {
			if (!sessionId.equals(connectionId.split("-")[0])) {
				connectionId = null;
			}
		}
		if (connectionId == null) {
			connectionId = sessionId + "-" + count.getAndIncrement();
		}
		this.connectionId = connectionId;
		this.user = this.originalRequest.getRemoteUser();
		this.pingInterval = pingInterval;
		this.respTimeout = respTimeout;
		this.pingTask = new PingTask(this, pingInterval);
		this.isAuthorized = request.isUserInRole("admin") || request.isUserInRole("user");
		logger.debug("Socket created - Host: {}", request.getRemoteHostName());
	}

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		try {
			if (isAuthorized) {
				OutgoingWsMessage resp = new OutgoingWsMessage("connection", this);
				resp.put("connectionId", connectionId);
				resp.put("pingInterval", pingInterval);
				resp.put("responseTimeout", respTimeout);
				resp.send();
				ping();
				logger.debug("Socket connected - Host: {}", hostname);
			} else {
				logger.warn("Unauthorized WebSocket connection from {}", hostname);
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
		logger.debug("Received message: '{}' - Host: {} User: {}", message, hostname, user);

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
		OutgoingWsMessage reply = new OutgoingWsMessage("reply", this);
		try {
			Object tag = message.get("tag");
			if (tag == null) {
				reply.sendError("Attribute 'tag' not found");
				return;
			}
			String action = message.get("action");
			if (action == null) {
				reply.sendError("Attribute 'action' not found");
				return;
			}
			reply.put("tag", tag);
			reply.put("action", action);

			switch (action) {
			case "subscribe":
				String nodes = message.get("nodes");
				String files = message.get("files");
				boolean ok = false;
				if (nodes != null) {
					if (nodesSubscription != null) {
						nodesSubscription.destroy();
					}
					nodesSubscription = new WsEventListener(this, nodes, connectionId);
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
					reply.sendResult("ok");
				} else {
					reply.sendError("Missing attributes");
				}
				break;

			case "command":
				String cmd = message.get("cmd");
				if (cmd == null) {
					reply.sendError("Attribute 'cmd' not found");
					return;
				}
				Object res = null;
				try {
					res = CommandExecutor.exec(cmd, originalRequest, connectionId, user);
				} catch (Exception e) {
					reply.sendError(e.getMessage());
					return;
				}
				reply.sendResult(res);
				break;

			case "event":
				String id = message.get("id");
				if (id == null) {
					reply.sendError("Attribute 'id' not found");
					return;
				}
				String value = message.get("value");
				try {
					RemoteApiEvent remoteEvent = new RemoteApiEvent(id, value, originalRequest,
							connectionId);
					Bus.post(remoteEvent);
					reply.sendResult("ok");
				} catch (Exception e) {
					reply.sendError(e.getMessage());
				}
				break;

			default:
				reply.sendError("Unknown action");
				break;
			}
		} catch (IOException e) {
			logger.warn("Error processing WebSocket message", e);
			reply.sendError("Server error: " + e.getMessage());
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
		logger.debug("Socket Closed: [{}] {} - Host: {}", statusCode, reason, hostname);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		logger.warn("WebSocket error - Host: " + hostname, cause);
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
	 *            the message to be sent
	 * @throws IOException
	 *             if unable to send the text message
	 */
	synchronized void send(String text) throws IOException {
		RemoteEndpoint remote = getRemote();
		if (remote != null) {
			logger.debug("Sending: '{}' - Host: {}", text, hostname);
			remote.sendString(text);
		}
	}

}
