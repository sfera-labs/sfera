/**
 * 
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
