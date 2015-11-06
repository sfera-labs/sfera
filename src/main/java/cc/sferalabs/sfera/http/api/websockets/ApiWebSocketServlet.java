package cc.sferalabs.sfera.http.api.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.http.api.rest.ApiServlet;

/**
 * Implementation of {@link WebSocketServlet} for handling of API requests.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class ApiWebSocketServlet extends WebSocketServlet {

	public static final String PATH = ApiServlet.PATH + "websocket";

	@Override
	public void configure(WebSocketServletFactory factory) {
		long pingInterval = SystemNode.getConfiguration().get("ws_ping_interval", 10000l);
		long respTimeout = SystemNode.getConfiguration().get("ws_response_timeout", 5000l);
		factory.setCreator(new ApiSocketCreator(pingInterval, respTimeout));
		factory.getPolicy().setIdleTimeout(pingInterval + respTimeout);
	}

}
