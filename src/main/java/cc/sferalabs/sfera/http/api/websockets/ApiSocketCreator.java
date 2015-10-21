package cc.sferalabs.sfera.http.api.websockets;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ApiSocketCreator implements WebSocketCreator {

	private final static Logger logger = LoggerFactory.getLogger(ApiSocketCreator.class);

	@Override
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
		if (isAuthorized(req)) {
			return new ApiSocket(req);
		} else {
			logger.warn("Unauthorized WebSocket upgrade request from {}", req.getRemoteHostName());
			return null;
		}
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private boolean isAuthorized(ServletUpgradeRequest req) {
		return req.isUserInRole("admin") || req.isUserInRole("user");
	}

}
