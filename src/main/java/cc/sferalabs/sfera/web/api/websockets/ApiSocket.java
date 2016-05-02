package cc.sferalabs.sfera.web.api.websockets;

import java.io.IOException;
import java.util.EventListener;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.UpgradeHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.AccessChangeEvent;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.web.api.CommandExecutor;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.JsonMessage;
import cc.sferalabs.sfera.web.api.WebApiEvent;

/**
 * {@link WebSocketAdapter} to process API requests
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ApiSocket extends WebSocketAdapter implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(ApiSocket.class);
	private static final AtomicLong count = new AtomicLong(77);

	private static final String PING_STRING = "&";

	private final HttpServletRequest originalRequest;
	final String hostname;
	final String connectionId;
	final String user;

	private WsEventListener nodesSubscription;
	private WsFileWatcher filesSubscription;
	private final Task pingTask;
	private Future<?> pingTaskFuture;
	private final long pingInterval;
	private final long respTimeout;
	private WsConsoleSession consoleSession;

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
		logger.debug("Socket created - Host: {}", request.getRemoteHostName());
	}

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		try {
			if (isUserInRole("admin", "user")) {
				OutgoingWsMessage resp = new OutgoingWsMessage("connection", this);
				resp.put("connectionId", connectionId);
				resp.put("pingInterval", pingInterval);
				resp.put("responseTimeout", respTimeout);
				resp.send();
				ping();
				Bus.register(this);
				logger.debug("Socket connected - Host: {}", hostname);
			} else {
				logger.warn("Unauthorized WebSocket connection from {}", hostname);
				closeSocket(StatusCode.POLICY_VIOLATION, "Unauthorized");
			}
		} catch (Exception e) {
			onWebSocketError(new Exception("Connection error", e));
		}
	}

	@Subscribe
	public void checkUser(AccessChangeEvent e) {
		if (!isUserInRole("admin", "user")) {
			closeSocket(StatusCode.POLICY_VIOLATION, "Unauthorized");
		}
	}

	/**
	 * 
	 * @param roles
	 * @return
	 */
	private boolean isUserInRole(String... roles) {
		if (user == null) {
			return false;
		}
		User u = Access.getUser(user);
		if (u == null) {
			return false;
		}
		for (String role : roles) {
			if (u.isInRole(role)) {
				return true;
			}
		}
		return false;
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

		if (!isUserInRole("admin", "user")) {
			closeSocket(StatusCode.POLICY_VIOLATION, "Unauthorized");
			return;
		}

		if (message.equals(PING_STRING)) {
			pingTaskFuture = TasksManager.submit(pingTask);
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
				reply.sendErrors(new ErrorMessage(0, "Attribute 'tag' not found"));
				return;
			}
			String action = message.get("action");
			if (action == null) {
				reply.sendErrors(new ErrorMessage(0, "Attribute 'action' not found"));
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
					reply.sendErrors(new ErrorMessage(0, "Missing attributes"));
				}
				break;

			case "command":
				String cmd = message.get("cmd");
				if (cmd == null) {
					reply.sendErrors(new ErrorMessage(0, "Attribute 'cmd' not found"));
					return;
				}
				Object res = null;
				try {
					res = CommandExecutor.exec(cmd, originalRequest, connectionId, user);
				} catch (IllegalArgumentException | ScriptException e) {
					reply.sendErrors(new ErrorMessage(0, e.getMessage()));
					return;
				}
				reply.sendResult(res);
				break;

			case "event":
				String id = message.get("id");
				if (id == null) {
					reply.sendErrors(new ErrorMessage(0, "Attribute 'id' not found"));
					return;
				}
				String value = message.get("value");
				try {
					WebApiEvent remoteEvent = new WebApiEvent(id, value, originalRequest,
							connectionId);
					Bus.post(remoteEvent);
					reply.sendResult("ok");
				} catch (Exception e) {
					reply.sendErrors(new ErrorMessage(0, e.getMessage()));
				}
				break;

			case "console":
				if (!isUserInRole("admin")) {
					closeSocket(StatusCode.POLICY_VIOLATION, "Unauthorized");
					return;
				}
				String command = message.get("cmd");
				if (command == null) {
					reply.sendErrors(new ErrorMessage(0, "Attribute 'cmd' not found"));
					return;
				}
				if (consoleSession == null) {
					consoleSession = new WsConsoleSession(this);
				}
				if ("exit".equals(command)) {
					consoleSession.quit();
				} else {
					if (!consoleSession.isActive()) {
						consoleSession.start();
					}
					consoleSession.process(command);
				}
				break;

			default:
				reply.sendErrors(new ErrorMessage(0, "Unknown action"));
				break;
			}
		} catch (Exception e) {
			logger.warn("Error processing WebSocket message", e);
			reply.sendErrors(new ErrorMessage(0, "Server error: " + e.getMessage()));
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
		if (pingTaskFuture != null) {
			pingTaskFuture.cancel(true);
		}
		if (consoleSession != null) {
			consoleSession.quit();
		}
		Bus.unregister(this);
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
