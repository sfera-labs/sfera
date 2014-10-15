package com.homesystemsconsulting.drivers.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.homesystemsconsulting.core.Configuration;
import com.homesystemsconsulting.core.Sfera;
import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.drivers.webserver.access.Access;
import com.homesystemsconsulting.drivers.webserver.access.Token;
import com.homesystemsconsulting.drivers.webserver.util.ResourcesUtil;
import com.homesystemsconsulting.util.logging.SystemLogger;

public abstract class WebServer extends Driver {
	
	static final Path ROOT = Paths.get("webapp/");
	static final String HTTP_HEADER_FIELD_SERVER = "Sfera " + Sfera.VERSION;
	static final String API_BASE_URI = "/x/";
	
	private static boolean initialized = false;
	private static final Object initLock = new Object();
	
	private static SystemLogger log;
	
	private ServerSocket socket;
	
	/**
	 * 
	 */
	protected WebServer(String id) {
		super(id);
	}

	@Override
	protected boolean onInit(Configuration configuration) throws InterruptedException {
		synchronized (initLock) {
			if (!initialized) {
				log = super.log;
				
				ConnectionHandler.init(configuration);
				try {
					ResourcesUtil.lookForPluginsOverwritingWebapp();
				} catch (IOException e) {
					log.error("error scanning plugins directory: " + e);
				}
				try {
					InterfaceCache.init(configuration);
				} catch (Exception e) {
					log.error("error creating cache: " + e);
				}
				try {
					Access.init();
				} catch (Exception e) {
					log.error("error initializing access: " + e);
					return false;
				}
				
				Token.maxAgeSeconds = configuration.getIntProperty("password_validity_minutes", 60) * 60;
				
				initialized = true;
			}			
		}
		
		Integer port = configuration.getIntProperty(getProtocolName() + ".port", getDefaultPort());
		try {
			socket = getServerSocket(port, configuration);
		} catch (Exception e) {
			log.error("error instantiating socket: " + e);
			return false;
		}
		
		log.info("accepting connections on port " + socket.getLocalPort());
		
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public static SystemLogger getLogger() {
		return log;
	}

	/**
	 * 
	 * @return
	 */
	protected abstract int getDefaultPort();
	
	/**
	 * 
	 * @return
	 */
	protected abstract String getProtocolName();

	/**
	 * 
	 * @param port
	 * @param configuration
	 * @return
	 * @throws Exception
	 */
	protected abstract ServerSocket getServerSocket(int port, Configuration configuration) throws Exception;

	@Override
	protected boolean loop() throws InterruptedException {
		Socket connection = null;
		try {
			connection = socket.accept();
			log.debug("accepted connection from: " + connection.getInetAddress());
			new ConnectionHandler(getId(), connection, getProtocolName());
			
		} catch (IOException e) {
			log.error("error accepting connection: " + e);
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException ioe) {}
			}
			
			return false;
		}
		
		return true;
	}

	@Override
	protected void onQuit() throws InterruptedException {
		try {
			socket.close();
		} catch (Exception e) {}
		ConnectionHandler.quit();
	}
}
