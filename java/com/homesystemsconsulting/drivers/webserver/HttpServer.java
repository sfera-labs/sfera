package com.homesystemsconsulting.drivers.webserver;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer extends WebServer {

	public HttpServer(String id) {
		super();
	}
	
	@Override
	protected int getDefaultPort() {
		return 80;
	}

	@Override
	protected ServerSocket getServerSocket(int port) throws IOException {
		return new ServerSocket(port);
	}

	@Override
	protected String getProtocolName() {
		return "http";
	}
	
}
