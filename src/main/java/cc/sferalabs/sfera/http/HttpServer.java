package cc.sferalabs.sfera.http;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.http.api.rest.CommandServlet;
import cc.sferalabs.sfera.http.api.rest.EventServlet;
import cc.sferalabs.sfera.http.api.rest.LoginServlet;
import cc.sferalabs.sfera.http.api.rest.LogoutServlet;
import cc.sferalabs.sfera.http.api.rest.StateServlet;
import cc.sferalabs.sfera.http.api.rest.SubscribeServlet;
import cc.sferalabs.sfera.http.api.rest.admin.EditFileServlet;
import cc.sferalabs.sfera.http.api.rest.admin.GetFileServlet;
import cc.sferalabs.sfera.http.api.websockets.ApiWebSocketServlet;
import cc.sferalabs.sfera.http.auth.AuthenticationFilter;
import cc.sferalabs.sfera.http.auth.AuthenticationRequestWrapper;

/**
 * HTTP Server
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpServer implements AutoStartService {

	private static final String KEYSTORE_PATH = "data/http/sfera.keys";
	private static final String SESSIONS_STORE_DIR = "data/http/sessions";

	private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
	private static final String[] EXCLUDED_PROTOCOLS = { "SSL", "SSLv2", "SSLv2Hello", "SSLv3" };
	private static final String[] EXCLUDED_CIPHER_SUITES = { ".*NULL.*", ".*RC4.*", ".*MD5.*",
			".*DES.*", ".*DSS.*" };

	private static Server server;
	private static ServletContextHandler contexts;

	@Override
	public void init() throws Exception {
		Configuration config = SystemNode.getConfiguration();
		Integer http_port = null;
		Integer https_port = null;
		if (config != null) {
			http_port = config.get("http_port", null);
			https_port = config.get("https_port", null);
		}

		if (http_port == null && https_port == null) {
			logger.warn("No HTTP port defined in configuration. Server disabled");
			return;
		}

		int maxThreads = config.get("http_max_threads",
				Runtime.getRuntime().availableProcessors() * 128);
		int minThreads = config.get("http_min_threads", 8);
		int idleTimeout = config.get("http_threads_idle_timeout", 60000);

		QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

		server = new Server(threadPool);

		if (http_port != null) {
			ServerConnector http = new ServerConnector(server);
			http.setName("http");
			http.setPort(http_port);
			server.addConnector(http);
			logger.info("Starting HTTP server on port {}", http_port);
		}

		if (https_port != null) {
			String keyStorePassword = config.get("keystore_password", null);
			if (keyStorePassword == null) {
				throw new Exception("'keystore_password' not specified in configuration");
			}
			Path keystorePath = Paths.get(KEYSTORE_PATH);
			if (!Files.exists(keystorePath)) {
				throw new NoSuchFileException(KEYSTORE_PATH);
			}

			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStorePath(KEYSTORE_PATH);
			sslContextFactory.setKeyStorePassword(keyStorePassword);
			String keyManagerPassword = config.get("keymanager_password", null);
			if (keyManagerPassword != null) {
				sslContextFactory.setKeyManagerPassword(keyManagerPassword);
			}
			sslContextFactory.addExcludeProtocols(EXCLUDED_PROTOCOLS);
			sslContextFactory.setExcludeCipherSuites(EXCLUDED_CIPHER_SUITES);
			sslContextFactory.setRenegotiationAllowed(false);
			sslContextFactory.setUseCipherSuitesOrder(false);

			HttpConfiguration https_config = new HttpConfiguration();
			https_config.setSecurePort(https_port);
			https_config.addCustomizer(new SecureRequestCustomizer());

			ServerConnector https = new ServerConnector(server,
					new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
					new HttpConnectionFactory(https_config));
			https.setName("https");
			https.setPort(https_port);

			server.addConnector(https);
			logger.info("Starting HTTPS server on port {}", https_port);
		}

		HashLoginService loginService = new HashLoginService();
		server.addBean(loginService);
		AuthenticationRequestWrapper.setLoginService(loginService);
		addUsers(loginService);

		HashSessionManager hsm = new HashSessionManager();
		hsm.setStoreDirectory(new File(SESSIONS_STORE_DIR));
		// TODO try to make session restorable when server not stopped properly
		hsm.setSessionCookie("session");
		int maxInactiveInterval = config.get("http_session_max_inactive", 3600);
		hsm.setMaxInactiveInterval(maxInactiveInterval);
		SessionHandler sessionHandler = new SessionHandler(hsm);
		sessionHandler.addEventListener(new HttpSessionListener() {

			@Override
			public void sessionCreated(HttpSessionEvent se) {
				logger.debug("Creted new session: {}", se.getSession().getId());
			}

			@Override
			public void sessionDestroyed(HttpSessionEvent se) {
				logger.debug("Session '{}' destroyed", se.getSession().getId());
			}
		});

		contexts = new ServletContextHandler(server, "/",
				ServletContextHandler.SESSIONS | ServletContextHandler.SECURITY);
		contexts.setSessionHandler(sessionHandler);
		contexts.addFilter(AuthenticationFilter.class, "/*", null);
		contexts.addServlet(DefaultErrorServlet.class, "/*");

		registerApiServlets();

		try {
			server.start();
		} catch (Exception e) {
			throw new Exception("Error starting server: " + e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 
	 * @param loginService
	 */
	private void addUsers(HashLoginService loginService) {
		for (User user : Access.getUsers()) {
			loginService.putUser(user.getUsername(), new Credential() {

				private static final long serialVersionUID = 3107948362308619263L;

				@Override
				public boolean check(Object credentials) {
					return Access.authenticate(user.getUsername(), (String) credentials);
				}
			}, user.getRoles());
		}
	}

	/**
	 * 
	 * @throws HttpServerException
	 */
	private void registerApiServlets() throws HttpServerException {
		addServlet(LoginServlet.class, LoginServlet.PATH);
		addServlet(LogoutServlet.class, LogoutServlet.PATH);
		addServlet(SubscribeServlet.class, SubscribeServlet.PATH);
		addServlet(StateServlet.class, StateServlet.PATH);
		addServlet(CommandServlet.class, CommandServlet.PATH);
		addServlet(EventServlet.class, EventServlet.PATH);
		addServlet(ApiWebSocketServlet.class, ApiWebSocketServlet.PATH);
		addServlet(EditFileServlet.class, EditFileServlet.PATH);
		addServlet(GetFileServlet.class, GetFileServlet.PATH);
	}

	/**
	 * Registers the specified servlet class to handle the requests on paths
	 * matching the specified path spec
	 * 
	 * @param servlet
	 *            the servlet class
	 * @param pathSpec
	 *            the path spec to map servlet to
	 * @throws HttpServerException
	 *             if an error occurs
	 */
	public synchronized static void addServlet(Class<? extends Servlet> servlet, String pathSpec)
			throws HttpServerException {
		addServlet((Object) servlet, pathSpec);
	}

	/**
	 * Registers the servlet contained by the servlet holder to handle the
	 * requests on paths matching the specified path spec
	 * 
	 * @param servlet
	 *            the servlet holder
	 * @param pathSpec
	 *            the path spec to map servlet to
	 * @throws HttpServerException
	 *             if an error occurs
	 */
	public synchronized static void addServlet(ServletHolder servlet, String pathSpec)
			throws HttpServerException {
		addServlet((Object) servlet, pathSpec);
	}

	/**
	 * 
	 * @param servlet
	 * @param pathSpec
	 * @throws HttpServerException
	 */
	@SuppressWarnings("unchecked")
	private static void addServlet(Object servlet, String pathSpec) throws HttpServerException {
		if (contexts != null) {
			try {
				if (servlet instanceof ServletHolder) {
					contexts.addServlet((ServletHolder) servlet, pathSpec);
				} else if (servlet instanceof Class) {
					contexts.addServlet((Class<? extends Servlet>) servlet, pathSpec);
				} else {
					contexts.addServlet((String) servlet, pathSpec);
				}
				logger.debug("Added servlet for path {}", pathSpec);
			} catch (Exception e) {
				throw new HttpServerException(e);
			}
		} else {
			throw new HttpServerException("HTTP server service not available");
		}
	}

	/**
	 * Removes the previously registered servlet holder
	 * 
	 * @param servlet
	 *            the servlet holder
	 * @throws HttpServerException
	 *             if an error occurs
	 */
	public synchronized static void removeServlet(ServletHolder servlet)
			throws HttpServerException {
		if (contexts != null) {
			try {
				ServletHandler handler = contexts.getServletHandler();
				List<ServletHolder> servlets = new ArrayList<ServletHolder>();
				List<ServletMapping> mappings = new ArrayList<ServletMapping>();

				for (ServletHolder sh : handler.getServlets()) {
					if (servlet != sh) {
						servlets.add(sh);
					}
				}

				for (ServletMapping mapping : handler.getServletMappings()) {
					if (!mapping.getServletName().equals(servlet.getName())) {
						mappings.add(mapping);
					} else {
						if (logger.isDebugEnabled()) {
							for (String path : mapping.getPathSpecs()) {
								logger.debug("Removed servlet for path {}", path);
							}
						}
					}
				}

				handler.setServletMappings(mappings.toArray(new ServletMapping[] {}));
				handler.setServlets(servlets.toArray(new ServletHolder[] {}));
			} catch (Exception e) {
				throw new HttpServerException(e);
			}
		}
	}

	/**
	 * Removes the previously registered servlets of the specified class
	 * 
	 * @param servlet
	 *            the servlet class
	 * @throws HttpServerException
	 *             if an error occurs
	 */
	public synchronized static void removeServlet(Class<? extends Servlet> servlet)
			throws HttpServerException {
		if (contexts != null) {
			ServletHandler handler = contexts.getServletHandler();
			if (handler != null) {
				for (ServletHolder sh : handler.getServlets()) {
					Servlet s;
					try {
						s = sh.getServlet();
					} catch (ServletException e) {
						throw new HttpServerException(e);
					}
					if (servlet.isInstance(s)) {
						removeServlet(sh);
					}
				}
			}
		}
	}

	@Override
	public void quit() throws Exception {
		if (server != null)
			server.stop();
	}

}
