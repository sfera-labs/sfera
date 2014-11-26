package com.homesystemsconsulting.drivers.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import com.homesystemsconsulting.core.Task;

public abstract class SocketListner extends Task {

	private final WebServer webServer;
	private final ArrayBlockingQueue<Connection> connectionsQ;
	private final String protocolName;
	protected ServerSocket serverSocket;

	public SocketListner(WebServer webServer, String protocolName,
			ArrayBlockingQueue<Connection> connectionsQ) throws Exception {
		super(webServer.getId() + ":SocketListner:" + protocolName);
		this.webServer = webServer;
		this.protocolName = protocolName;
		this.connectionsQ = connectionsQ;
	}

	@Override
	public void execute() {
		webServer.getLogger().info(
				"accepting connections on port " + serverSocket.getLocalPort());
		Socket s = null;
		try {
			while (true) {
				s = serverSocket.accept();
				Connection c = new Connection(s, protocolName);
				if (!connectionsQ.offer(c)) {
					webServer.getLogger().warning("too many connections");
				}
			}
		} catch (IOException e) {
			webServer.getLogger().error("error accepting connection: " + e);
			if (s != null) {
				try {
					s.close();
				} catch (IOException ioe) {
				}
			}

			webServer.quit();
		}
	}

	public void close() {
		try {
			serverSocket.close();
		} catch (Exception e) {
		}
	}

}
