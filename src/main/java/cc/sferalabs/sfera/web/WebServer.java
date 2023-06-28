/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.session.FileSessionDataStoreFactory;
import org.eclipse.jetty.server.session.NullSessionDataStore;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.SessionDataStore;
//import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.Configuration;
import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.web.api.http.servlets.CommandServlet;
import cc.sferalabs.sfera.web.api.http.servlets.ConnectServlet;
import cc.sferalabs.sfera.web.api.http.servlets.EventServlet;
import cc.sferalabs.sfera.web.api.http.servlets.InfoServlet;
import cc.sferalabs.sfera.web.api.http.servlets.LoginServlet;
import cc.sferalabs.sfera.web.api.http.servlets.LogoutServlet;
import cc.sferalabs.sfera.web.api.http.servlets.StateServlet;
import cc.sferalabs.sfera.web.api.http.servlets.SubscribeServlet;
import cc.sferalabs.sfera.web.api.http.servlets.access.AddAccessServlet;
import cc.sferalabs.sfera.web.api.http.servlets.access.ListUsersServlet;
import cc.sferalabs.sfera.web.api.http.servlets.access.RemoveAccessServlet;
import cc.sferalabs.sfera.web.api.http.servlets.access.UpdateAccessServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.CopyFilesServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.DeleteFilesServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.DownloadFilesServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.ListFilesServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.MkdirFileServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.MoveFilesServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.ReadFileServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.UploadFilesServlet;
import cc.sferalabs.sfera.web.api.http.servlets.files.WriteFileServlet;
import cc.sferalabs.sfera.web.api.websockets.ApiWebSocketServlet;

