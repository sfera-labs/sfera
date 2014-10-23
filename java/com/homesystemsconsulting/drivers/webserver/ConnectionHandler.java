package com.homesystemsconsulting.drivers.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;

import com.homesystemsconsulting.core.Configuration;
import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.core.TasksManager;
import com.homesystemsconsulting.drivers.webserver.access.Access;
import com.homesystemsconsulting.drivers.webserver.access.Subscription;
import com.homesystemsconsulting.drivers.webserver.access.Token;
import com.homesystemsconsulting.drivers.webserver.access.User;
import com.homesystemsconsulting.drivers.webserver.util.DateUtil;
import com.homesystemsconsulting.events.Event;

public class ConnectionHandler extends Task {
	private static final int SOCKET_TIMEOUT = 60000;

	private static TasksManager tasksManager;
	private static final Object tasksManagerLock = new Object();

	private final Socket connection;
	private final String protocol;
	private PrintWriter out;
	private BufferedOutputStream dataOut;
	private BufferedReader in;
	
	/**
	 * 
	 * @param driverId
	 * @param connection
	 * @param protocol
	 * @throws SocketException
	 */
	public ConnectionHandler(String driverId, Socket connection, String protocol) throws SocketException {
		super(driverId + "#" + connection.getInetAddress());
		this.connection = connection;
		this.connection.setSoTimeout(SOCKET_TIMEOUT);
		this.protocol = protocol;
		tasksManager.execute(this);
	}
	
	/**
	 * 
	 * @param configuration
	 */
	public static void init(Configuration configuration) {
		synchronized (tasksManagerLock) {
			if (tasksManager == null) {
				Integer maxRequestThreads = configuration.getIntProperty("max_threads", null);
				if (maxRequestThreads == null) {
					int availableProcessors = Runtime.getRuntime().availableProcessors();
					maxRequestThreads = availableProcessors * 128;
				}
				tasksManager = new TasksManager(Executors.newFixedThreadPool(maxRequestThreads));
			}
		}
	}
	
	/**
	 * 
	 */
	public static void quit() {
		synchronized (tasksManagerLock) {			
			if (tasksManager != null) {
				tasksManager.getExecutorService().shutdownNow();
				tasksManager = null;
			}
		}
	}

	@Override
	public void execute() {
		try {
			out = new PrintWriter(connection.getOutputStream());
			dataOut = new BufferedOutputStream(connection.getOutputStream());
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
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
							
							try {
								if (!processRequest(httpRequestHeader)) {
									keepAlive = false;
								}
							} catch (Exception e) {
								WebServer.getLogger().warning("error processing request: " + e);
								e.printStackTrace();
								keepAlive = false;
							}
						}
						
						break;
					}
					
					if (httpRequestHeader == null) {
						try {
							httpRequestHeader = new HttpRequestHeader(reqLine);
						} catch (NotImplementedRequestMethodException e) {
							notImplementedError();
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
			WebServer.getLogger().debug("connection exception: " + e);
			
		} finally {
			try { in.close(); } catch (Exception e) {}
			try { out.close(); } catch (Exception e) {}
			try { dataOut.close(); } catch (Exception e) {}
		}
		
		WebServer.getLogger().debug("connection " + connection.getInetAddress() + " terminated");
	}

	/**
	 * 
	 * @param httpRequestHeader
	 * @return
	 * @throws Exception
	 */
	private boolean processRequest(HttpRequestHeader httpRequestHeader) throws Exception {
		String uri = httpRequestHeader.getURI();
		Token token = getToken(httpRequestHeader);
		WebServer.getLogger().debug("processing request from: " + connection.getInetAddress() + " URI: " + uri + (token == null ? "" : " User: " + token.getUser().getUsername()));
		
		if (uri.startsWith(WebServer.API_BASE_URI)) {
			HashMap<String, String> query = new HashMap<String, String>();;
			int qmIdx = uri.indexOf('?');
			if (qmIdx >= 0) {
				String queryString = uri.substring(qmIdx + 1);
				for (String pair : queryString.split("&")) {
					String[] key_val = pair.split("=");
					if (key_val.length == 2) {
						query.put(key_val[0], key_val[1]);
					}
				}
				uri = uri.substring(0, qmIdx);
			}
			
			return processApiRequest(uri.substring(WebServer.API_BASE_URI.length()), token, query, httpRequestHeader);
			
		} else {
			return InterfaceCache.processFileRequest(uri, token, httpRequestHeader, this);
		}
	}

