package cc.sferalabs.sfera.http.api.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.http.api.rest.ApiServlet;

@SuppressWarnings("serial")
public class ApiWebSocketServlet extends WebSocketServlet {

	public static final String PATH = ApiServlet.PATH + "websocket";

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.setCreator(new ApiSocketCreator());
		long pingInterval = SystemNode.getConfiguration().get("http_ws_ping_interval", 10000l);
		long pongTimeout = SystemNode.getConfiguration().get("http_ws_pong_timeout", 5000l);
		factory.getPolicy().setIdleTimeout(pingInterval + pongTimeout + 200);
	}

}
