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
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.FileSessionDataStoreFactory;
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
	private static final String[] EXCLUDED_CIPHER_SUITES = { ".*NULL.*", ".*RC4.*", ".*MD5.*", ".*DES.*", ".*DSS.*" };
	private static final String DEFAULT_KEY_STORE_PASSWORD = "sferapass";

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

		int maxThreads = config.get("http_max_threads", Runtime.getRuntime().availableProcessors() * 128);
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
			try {
				SslContextFactory sslContextFactory = new SslContextFactory();
				sslContextFactory.setKeyStorePath(KEYSTORE_PATH);
				String keyStorePassword = config.get("keystore_password", DEFAULT_KEY_STORE_PASSWORD);
				sslContextFactory.setKeyStorePassword(keyStorePassword);
				String keyManagerPassword = config.get("keymanager_password", null);
				if (keyManagerPassword != null) {
					sslContextFactory.setKeyManagerPassword(keyManagerPassword);
				}

				if (!checkKeyStoreFile(sslContextFactory, keyStorePassword)) {
					generateKeyStoreFile();
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
			} catch (Exception e) {
				logger.error("Error enabling HTTPS", e);
			}
		}

		SessionHandler sessionHandler = new SessionHandler();
		sessionHandler.setSessionCookie("session");
		int maxInactiveInterval = config.get("http_session_max_inactive", 3600);
		sessionHandler.setMaxInactiveInterval(maxInactiveInterval);
		sessionHandler.addEventListener(new HttpSessionDestroyer());
		boolean persistSessions = config.get("http_session_persist", false);
		if (persistSessions) {
			SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
			sessionHandler.setSessionCache(sessionCache);
			sessionCache.setRemoveUnloadableSessions(true);
			FileSessionDataStoreFactory dataStoreFactory = new FileSessionDataStoreFactory();
			dataStoreFactory.setStoreDir(new File(SESSIONS_STORE_DIR));
			dataStoreFactory.setDeleteUnrestorableFiles(true);
			dataStoreFactory.setSavePeriodSec(maxInactiveInterval / 2);
			SessionDataStore dataStore = dataStoreFactory.getSessionDataStore(sessionHandler);
			sessionCache.setSessionDataStore(dataStore);
		}

		contexts = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
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
	 * @param keyStorePassword
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchProviderException
	 */
	private boolean checkKeyStoreFile(SslContextFactory sslContextFactory, String keyStorePassword) {
		try {
			Path keystorePath = Paths.get(KEYSTORE_PATH);
			if (!Files.exists(keystorePath)) {
				logger.warn("SSL key store file not found");
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
				keystore.load(inStream, keyStorePassword == null ? null : keyStorePassword.toCharArray());
			} catch (CertificateException | IOException e) {
				logger.warn("SSL key store file corrupted");
				try {
					Files.move(keystorePath,
							keystorePath.resolveSibling(keystorePath.getFileName().toString() + "-corrupted"));
				} catch (FileAlreadyExistsException e1) {
					Files.delete(keystorePath);
				}
				return false;
			}
		} catch (Exception e) {
			logger.error("Error checking key store file", e);
		}

		return true;
	}

	/**
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void generateKeyStoreFile() throws IOException, InterruptedException {
		logger.info("Generating self-signed certificate");
		Path keystorePath = Paths.get(KEYSTORE_PATH);
		Files.createDirectories(keystorePath.getParent());
		String[] cmd = { "keytool", "-genkeypair", "-dname", "cn=Sfera Server generated", "-alias", "sferaservergen",
				"-keystore", KEYSTORE_PATH, "-keypass", DEFAULT_KEY_STORE_PASSWORD, "-storepass",
				DEFAULT_KEY_STORE_PASSWORD, "-validity", "73000", "-keyalg", "RSA", "-keysize", "2048" };
		Process p = new ProcessBuilder(cmd).start();
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
	 * Registers the servlet contained by the servlet holder to handle the
	 * requests on paths matching the specified path spec
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
				List<ServletHolder> servlets = new ArrayList<>();
				List<ServletMapping> mappings = new ArrayList<>();

				Map<String, List<ServletHolder>> servletsMap = new HashMap<>();
				for (ServletHolder sh : handler.getServlets()) {
					List<ServletHolder> list = servletsMap.get(sh.getName());
					if (list == null) {
						list = new ArrayList<>();
						servletsMap.put(sh.getName(), list);
					}
					list.add(sh);
				}

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
					} else {
						List<ServletHolder> list = servletsMap.get(mapping.getServletName());
						if (list != null && !list.isEmpty()) {
							list.remove(0);
						}
					}
				}

				for (List<ServletHolder> list : servletsMap.values()) {
					servlets.addAll(list);
				}

				handler.setServletMappings(mappings.toArray(new ServletMapping[mappings.size()]));
				handler.setServlets(servlets.toArray(new ServletHolder[servlets.size()]));
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
