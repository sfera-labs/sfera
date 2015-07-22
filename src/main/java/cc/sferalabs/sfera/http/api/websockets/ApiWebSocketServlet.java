package cc.sferalabs.sfera.http.api.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import cc.sferalabs.sfera.http.api.rest.ApiServlet;

@SuppressWarnings("serial")
public class ApiWebSocketServlet extends WebSocketServlet {

	public static final String PATH = ApiServlet.PATH + "websocket";
	private static final long IDLE_TIMEOUT = 30000;

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.setCreator(new ApiSocketCreator());
		factory.getPolicy().setIdleTimeout(IDLE_TIMEOUT);
	}

}
