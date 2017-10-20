/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.web.api.websockets;

import java.io.IOException;
import java.util.EventListener;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	private final HttpSession session;
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
		this.originalRequest = ((UpgradeHttpServletRequest) request.getHttpServletRequest()).getHttpServletRequest();
		this.hostname = request.getRemoteHostName();
		String connectionId = originalRequest.getParameter("connectionId");
		session = originalRequest.getSession();
		String sessionId = session.getId();
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
			if (isAuthorized(false)) {
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
		if (!isAuthorized(false)) {
			closeSocket(StatusCode.POLICY_VIOLATION, "Unauthorized");
		}
	}

	/**
	 * 
	 * @param needAdmin
	 * @return
	 */
	private boolean isAuthorized(boolean needAdmin) {
		if (user == null) {
			return false;
		}
		User u = Access.getUser(user);
		if (u == null) {
			return false;
		}
		try {
			// Try to get an attribute from session to check if it is still
			// valid
			session.getAttribute("");
		} catch (IllegalStateException e) {
			return false;
		}
		if (needAdmin && !u.isInRole("admin")) {
			return false;
		}
		return true;
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

		if (!isAuthorized(false)) {
			closeSocket(StatusCode.POLICY_VIOLATION, "Unauthorized");
			return;
		}

		if (message.equals(PING_STRING)) {
			pingTaskFuture = TasksManager.submit(pingTask);
			return;
		}

		JsonMessage m = new JsonMessage(message);
		process(m);
	}

	/**
	 * Process the specified incoming message and sends a response.
	 * 
	 * @param message
	 *            the message to process
	 */
	private void process(JsonMessage message) {
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
					nodesSubscription.sendCurrentSate();
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
				String value = message.get("value").toString();
				try {
					WebApiEvent remoteEvent = new WebApiEvent(id, value, originalRequest, connectionId);
					Bus.post(remoteEvent);
					reply.sendResult("ok");
				} catch (Exception e) {
					reply.sendErrors(new ErrorMessage(0, e.getMessage()));
				}
				break;

			case "console":
				if (!isAuthorized(true)) {
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
		} catch (Throwable e) {
			logger.warn("Error processing WebSocket message '" + message + "'", e);
			try {
				reply.sendErrors(new ErrorMessage(0, "Server error: " + e.getMessage()));
			} catch (Exception e1) {
			}
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
	void send(String text) throws IOException {
		if (text == null) {
			throw new NullPointerException("null text");
		}
		RemoteEndpoint remote = getRemote();
		if (remote != null) {
			// FIXME this log creates a recursion when using
			// the "log start -l debug" console command via WebSockets
			logger.debug("Sending: '{}' - Host: {}", text, hostname);
			remote.sendStringByFuture(text);
		}
	}

}
