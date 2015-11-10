/**
 * 
 */
package cc.sferalabs.sfera.http.api.websockets;

import cc.sferalabs.sfera.core.services.Task;

/**
 * Task executing pings on WebSocket connections.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class PingTask extends Task {

	private final ApiSocket socket;
	private final long pingInterval;

	/**
	 * @param socket
	 * @param pingInterval
	 */
	PingTask(ApiSocket socket, long pingInterval) {
		super("WS Ping " + socket.hostname);
		this.socket = socket;
		this.pingInterval = pingInterval;
	}

	@Override
	protected void execute() {
		try {
			Thread.sleep(pingInterval);
			socket.ping();
		} catch (InterruptedException e) {
		}
	}

}
