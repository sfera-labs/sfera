package com.homesystemsconsulting.drivers.webserver;

import java.io.IOException;
import java.net.ServerSocket;

import com.homesystemsconsulting.core.Configuration;

public class HttpServer extends WebServer {

	public HttpServer(String id) {
		super(id);
	}
	
	@Override
	protected int getDefaultPort() {
		return 80;
	}

	@Override
	protected ServerSocket getServerSocket(int port, Configuration configuration) throws IOException {
		return new ServerSocket(port);
	}

	@Override
	protected String getProtocolName() {
		return "http";
	}
	
}
