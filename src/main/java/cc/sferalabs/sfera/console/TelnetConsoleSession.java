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
import java.net.SocketTimeoutException;

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
	 * Constructor.
	 * 
	 * @param socket
	 *            the socket associated with this telnet console session
	 */
	TelnetConsoleSession(Socket socket) {
		super("Telnet console session (" + socket.getRemoteSocketAddress() + ")");
		this.socket = socket;
	}

	@Override
	protected boolean init() {
		SocketAddress addr = socket.getRemoteSocketAddress();
		logger.info("Accepted telnet connection from {}", addr);
		boolean ok = false;
		try {
			socket.setSoTimeout(30000);
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
				logger.info("Telnet console access from {} - user: {}", addr, user);
				out.println("Granted - Input your commands:");
				ok = true;
			}
			socket.setSoTimeout(5000);
		} catch (SocketTimeoutException e) {
			logger.debug("Timeout expired", e);
			out.println("Timeout expired");
		} catch (Exception e) {
			logger.error("Connection error", e);
		}
		return ok;
	}

	@Override
	public String acceptCommand() {
		try {
			while (isActive()) {
				try {
					return in.readLine();
				} catch (SocketTimeoutException e) {
				}
			}
		} catch (IOException e) {
		}
		return null;
	}

	@Override
	public void doOutput(String text) {
		out.print(text);
		out.flush();
	}

	@Override
	protected void cleanUp() {
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
		logger.info(getName() + " closed");
	}

}
