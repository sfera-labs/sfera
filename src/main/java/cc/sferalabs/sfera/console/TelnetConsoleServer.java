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

package cc.sferalabs.sfera.console;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.TasksManager;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class TelnetConsoleServer {

	private static final Logger logger = LoggerFactory.getLogger(TelnetConsoleServer.class);

	private final ServerSocket socket;
	private boolean run;

	/**
	 * Construct and starts as a new system task a telnet console server bound
	 * to the specified port.
	 * 
	 * @param port
	 *            the port number
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	TelnetConsoleServer(int port) throws IOException {
		this.socket = new ServerSocket(port);
		run = true;
		TasksManager.executeSystem("Telnet Console server", this::acceptTelnetConnection);
	}

	/**
	 * 
	 */
	private void acceptTelnetConnection() {
		logger.info("Telnet Console server accepting connections on port {}",
				socket.getLocalPort());
		while (run) {
			try {
				Socket s = socket.accept();
				TelnetConsoleSession tcs = new TelnetConsoleSession(s);
				tcs.start();
			} catch (IOException e) {
				if (socket.isClosed()) {
					break;
				}
				logger.error("Telnet server error", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			}
		}
		logger.debug("Telnet Console server quitted");
	}

	/**
	 * Stops the telnet server.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs when closing the socket
	 */
	public void quit() throws IOException {
		run = false;
		socket.close();
	}

}