	/**
	 * 
	 * @param httpRequestHeader
	 * @return
	 */
	private Token getToken(HttpRequestHeader httpRequestHeader) {
		String tokenUUID = getCookieValue("token", httpRequestHeader.getCookies());
		if (tokenUUID == null) {
			return null;
		}
		return Access.getToken(tokenUUID, httpRequestHeader);
	}

	/**
	 * 
	 * @param request
	 * @param token
	 * @param query
	 * @param httpRequestHeader
	 * @return
	 * @throws Exception
	 */
	private boolean processApiRequest(String request, Token token, HashMap<String, String> query, HttpRequestHeader httpRequestHeader) throws Exception {
		if (request.equals("login")) {
			login(token, query, httpRequestHeader);
			return true;
		}
		
		if (token == null) {
			notAuthorizedError();
			return true;
		}
		
		if (request.equals("logout")) {
			logout(token);
			return true;
		}
		
		if (request.equals("subscribe")) {
			subscribe(token, query);
			return true;
		}
		
		if (request.startsWith("state/")) {
			state(request.substring(6), token, query);
			return true;
		}
		
		WebServer.getLogger().warning("unknown API request: " + request);
		return false;
	}

	/**
	 * 
	 * @param token
	 * @param query
	 */
	private void subscribe(Token token, HashMap<String, String> query) {
		String id = query.get("id");
		String nodes = query.get("nodes");
		id = token.subscribe(id, nodes);
		
		ok("{\"id\":\"" + id + "\"}", "application/json");
	}
	
	/**
	 * 
	 * @param id
	 * @param token
	 * @param query
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private void state(String id, Token token, HashMap<String, String> query) throws InterruptedException, IOException {
		long ts;
		try {
			ts = Long.parseLong(query.get("ts"));
		} catch (NumberFormatException nfe) {
			ts = 0;
		}
		
		Subscription s = token.getSubscription(id);
		if (s != null) {
			Map<String, Event> changes = s.pollChanges(ts, 5, TimeUnit.SECONDS);
			JSONObject obj = new JSONObject();
			obj.put("timestamp", System.currentTimeMillis());
			JSONObject nodes = new JSONObject();
			for (Entry<String, Event> change : changes.entrySet()) {
				nodes.put(change.getKey(), change.getValue().getValue());
			}
			obj.put("nodes", nodes);
			
			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			
			ok(out.toString(), "application/json");
			
		} else {
			notFoundError();
		}
	}

	/**
	 * 
	 * @param token
	 * @param query
	 * @param httpRequestHeader
	 * @throws Exception
	 */
	private void login(Token token, HashMap<String, String> query, HttpRequestHeader httpRequestHeader) throws Exception {
		String username = query.get("user");
		String password = query.get("password");
		
		if (username == null || password == null) {
			if (token != null) {
				ok(null, null);
			} else {
				notAuthorizedError();
			}
			
		} else {
			if (token != null) {
				Access.removeToken(token.getUUID());
			}
			User user = Access.authenticate(username, password);
			if (user != null) {
				String tokenUUID = Access.assignToken(user, httpRequestHeader);
				setTokenCookie(tokenUUID);
				WebServer.getLogger().info("login: " + username);
				
			} else {
				WebServer.getLogger().warning("failed login attempt - username: " + username);
				notAuthorizedError();
			}
		}
	}
	
	/**
	 * 
	 * @param token
	 */
	private void logout(Token token) {
		Access.removeToken(token.getUUID());
		WebServer.getLogger().info("logout: " + token.getUser().getUsername());
		setTokenCookie(null);
	}

