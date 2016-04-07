/**
 * 
 */
package cc.sferalabs.sfera.web.api.websockets;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.console.ConsoleSession;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class WsConsoleSession extends ConsoleSession {

	private static final Logger logger = LoggerFactory.getLogger(WsConsoleSession.class);

	private final ApiSocket socket;
	private final BlockingQueue<String> cmdQ = new ArrayBlockingQueue<>(10);

	/**
	 * 
	 * @param socket
	 *            the WebSocket linked to this session
	 */
	WsConsoleSession(ApiSocket socket) {
		super("WebSocket Console Session (" + socket.connectionId + ")");
		this.socket = socket;
	}

	@Override
	protected boolean init() {
		logger.info("Console session started - Host: {} User: {}", socket.hostname, socket.user);
		return true;
	}

	@Override
	protected void cleanUp() {
		logger.info("Console session ended - Host: {} User: {}", socket.hostname, socket.user);
	}

	@Override
	public String acceptCommand() {
		try {
			return cmdQ.take();
		} catch (InterruptedException e) {
			return null;
		}
	}

	@Override
	protected void doOutput(String text) {
		try {
			OutgoingWsMessage m = new OutgoingWsMessage("console", socket);
			m.send("output", text);
		} catch (Exception e) {
			socket.onWebSocketError(e);
		}
	}

	/**
	 * @param command
	 *            the command to process
	 */
	void process(String command) {
		if (!cmdQ.offer(command)) {
			logger.warn("Coudn't process command '{}'", command);
		}
	}

}
