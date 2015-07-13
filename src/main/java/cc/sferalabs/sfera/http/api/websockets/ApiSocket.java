package cc.sferalabs.sfera.http.api.websockets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class ApiSocket extends WebSocketAdapter {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		logger.debug("Socket Connected");
	}

	@Override
	public void onWebSocketText(String message) {
		try {
			super.onWebSocketText(message);
			logger.debug("Received message: {}", message);
			// TODO
			getRemote().sendString("ciao!");

		} catch (Exception e) {
			logger.warn("Error processing message", e);
		}
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		logger.debug("Socket Closed: {} - {}", statusCode, reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		logger.warn("WebSocket error", cause);
	}
}
