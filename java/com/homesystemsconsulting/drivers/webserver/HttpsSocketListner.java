package com.homesystemsconsulting.drivers.webserver;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.concurrent.ArrayBlockingQueue;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class HttpsSocketListner extends SocketListner {

	public HttpsSocketListner(WebServer webServer, int port,
			ArrayBlockingQueue<Connection> connectionsQ, String sslPassword)
			throws Exception {
		super(webServer, "https", connectionsQ);
		SSLContext context = SSLContext.getInstance("SSLv3");
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		KeyStore ks = KeyStore.getInstance("JKS");
		char[] kspwd = sslPassword.toCharArray();
		ks.load(new FileInputStream("sfera.keys"), kspwd);
		kmf.init(ks, kspwd);
		context.init(kmf.getKeyManagers(), null, null);
		SSLServerSocketFactory ssf = context.getServerSocketFactory();
		this.serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
	}
}