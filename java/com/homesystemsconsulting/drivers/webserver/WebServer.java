package com.homesystemsconsulting.drivers.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.xml.stream.XMLStreamException;

import org.apache.http.client.utils.DateUtils;

import com.homesystemsconsulting.core.Configuration;
import com.homesystemsconsulting.core.Sfera;
import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.core.TasksManager;
import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.drivers.webserver.HttpRequestHeader.Method;
import com.homesystemsconsulting.drivers.webserver.access.Access;
import com.homesystemsconsulting.util.files.ResourcesUtils;

public abstract class WebServer extends Driver {
	
	static final Path ROOT = Paths.get("webapp/");
	static final Path CACHE_ROOT = ROOT.resolve("cache/");
	
	private static final Path ABSOLUTE_CACHE_ROOT_PATH = CACHE_ROOT.toAbsolutePath();
	private static final String API_BASE_URI = "/x/";
	
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DateUtils.PATTERN_RFC1123);
	private static final int SOCKET_TIMEOUT = 60000;
	
	private static final String HTTP_HEADER_FIELD_SERVER = "Sfera " + Sfera.VERSION;
	private static final String HTTP_CONTENT_FILE_NOT_FOUND = "<html><head><title>File Not Found</title></head><body>Sorry, the object you requested was not found.</body><html>";

	private static TasksManager tasksManager;
	private static Object tasksManagerLock = new Object();
	
	private static Set<String> interfaces;
	
	private ServerSocket socket;
	private String defaultInterface;
	private boolean usePermanentCache;
	
	/**
	 * 
	 * @param id
	 */
	protected WebServer(String id) {
		super(id);
	}

	@Override
	protected boolean onInit() throws InterruptedException {
		synchronized (tasksManagerLock) {
			if (tasksManager == null) {
				Integer maxRequestThreads = Configuration.getIntProperty("web.max_threads", null);
				if (maxRequestThreads == null) {
					int availableProcessors = Runtime.getRuntime().availableProcessors();
					maxRequestThreads = availableProcessors * 128;
				}
				
				tasksManager = new TasksManager(Executors.newFixedThreadPool(maxRequestThreads));
			}
		}
		
		defaultInterface = Configuration.getProperty("web.default_interface", null);
		
		Integer port = Configuration.getIntProperty("web." + getId() + ".port", getDefaultPort());
		
		try {
			socket = getServerSocket(port);
		} catch (Exception e) {
			log.error("error instantiating socket: " + e);
			return false;
		}
		
		usePermanentCache = Configuration.getBoolProperty("web.use_permanent_cache", true);
		
		createCache();
		
		log.info("accepting connections on port " + socket.getLocalPort());
		
		//TODO read from access.ini ============
		try {
			Access.addUser("user", "12345");
			Access.addUser("test", "55555");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//======================================
		
		return true;
	}

	/**
	 * 
	 */
	private void createCache() {
		interfaces = new HashSet<String>();
		
		try {
			Set<String> interfaceNames = ResourcesUtils.listDirectoriesNamesInDirectory(ROOT.resolve("interfaces/"), true);
			if (interfaceNames != null) {
				for (String interfaceName : interfaceNames) {
					try {
						createCacheFor(interfaceName);
						interfaces.add(interfaceName);
					} catch (Exception e) {
						log.error("error creating cache for interface '" + interfaceName + "': " + e);
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			log.error("error creating cache: " + e);
		}
	}

	/**
	 * 
	 * @param interfaceName
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private void createCacheFor(String interfaceName) throws IOException, XMLStreamException {
		log.debug("creating cache for interface: " + interfaceName);
		InterfaceCacheCreator icc = new InterfaceCacheCreator(interfaceName);
		icc.create(usePermanentCache);
		log.debug("created cache for interface: " + interfaceName);
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
	 * @return
	 * @throws Exception
	 */
	protected abstract ServerSocket getServerSocket(int port) throws Exception;

	@Override
	protected boolean loop() throws InterruptedException {
		Socket connection = null;
		try {
			connection = socket.accept();
			log.debug("accepted connection from: " + connection.getInetAddress());
			connection.setSoTimeout(SOCKET_TIMEOUT);
			delegateRequestProcess(connection);
			
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

	/**
	 * 
	 * @param connection
	 */
	private void delegateRequestProcess(Socket connection) {
		tasksManager.execute(new RequestProcessor(connection));
	}

	@Override
	protected void onQuit() throws InterruptedException {
		try {
			socket.close();
		} catch (Exception e) {}
		
		synchronized (tasksManagerLock) {			
			if (tasksManager != null) {
				tasksManager.getExecutorService().shutdownNow();
				tasksManager = null;
			}
		}
	}
	
	/**
	 * 
	 *
	 */
	private class RequestProcessor extends Task {
		
		private final Socket connection;

		public RequestProcessor(Socket connection) {
			super(getId() + "#" + connection.getInetAddress());
			this.connection = connection;
		}

		@Override
		public void execute() {
			try (
					PrintWriter out = new PrintWriter(connection.getOutputStream());
					BufferedOutputStream dataOut = new BufferedOutputStream(connection.getOutputStream());
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			) {
				
				boolean keepAlive = true;
				while (keepAlive) {
					HttpRequestHeader httpRequestHeader = null;
					String reqLine;
					while ((reqLine = in.readLine()) != null) {
						if (reqLine.length() == 0) {
							if (httpRequestHeader == null) {
								keepAlive = false;
								
							} else {
								if (httpRequestHeader.getConnectionClose()) {
									keepAlive = false;
								} 
								
								if (!processRequest(httpRequestHeader, in, out, dataOut)) {
									keepAlive = false;
								}
							}
							
							break;
						}
						
						if (httpRequestHeader == null) {
							try {
								httpRequestHeader = new HttpRequestHeader(reqLine);
							} catch (NotImplementedRequestMethodException e) {
								respondWithNotImplementedError(out);
								keepAlive = false;
								break;
							}
						} else {
							httpRequestHeader.addField(reqLine);
						}
					}
					
					if (reqLine == null) {
						keepAlive = false;
					}
				}
				
			} catch (Exception e) {
				log.warning("error processing request: " + e);
				e.printStackTrace();
			}
			
			log.debug("request process for " + connection.getInetAddress() + " terminated");
		}

		/**
		 * 
		 * @param httpRequestHeader
		 * @param in
		 * @param out
		 * @param dataOut
		 * @return
		 * @throws Exception
		 */
		private boolean processRequest(HttpRequestHeader httpRequestHeader, 
				BufferedReader in, PrintWriter out, BufferedOutputStream dataOut) throws Exception {
			
			String uri = httpRequestHeader.getURI();
			log.debug("processing request from: " + connection.getInetAddress() + " URI: " + uri);
			
			int qmIdx = uri.indexOf('?');
			String query;
			if (qmIdx >= 0) {
				query = uri.substring(qmIdx);
				uri = uri.substring(0, qmIdx);
			} else {
				query = null;
			}
			
			if (uri.startsWith(API_BASE_URI)) {
				return processApiRequest(uri.substring(API_BASE_URI.length()), query, httpRequestHeader, in, out);
			
			} else {
				return processFileRequest(uri, httpRequestHeader, out, dataOut);
			}
		}

		/**
		 * 
		 * @param uri
		 * @param httpRequestHeader
		 * @param out
		 * @param dataOut
		 * @return
		 * @throws IOException
		 */
		private boolean processFileRequest(String uri, HttpRequestHeader httpRequestHeader,
				PrintWriter out, BufferedOutputStream dataOut) throws IOException {
			
			if (uri.charAt(0) == '/') {
				uri = uri.substring(1);
			}
			Path path = CACHE_ROOT.resolve(uri).toAbsolutePath().normalize();
			
			if (!path.startsWith(ABSOLUTE_CACHE_ROOT_PATH)) {
				log.warning("resource out of root requested: " + path);
				return false;
			}
			
			path = ABSOLUTE_CACHE_ROOT_PATH.relativize(path);
			
			String pathStr = path.toString();
			if (pathStr.equals("favicon.ico") || pathStr.equals("favicon.png")) {
				serveCacheFile(httpRequestHeader.method, path, out, dataOut, httpRequestHeader);
				return true;
			}
			
			String requestedInterface = path.getName(0).toString();
			
			if (requestedInterface.length() == 0 && defaultInterface != null) {
				redirectTo(defaultInterface, out, httpRequestHeader);
				return false;
			}
			
			if (!interfaces.contains(requestedInterface)) {
				log.warning("non-existing interface requested: " + requestedInterface);
				return false;
			}
			
			if (httpRequestHeader.method != HttpRequestHeader.Method.GET && httpRequestHeader.method != HttpRequestHeader.Method.HEAD) {
				respondWithNotImplementedError(out);
				return false;
			}
			
			if (isLoginComponent(path)) { // GET /<interface>/login/...
				serveCacheFile(httpRequestHeader.method, path, out, dataOut, httpRequestHeader);
				
			} else {
				if (isLoginAlias(path)) { // GET /<interface>/login
					if (isAuthenticated(httpRequestHeader.getCookies())) {
						redirectTo(requestedInterface, out, httpRequestHeader);
						return false;
						
					} else {
						serveCacheFile(httpRequestHeader.method, path.resolve("index.html"), out, dataOut, httpRequestHeader);
					}
					
				} else if (isInterfaceAlias(path)) { // GET /<interface>
					if (isAuthenticated(httpRequestHeader.getCookies())) {
						if (isAuthorized(path, httpRequestHeader.getCookies())) {
							serveCacheFile(httpRequestHeader.method, path.resolve("index.html"), out, dataOut, httpRequestHeader);
						} else {
							log.warning("unauthorized interface request: " + path);
							return false;
						}
					} else {
						redirectTo(requestedInterface + "/login", out, httpRequestHeader);
						return false;
					}
					
				} else { // GET /<interface>/<any_other_resource>
					if (isAuthenticated(httpRequestHeader.getCookies()) && isAuthorized(path, httpRequestHeader.getCookies())) {
						serveCacheFile(httpRequestHeader.method, path, out, dataOut, httpRequestHeader);
						
					} else {
						log.debug("unauthorized file request: " + path);
			    		respondWithFileNotFoundError(out);
					}
				}
			}
			
		    return true;
		}

		/**
		 * 
		 * @param method
		 * @param path
		 * @param out
		 * @param dataOut
		 * @param httpRequestHeader
		 * @throws IOException
		 */
		private void serveCacheFile(Method method, Path path, PrintWriter out, 
				BufferedOutputStream dataOut, HttpRequestHeader httpRequestHeader) throws IOException {
			
			//TODO synchronize on cache creation
			
			path = ABSOLUTE_CACHE_ROOT_PATH.resolve(path);
			
			try {
	    		long lastModified = Files.getLastModifiedTime(path).toMillis();
	    		
				if (lastModified <= httpRequestHeader.getIfModifiedSinceTime()) {
	    			out.write("HTTP/1.1 304 Not Modified\r\n");
	    			out.print("Date: " + DATE_FORMAT.format(new Date()) + "\r\n");
			        out.print("Server: " + HTTP_HEADER_FIELD_SERVER + "\r\n");
			        out.print("Last-Modified: " + DATE_FORMAT.format(lastModified) + "\r\n");
	    			out.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
	    			out.print("\r\n");
	    			out.flush();
	    			
	    		} else {
					byte[] fileData = Files.readAllBytes(path);
		    		String contentType = getMimeContentType(path);
			    	
			        out.print("HTTP/1.1 200 OK\r\n");
			        out.print("Date: " + DATE_FORMAT.format(new Date()) + "\r\n");
			        out.print("Server: " + HTTP_HEADER_FIELD_SERVER + "\r\n");
			        out.print("Last-Modified: " + DATE_FORMAT.format(lastModified) + "\r\n");
			        out.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
			        if (contentType != null) {
			        	out.print("Content-type: " + contentType + "\r\n");
			        }
			        out.print("Content-length: " + fileData.length + "\r\n");
			        out.print("\r\n");
			        out.flush();
	
			        if (method == HttpRequestHeader.Method.GET) {
				        dataOut.write(fileData, 0, fileData.length);
				        dataOut.flush();
			        }
	    		}
			} catch (NoSuchFileException e) {
	    		log.warning("file not found: " + ABSOLUTE_CACHE_ROOT_PATH.relativize(path));
	    		respondWithFileNotFoundError(out);
			}
		}
		
		/**
		 * 
		 * @param page
		 * @param out
		 * @param httpRequestHeader
		 */
		private void redirectTo(String page, PrintWriter out, HttpRequestHeader httpRequestHeader) {
			out.print("HTTP/1.1 307 Temporary Redirect\r\n");
			out.print("Location: ");
			if (httpRequestHeader.getHost() != null) {				
				out.print(getProtocolName());
				out.print("://");
				out.print(httpRequestHeader.getHost());
			}
			out.print('/');
			out.print(page);
			out.print("\r\n\r\n");
			out.flush();
		}

		/**
		 * 
		 * @param out
		 */
		private void respondWithFileNotFoundError(PrintWriter out) {
			out.print("HTTP/1.1 404 File Not Found\r\n");
			out.print("Date: " + DATE_FORMAT.format(new Date()) + "\r\n");
			out.print("Server: " + HTTP_HEADER_FIELD_SERVER + "\r\n");
			out.print("Content-length: " + HTTP_CONTENT_FILE_NOT_FOUND.getBytes().length + "\r\n");
			out.print("Content-type: text/html\r\n");
			out.print("\r\n");
			out.print(HTTP_CONTENT_FILE_NOT_FOUND);
			out.flush();
		}
		
		/**
		 * 
		 * @param out
		 */
		private void respondWithNotImplementedError(PrintWriter out) {
			out.print("HTTP/1.1 501 Not implemented\r\n");
			out.print("Date: " + DATE_FORMAT.format(new Date()) + "\r\n");
			out.print("Server: " + HTTP_HEADER_FIELD_SERVER + "\r\n");
			out.print("\r\n");
			out.flush();
		}

		/**
		 * 
		 * @param path
		 * @param cookies
		 * @return
		 */
		private boolean isAuthorized(Path path, String cookies) {
			// TODO Auto-generated method stub
			return true;
		}

		/**
		 * 
		 * @param cookies
		 * @return
		 */
		private boolean isAuthenticated(String cookies) {
			// TODO Auto-generated method stub
			return true;
		}
		
		/**
		 * 
		 * @param path
		 * @return
		 */
		private boolean isInterfaceAlias(Path path) {
			return path.getNameCount() == 1;
		}

		/**
		 * 
		 * @param path
		 * @return
		 */
		private boolean isLoginAlias(Path path) {
			return (path.getNameCount() == 2 && path.getName(1).toString().equals("login"));
		}

		/**
		 * 
		 * @param path
		 * @return
		 */
		private boolean isLoginComponent(Path path) {
			return (path.getNameCount() > 2 && path.getName(1).toString().equals("login"));
		}

		/**
		 * 
		 * @param path
		 * @return
		 * @throws IOException
		 */
		private String getMimeContentType(Path path) throws IOException {
			if (path.getFileName().toString().equals("manifest")) {
				return "text/cache-manifest";
			}
			// TODO test on linux, on OS X it looks like it's not working...
			return Files.probeContentType(path);
		}

		/**
		 * 
		 * @param command
		 * @param query 
		 * @param httpRequestHeader
		 * @param in
		 * @param out
		 * @return
		 * @throws Exception 
		 */
		private boolean processApiRequest(String command, String query, HttpRequestHeader httpRequestHeader, 
				BufferedReader in, PrintWriter out) throws Exception {
			
			// TODO
			
			System.out.println("command: " + command);
			System.out.println("query: " + query);
			
			if (command.equals("login")) {
				
				String username = getQueryValue("user", query);
				String password = getQueryValue("password", query);
				if (Access.authenticate(username, password)) {
					
				}
			}
			
			return false;
		}

		/**
		 * 
		 * @param key
		 * @param query
		 * @return
		 */
		private String getQueryValue(String key, String query) {
			int start = query.indexOf("?" + key + "=");
			if (start < 0) {
				start = query.indexOf("&" + key + "=");
				if (start < 0) {
					return null;
				}
			}
			start += key.length() + 2;
			int end = query.indexOf('&', start);
			if (end < 0) {
				end = query.length();
			}
			return query.substring(start, end);
		}
	}
}
