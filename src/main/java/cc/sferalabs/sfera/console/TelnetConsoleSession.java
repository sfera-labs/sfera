/**
 * 
 */
package cc.sferalabs.sfera.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class TelnetConsoleSession extends ConsoleSession {

	private static final Logger logger = LoggerFactory.getLogger(TelnetConsoleSession.class);

	private final Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	/**
	 * @param socket
	 */
	TelnetConsoleSession(Socket socket) {
		super("Telnet Console (" + socket.getRemoteSocketAddress() + ")");
		this.socket = socket;
	}

	@Override
	protected boolean init() {
		SocketAddress addr = socket.getRemoteSocketAddress();
		logger.info("Accepted telnet connection from {}", addr);
		boolean ok = false;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("User:");
			String user = in.readLine();
			out.println("Password:");
			String pswd = in.readLine();
			User u = Access.authenticate(user, pswd);
			if (u == null || !u.isInRole("admin")) {
				logger.warn("Attempted telnet console access from {} - user: {}", addr, user);
				out.println("Nope!");
			} else {
				logger.warn("Telnet console access from {} - user: {}", addr, user);
				out.println("Granted - Input your commands:");
				ok = true;
			}
		} catch (Exception e) {
			logger.error("Connection error", e);
		}
		return ok;
	}

	@Override
	protected String acceptCommand() {
		try {
			String cmd = in.readLine();
			if (cmd != null) {
				return cmd;
			}
		} catch (IOException e) {
		}
		quit();
		return null;
	}

	@Override
	protected void output(String text) {
		out.println(text);
	}

	@Override
	protected void clear() {
		try {
			out.close();
		} catch (Exception e) {
		}
		try {
			in.close();
		} catch (Exception e) {
		}
		try {
			socket.close();
		} catch (Exception e) {
		}
		TelnetConsoleServer.removeSession(this);
	}

}
