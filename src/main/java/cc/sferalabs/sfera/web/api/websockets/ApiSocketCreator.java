package cc.sferalabs.sfera.web.api.websockets;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ApiSocketCreator implements WebSocketCreator {

	private final long pingInterval;
	private final long respTimeout;

	/**
	 * @param pingInterval
	 *            the ping messages interval
	 * @param respTimeout
	 *            the responses timeout
	 */
	ApiSocketCreator(long pingInterval, long respTimeout) {
		this.pingInterval = pingInterval;
		this.respTimeout = respTimeout;
	}

	@Override
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
		return new ApiSocket(req, pingInterval, respTimeout);
	}

}