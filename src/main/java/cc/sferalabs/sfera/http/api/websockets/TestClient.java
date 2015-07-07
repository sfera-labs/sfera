package cc.sferalabs.sfera.http.api.websockets;

import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class TestClient {

	public static void main(String[] args) {
		URI uri = URI.create("ws://localhost:8080" + ApiWebSocketServlet.PATH);
		//URI uri = URI.create("wss://localhost:2443" + ApiWebSocketServlet.PATH);
		WebSocketClient client = new WebSocketClient();
		try {
			client.start();
			ApiSocket socket = new ApiSocket();
			
			Future<Session> fut = client.connect(socket, uri);
			Session session = fut.get();
			session.getRemote().sendString("Hello 1");
			session.close();
			
			Thread.sleep(3000);
			
			fut = client.connect(socket, uri);
			session = fut.get();
			session.getRemote().sendString("Hello 2");
			session.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
