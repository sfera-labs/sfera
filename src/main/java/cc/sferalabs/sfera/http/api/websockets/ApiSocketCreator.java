package cc.sferalabs.sfera.http.api.websockets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class ApiSocketCreator implements WebSocketCreator {

	private final static Logger logger = LogManager.getLogger();

	@Override
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
		if (isAuthorized(req)) {
			return new ApiSocket(req);
		} else {
			logger.warn("Unauthorized websocket upgrade request from {}", req.getRemoteHostName());
			return null;
		}
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private boolean isAuthorized(ServletUpgradeRequest req) {
		return req.isUserInRole("admin") || req.isUserInRole("api");
	}

}