	/**
	 * 
	 */
	public void notAuthorizedError() {
		out.print("HTTP/1.1 401 Unauthorized\r\n");
		out.print("Date: " + DateUtil.now() + "\r\n");
		out.print("Server: " + WebServer.HTTP_HEADER_FIELD_SERVER + "\r\n");
		out.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
		out.print("Content-length: 0\r\n");
		out.print("\r\n");
		out.flush();
	}
	
	/**
	 * 
	 */
	public void notImplementedError() {
		out.print("HTTP/1.1 501 Not implemented\r\n");
		out.print("Date: " + DateUtil.now() + "\r\n");
		out.print("Server: " + WebServer.HTTP_HEADER_FIELD_SERVER + "\r\n");
		out.print("Content-length: 0\r\n");
		out.print("\r\n");
		out.flush();
	}
	
	/**
	 * 
	 * @param out
	 */
	public void notFoundError() {
		out.print("HTTP/1.1 404 Not Found\r\n");
		out.print("Date: " + DateUtil.now() + "\r\n");
		out.print("Server: " + WebServer.HTTP_HEADER_FIELD_SERVER + "\r\n");
		out.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
		out.print("Content-length: 0\r\n");
		out.print("\r\n");
		out.flush();
	}
	
	/**
	 * 
	 * @param body
	 * @param contentType
	 */
	private void ok(String body, String contentType) {
		out.print("HTTP/1.1 200 OK\r\n");
        out.print("Date: " + DateUtil.now() + "\r\n");
        out.print("Server: " + WebServer.HTTP_HEADER_FIELD_SERVER + "\r\n");
        out.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
        if (contentType != null) {
        	out.print("Content-type: " + contentType + "\r\n");
        }
        if (body != null) {
        	out.print("Content-length: " + body.getBytes(Charset.forName("UTF-8")).length + "\r\n");
        } else {
        	out.print("Content-length: 0\r\n");
        }
        out.print("\r\n");
        if (body != null) {
        	out.print(body);
        }
        out.flush();
	}
	
	/**
	 * 
	 * @param page
	 * @param httpRequestHeader
	 */
	public void redirectTo(String page, HttpRequestHeader httpRequestHeader) {
		out.print("HTTP/1.1 307 Temporary Redirect\r\n");
		out.print("Location: ");
		if (httpRequestHeader.getHost() != null) {				
			out.print(protocol);
			out.print("://");
			out.print(httpRequestHeader.getHost());
		}
		out.print('/');
		out.print(page);
		out.print("\r\n");
		out.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
		out.print("Content-length: 0\r\n");
		out.print("\r\n");
		out.flush();
	}
	
	/**
	 * 
	 * @param tokenUUID
	 */
	private void setTokenCookie(String tokenUUID) {
		out.print("HTTP/1.1 200 OK\r\n");
		out.print("Date: " + DateUtil.now() + "\r\n");
		out.print("Server: " + WebServer.HTTP_HEADER_FIELD_SERVER + "\r\n");
		if (tokenUUID == null) {
			out.print("Set-Cookie: token=removed; Path=/; Max-Age=0\r\n");
		} else {
			out.print("Set-Cookie: token=" + tokenUUID + "; Path=/; Max-Age=" + Token.maxAgeSeconds + "\r\n");
		}
		out.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
		out.print("Content-length: 0\r\n");
		out.print("\r\n");
		out.flush();
	}
	
	/**
	 * 
	 * @param key
	 * @param cookies
	 * @return
	 */
	private String getCookieValue(String key, String cookies) {
		if (cookies == null) {
			return null;
		}
		String[] entries = cookies.split("[ ,;]+");
		for (String entry : entries) {
			if (entry.startsWith(key + "=")) {
				return entry.substring(key.length() + 1);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param s
	 */
	public void write(String s) {
		out.print(s);
	}
	
	/**
	 * 
	 * @param b
	 * @throws IOException
	 */
	public void write(byte[] b) throws IOException {
		dataOut.write(b, 0, b.length);
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void flush() throws IOException {
		out.flush();
		dataOut.flush();
	}
}
