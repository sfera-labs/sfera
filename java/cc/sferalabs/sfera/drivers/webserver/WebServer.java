package cc.sferalabs.sfera.drivers.webserver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.Sfera;
import cc.sferalabs.sfera.core.TasksManager;
import cc.sferalabs.sfera.drivers.Driver;
import cc.sferalabs.sfera.drivers.webserver.access.Access;
import cc.sferalabs.sfera.drivers.webserver.access.Token;
import cc.sferalabs.sfera.drivers.webserver.util.ResourcesUtil;
import cc.sferalabs.sfera.util.logging.SystemLogger;

public class WebServer extends Driver {

	static final Path ROOT = Paths.get("webapp/");
	static final String HTTP_HEADER_FIELD_SERVER = "Sfera " + Sfera.VERSION;
	static final String API_BASE_URI = "/x/";

	private static boolean initialized = false;
	private static final Object initLock = new Object();

	private ArrayBlockingQueue<Connection> connectionsQ;

	private List<SocketListner> socketListners;

	/**
	 * 
	 */
	public WebServer(String id) {
		super(id);
	}

	@Override
	protected boolean onInit(Configuration configuration)
			throws InterruptedException {
		synchronized (initLock) {
			if (!initialized) {
				ConnectionHandler.init(configuration);
				try {
					ResourcesUtil.lookForPluginsOverwritingWebapp();
				} catch (IOException e) {
					log.error("error scanning plugins directory: " + e);
				}
				try {
					InterfaceCache.init(this, configuration);
				} catch (Exception e) {
					log.error("error creating cache: " + e);
				}
				try {
					Access.init(this);
				} catch (Exception e) {
					log.error("error initializing access: " + e);
					return false;
				}

				Token.maxAgeSeconds = configuration.getIntProperty(
						"password_validity_minutes", 60) * 60;

				initialized = true;
			}
		}

		connectionsQ = new ArrayBlockingQueue<>(
				ConnectionHandler.getMaxRequestThreads() + 50);

		Integer http_port = configuration.getIntProperty("http_port", null);
		Integer https_port = configuration.getIntProperty("https_port", null);

		try {
			socketListners = new ArrayList<SocketListner>();
			if (http_port != null) {
				socketListners.add(new HttpSocketListner(this, http_port,
						connectionsQ));
			}
			if (https_port != null) {
				String sslPassword = configuration.getProperty("ssl_password",
						"sferapass");
				socketListners.add(new HttpsSocketListner(this, https_port,
						connectionsQ, sslPassword));
			}
		} catch (Exception e) {
			log.error("error instantiating socket: " + e);
			return false;
		}

		for (SocketListner sl : socketListners) {
			TasksManager.DEFAULT.execute(sl);
		}

		return true;
	}

	@Override
	protected boolean loop() throws InterruptedException {
		Connection c = connectionsQ.take();
		log.debug("accepted connection from: "
				+ c.getSocket().getRemoteSocketAddress());
		new ConnectionHandler(this, c);

		return true;
	}

	@Override
	protected void onQuit() throws InterruptedException {
		for (SocketListner sl : socketListners) {
			sl.close();
		}
		ConnectionHandler.quit();
	}

	/**
	 * 
	 * @return
	 */
	public SystemLogger getLogger() {
		return log;
	}
}
