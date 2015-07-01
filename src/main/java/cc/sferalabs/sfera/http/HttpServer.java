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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.core.AutoStartService;
import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.http.api.CommandServlet;
import cc.sferalabs.sfera.http.api.LoginServlet;
import cc.sferalabs.sfera.http.api.LogoutServlet;
import cc.sferalabs.sfera.http.api.StateServlet;
import cc.sferalabs.sfera.http.api.SubscribeServlet;
import cc.sferalabs.sfera.http.auth.AuthenticationFilter;
import cc.sferalabs.sfera.http.auth.AuthenticationRequestWrapper;

public class HttpServer implements AutoStartService {

	private static final String KEYSTORE_PATH = "data/http/sfera.keys";
	private static final String SESSIONS_STORE_DIR = "data/http/sessions";

	private static final Logger logger = LogManager.getLogger();

	private static Server server;

	private static ServletContextHandler contexts;

	@Override
	public void init() throws Exception {
		Configuration config = SystemNode.getConfiguration();
		Integer http_port = config.getIntProperty("http_port", null);
		Integer https_port = config.getIntProperty("https_port", null);

		if (http_port == null && https_port == null) {
			logger.debug("No HTTP port defined in configuration. Server disabled");
			return;
		}

		int maxThreads = config.getIntProperty("http_max_threads", Runtime
				.getRuntime().availableProcessors() * 128);
		int minThreads = config.getIntProperty("http_min_threads", 8);
		int idleTimeout = config.getIntProperty("http_threads_idle_timeout",
				60000);

		QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads,
				minThreads, idleTimeout);

		server = new Server(threadPool);

		if (http_port != null) {
			ServerConnector http = new ServerConnector(server);
			http.setName("http");
			http.setPort(http_port);
			server.addConnector(http);
			logger.info("Starting HTTP server on port {}", http_port);
		}

		if (https_port != null) {
			String keyStorePassword = config.getProperty("keystore_password",
					null);
			if (keyStorePassword == null) {
				throw new Exception(
						"'keystore_password' not specified in configuration");
			}
			Path keystorePath = Paths.get(KEYSTORE_PATH);
			if (!Files.exists(keystorePath)) {
				throw new NoSuchFileException(KEYSTORE_PATH);
			}

			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStorePath(KEYSTORE_PATH);
			sslContextFactory.setKeyStorePassword(keyStorePassword);
			String keyManagerPassword = config.getProperty(
					"keymanager_password", null);
			if (keyManagerPassword != null) {
				sslContextFactory.setKeyManagerPassword(keyManagerPassword);
			}

			HttpConfiguration https_config = new HttpConfiguration();
			https_config.setSecurePort(https_port);
			https_config.addCustomizer(new SecureRequestCustomizer());

			ServerConnector https = new ServerConnector(server,
					new SslConnectionFactory(sslContextFactory,
							HttpVersion.HTTP_1_1.asString()),
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
		int maxInactiveInterval = config.getIntProperty(
				"http_session_max_inactive", 3600);
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
			throw new Exception("Error starting server: "
					+ e.getLocalizedMessage(), e);
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
					return Access.authenticate(user.getUsername(),
							(String) credentials);
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
	}

	/**
	 * 
	 * @param servlet
	 * @param pathSpec
	 * @throws HttpServerException
	 */
	public synchronized static void addServlet(
			Class<? extends Servlet> servlet, String pathSpec)
			throws HttpServerException {
		addServlet((Object) servlet, pathSpec);
	}

	/**
	 * 
	 * @param servlet
	 * @param pathSpec
	 * @throws HttpServerException
	 */
	public synchronized static void addServlet(ServletHolder servlet,
			String pathSpec) throws HttpServerException {
		addServlet((Object) servlet, pathSpec);
	}

	/**
	 * 
	 * @param servlet
	 * @param pathSpec
	 * @throws HttpServerException
	 */
	@SuppressWarnings("unchecked")
	private static void addServlet(Object servlet, String pathSpec)
			throws HttpServerException {
		if (contexts != null) {
			try {
				if (servlet instanceof ServletHolder) {
					contexts.addServlet((ServletHolder) servlet, pathSpec);
				} else if (servlet instanceof Class) {
					contexts.addServlet((Class<? extends Servlet>) servlet,
							pathSpec);
				} else {
					contexts.addServlet((String) servlet, pathSpec);
				}
				logger.debug("Added servlet for path {}", pathSpec);
			} catch (Exception e) {
				throw new HttpServerException(e);
			}
		} else {
			throw new HttpServerException("Service not available");
		}
	}

	/**
	 * 
	 * @param servlet
	 * @throws HttpServerException
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
								logger.debug("Removed servlet for path {}",
										path);
							}
						}
					}
				}

				handler.setServletMappings(mappings
						.toArray(new ServletMapping[] {}));
				handler.setServlets(servlets.toArray(new ServletHolder[] {}));
			} catch (Exception e) {
				throw new HttpServerException(e);
			}
		}
	}

	/**
	 * 
	 * @param servlet
	 * @throws HttpServerException
	 */
	public synchronized static void removeServlet(
			Class<? extends Servlet> servlet) throws HttpServerException {
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
