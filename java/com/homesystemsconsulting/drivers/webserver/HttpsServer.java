package com.homesystemsconsulting.drivers.webserver;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class HttpsServer extends WebServer {

	public HttpsServer(String id) {
		super();
	}

	@Override
	protected int getDefaultPort() {
		return 443;
	}

	@Override
	protected ServerSocket getServerSocket(int port) throws Exception {
		SSLContext context = SSLContext.getInstance("SSLv3");
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		KeyStore ks = KeyStore.getInstance("JKS");
		//TODO get password from config
		char[] kspwd = "hsycopass".toCharArray();
		ks.load(new FileInputStream("sfera.keys"), kspwd);
		kmf.init(ks, kspwd);
		context.init(kmf.getKeyManagers(), null, null);
		SSLServerSocketFactory ssf = context.getServerSocketFactory();
		SSLServerSocket ssocket = (SSLServerSocket) ssf.createServerSocket(port);
		return ssocket;
	}
	
	@Override
	protected String getProtocolName() {
		return "https";
	}

}