/**
 * HTTP Server
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class WebServer implements AutoStartService {

	private static final String KEYSTORE_PATH = "data/http/sfera.keys";
	private static final String SESSIONS_STORE_DIR = "data/http/sessions";

	private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
	private static final String[] EXCLUDED_PROTOCOLS = { "SSL", "SSLv2", "SSLv2Hello", "SSLv3" };
	private static final String[] EXCLUDED_CIPHER_SUITES = { ".*NULL.*", ".*RC4.*", ".*MD5.*", ".*DES.*", ".*DSS.*",
			"^.*_(MD5|SHA|SHA1)$", "^TLS_RSA_.*$" };
	private static final String DEFAULT_CN = "sferaserver";
	private static final String DEFAULT_KEY_STORE_PASSWORD = "sferapass";
	private static final int MAX_HEADER_SIZE = 16384;

	private static Server server;
	private static ServletContextHandler contexts;

	@Override
	public void init() throws Exception {
		Configuration config = SystemNode.getConfiguration();
		Integer httpPort = null;
		Integer httpsPort = null;
		if (config != null) {
			httpPort = config.get("http_port", null);
			httpsPort = config.get("https_port", null);
		}

		if (httpPort == null && httpsPort == null) {
			logger.warn("No HTTP port defined in configuration. Server disabled");
			return;
		}

		int maxThreads = config.get("http_max_threads", Runtime.getRuntime().availableProcessors() * 128);
		int minThreads = config.get("http_min_threads", 8);
		int idleTimeout = config.get("http_threads_idle_timeout", 60000);

		QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

		server = new Server(threadPool);

		if (httpPort != null) {
			HttpConfiguration http_config = new HttpConfiguration();
			http_config.setRequestHeaderSize(MAX_HEADER_SIZE);
			ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
			http.setName("http");
			http.setPort(httpPort);
			server.addConnector(http);
			logger.info("Starting HTTP server on port {}", httpPort);
		}

		if (httpsPort != null) {
			try {
				SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
				sslContextFactory.setKeyStorePath(KEYSTORE_PATH);
				String cn = config.get("https_cert_cn", DEFAULT_CN);
				String storePassword = config.get("https_cert_storepass", DEFAULT_KEY_STORE_PASSWORD);
				String keyPassword = config.get("https_cert_keypass", storePassword);
				sslContextFactory.setKeyStorePassword(storePassword);
				sslContextFactory.setKeyManagerPassword(keyPassword);

				if (!checkKeyStoreFile(sslContextFactory, storePassword, keyPassword)) {
					generateKeyStoreFile(cn, storePassword, keyPassword);
				}

				sslContextFactory.addExcludeProtocols(EXCLUDED_PROTOCOLS);
				sslContextFactory.setExcludeCipherSuites(EXCLUDED_CIPHER_SUITES);
				sslContextFactory.setRenegotiationAllowed(false);
				sslContextFactory.setUseCipherSuitesOrder(false);

				HttpConfiguration https_config = new HttpConfiguration();
				https_config.setSecurePort(httpsPort);
				https_config.addCustomizer(new SecureRequestCustomizer());
				https_config.setRequestHeaderSize(MAX_HEADER_SIZE);

				ServerConnector https = new ServerConnector(server,
						new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
						new HttpConnectionFactory(https_config));
				https.setName("https");
				https.setPort(httpsPort);

				server.addConnector(https);
				logger.info("Starting HTTPS server on port {}", httpsPort);
			} catch (Exception e) {
				logger.error("Error enabling HTTPS", e);
			}
		}

		SessionHandler sessionHandler = new SessionHandler();
		sessionHandler.setSessionCookie("session");
		int maxInactiveInterval = config.get("http_session_max_inactive", 3600);
		sessionHandler.setMaxInactiveInterval(maxInactiveInterval);
		sessionHandler.addEventListener(new HttpSessionDestroyer());
		SessionCache sessionCache = new AuthenticationSessionCache(sessionHandler);
		sessionCache.setRemoveUnloadableSessions(true);
		boolean persistSessions = config.get("http_session_persist", false);
		if (persistSessions) {
			FileSessionDataStoreFactory dataStoreFactory = new FileSessionDataStoreFactory();
			dataStoreFactory.setStoreDir(new File(SESSIONS_STORE_DIR));
			dataStoreFactory.setDeleteUnrestorableFiles(true);
			dataStoreFactory.setSavePeriodSec(maxInactiveInterval / 2);
			SessionDataStore dataStore = dataStoreFactory.getSessionDataStore(sessionHandler);
			sessionCache.setSessionDataStore(dataStore);
		} else {
			sessionCache.setSessionDataStore(new NullSessionDataStore());
		}
		sessionHandler.setSessionCache(sessionCache);

		contexts = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
		contexts.setInitParameter(SessionHandler.__MaxAgeProperty, config.get("http_session_max_age", -1).toString());
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
	 * @param sslContextFactory
	 * @param storePassword
	 * @param keyPassword
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchProviderException
	 */
	private boolean checkKeyStoreFile(SslContextFactory sslContextFactory, String storePassword, String keyPassword)
			throws KeyStoreException, NoSuchProviderException {
		Path keystorePath = Paths.get(KEYSTORE_PATH);
		if (!Files.exists(keystorePath)) {
			logger.warn("SSL keystore file not found");
			return false;
		}

		String storeProvider = sslContextFactory.getKeyStoreProvider();
		String storeType = sslContextFactory.getKeyStoreType();
		KeyStore keystore;
		if (storeProvider != null) {
			keystore = KeyStore.getInstance(storeType, storeProvider);
		} else {
			keystore = KeyStore.getInstance(storeType);
		}
		Resource store = sslContextFactory.getKeyStoreResource();
		try (InputStream inStream = store.getInputStream()) {
			keystore.load(inStream, storePassword.toCharArray());
			if (keyPassword != null) {
				String alias = keystore.aliases().nextElement();
				keystore.getKey(alias, keyPassword.toCharArray());
			}
		} catch (Exception e) {
			logger.warn("SSL keystore file tampered with, or incorrect password", e);
			try {
				try {
					Files.move(keystorePath,
							keystorePath.resolveSibling(keystorePath.getFileName().toString() + "-corrupted"));
				} catch (FileAlreadyExistsException e1) {
					// preserve the old corrupted file
					Files.delete(keystorePath);
				}
			} catch (Exception e2) {
			}
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param cn
	 * @param storePassword
	 * @param keyPassword
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void generateKeyStoreFile(String cn, String storePassword, String keyPassword)
			throws IOException, InterruptedException {
		logger.info("Generating self-signed certificate");
		Path keystorePath = Paths.get(KEYSTORE_PATH);
		Files.createDirectories(keystorePath.getParent());
		String[] cmd = { "keytool", "-genkeypair", "-dname", "cn=" + cn, "-alias", "sferaservergen", "-keystore",
				KEYSTORE_PATH, "-storepass", storePassword, "-keypass", keyPassword, "-validity", "73000", "-keyalg",
				"RSA", "-keysize", "2048", "-ext", "BC:critical=ca:false", "-ext",
				"KU:critical=digitalSignature,keyEncipherment", "-ext", "EKU:critical=serverAuth" };
		ProcessBuilder pb = new ProcessBuilder(cmd);
		// To make sure keytool is run using the same java version as Sfera's process so
		// to create a compatible keystore
		String javaVersion = System.getProperty("java.version");
		if (javaVersion != null) {
			pb.environment().put("JAVA_VERSION", javaVersion);
		}
		Process p = pb.start();
		if (p.waitFor() != 0) {
			throw new IOException("keytool termination error");
		}
	}

	/**
	 * 
	 * @throws WebServerException
	 */
	private void registerApiServlets() throws WebServerException {
		addServlet(LoginServlet.class, LoginServlet.PATH);
		addServlet(LogoutServlet.class, LogoutServlet.PATH);
		addServlet(ConnectServlet.class, ConnectServlet.PATH);
		addServlet(SubscribeServlet.class, SubscribeServlet.PATH);
		addServlet(StateServlet.class, StateServlet.PATH);
		addServlet(CommandServlet.class, CommandServlet.PATH);
		addServlet(EventServlet.class, EventServlet.PATH);
		addServlet(ApiWebSocketServlet.class, ApiWebSocketServlet.PATH);
		addServlet(InfoServlet.class, InfoServlet.PATH);

		// files
		addServlet(ListFilesServlet.class, ListFilesServlet.PATH);
		addServlet(ReadFileServlet.class, ReadFileServlet.PATH);
		addServlet(WriteFileServlet.class, WriteFileServlet.PATH);
		addServlet(MoveFilesServlet.class, MoveFilesServlet.PATH);
		addServlet(CopyFilesServlet.class, CopyFilesServlet.PATH);
		addServlet(DeleteFilesServlet.class, DeleteFilesServlet.PATH);
		addServlet(MkdirFileServlet.class, MkdirFileServlet.PATH);
		addServlet(DownloadFilesServlet.class, DownloadFilesServlet.PATH);
		addServlet(UploadFilesServlet.class, UploadFilesServlet.PATH);

		// access
		addServlet(ListUsersServlet.class, ListUsersServlet.PATH);
		addServlet(AddAccessServlet.class, AddAccessServlet.PATH);
		addServlet(RemoveAccessServlet.class, RemoveAccessServlet.PATH);
		addServlet(UpdateAccessServlet.class, UpdateAccessServlet.PATH);
	}

	/**
	 * Registers the specified servlet class to handle the requests on paths
	 * matching the specified path spec
	 * 
	 * @param servlet
	 *            the servlet class
	 * @param pathSpec
	 *            the path spec to map servlet to
	 * @throws WebServerException
	 *             if an error occurs
	 */
	public synchronized static void addServlet(Class<? extends Servlet> servlet, String pathSpec)
			throws WebServerException {
		addServlet((Object) servlet, pathSpec);
	}

	/**
	 * Registers the servlet contained by the servlet holder to handle the requests
	 * on paths matching the specified path spec
	 * 
	 * @param servlet
	 *            the servlet holder
	 * @param pathSpec
	 *            the path spec to map servlet to
	 * @throws WebServerException
	 *             if an error occurs
	 */
	public synchronized static void addServlet(ServletHolder servlet, String pathSpec) throws WebServerException {
		addServlet((Object) servlet, pathSpec);
	}

	/**
	 * 
	 * @param servlet
	 * @param pathSpec
	 * @throws WebServerException
	 */
	@SuppressWarnings("unchecked")
	private static void addServlet(Object servlet, String pathSpec) throws WebServerException {
		Objects.requireNonNull(servlet, "servlet must not be null");
		Objects.requireNonNull(pathSpec, "pathSpec must not be null");
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
				throw new WebServerException(e);
			}
		} else {
			throw new WebServerException("HTTP server service not available");
		}
	}

	/**
	 * Removes the previously registered servlet from the specified path
	 * 
	 * @param pathSpec
	 *            the path spec to remove
	 * @throws WebServerException
	 *             if an error occurs
	 */
	public synchronized static void removeServlet(String pathSpec) throws WebServerException {
		if (contexts != null) {
			try {
				ServletHandler handler = contexts.getServletHandler();
				List<ServletMapping> mappings = new ArrayList<>();

				for (ServletMapping mapping : handler.getServletMappings()) {
					List<String> pathSpecs = new ArrayList<>();
					for (String path : mapping.getPathSpecs()) {
						if (!pathSpec.equals(path)) {
							pathSpecs.add(path);
						}
					}
					if (!pathSpecs.isEmpty()) {
						mapping.setPathSpecs(pathSpecs.toArray(new String[pathSpecs.size()]));
						mappings.add(mapping);
					}
				}

				handler.setServletMappings(mappings.toArray(new ServletMapping[mappings.size()]));
			} catch (Exception e) {
				throw new WebServerException(e);
			}
		}
	}

	/**
	 * Removes the previously registered servlet holder
	 * 
	 * @param servlet
	 *            the servlet holder
	 * @throws WebServerException
	 *             if an error occurs
	 */
	public synchronized static void removeServlet(ServletHolder servlet) throws WebServerException {
		if (contexts != null) {
			try {
				ServletHandler handler = contexts.getServletHandler();
				List<ServletHolder> servlets = new ArrayList<>();
				List<ServletMapping> mappings = new ArrayList<>();

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

				handler.setServletMappings(mappings.toArray(new ServletMapping[mappings.size()]));
				handler.setServlets(servlets.toArray(new ServletHolder[servlets.size()]));
			} catch (Exception e) {
				throw new WebServerException(e);
			}
		}
	}

	/**
	 * Removes the previously registered servlets of the specified class
	 * 
	 * @param servlet
	 *            the servlet class
	 * @throws WebServerException
	 *             if an error occurs
	 */
	public synchronized static void removeServlet(Class<? extends Servlet> servlet) throws WebServerException {
		if (contexts != null) {
			ServletHandler handler = contexts.getServletHandler();
			if (handler != null) {
				for (ServletHolder sh : handler.getServlets()) {
					Servlet s;
					try {
						s = sh.getServlet();
					} catch (ServletException e) {
						throw new WebServerException(e);
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
