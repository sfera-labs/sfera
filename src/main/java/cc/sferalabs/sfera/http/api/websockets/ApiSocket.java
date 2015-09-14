package cc.sferalabs.sfera.http.api.websockets;

import java.security.Principal;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiSocket extends WebSocketAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ApiSocket.class);
	ServletUpgradeRequest request;
	Principal user;
	WsEventListener subscription;

	/**
	 * 
	 * @param request
	 */
	public ApiSocket(ServletUpgradeRequest request) {
		this.request = request;
		this.user = request.getUserPrincipal();
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
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		logger.debug("Received message: {} - User: {}", message, user.getName());
		Message m = new Message(message);
		m.process(this);
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
