package cc.sferalabs.sfera.drivers.webserver;

import java.net.Socket;
import java.net.SocketException;

public class Connection {
	
	private static final int SOCKET_TIMEOUT = 60000;

	private final Socket socket;
	private final String protocol;
	
	public Connection(Socket socket, String protocol) throws SocketException {
		this.socket = socket;
		this.socket.setSoTimeout(SOCKET_TIMEOUT);
		this.protocol = protocol;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getProtocol() {
		return protocol;
	}

}
