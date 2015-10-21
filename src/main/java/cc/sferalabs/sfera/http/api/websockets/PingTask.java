/**
 * 
 */
package cc.sferalabs.sfera.http.api.websockets;

import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.core.services.Task;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class PingTask extends Task {

	private final ApiSocket socket;
	private final long interval;

	/**
	 * @param socket
	 */
	PingTask(ApiSocket socket) {
		super("WS Ping " + socket.getHttpRequest().getRemoteHost());
		this.socket = socket;
		this.interval = SystemNode.getConfiguration().get("http_ws_ping_interval", 10000l);
	}

	@Override
	protected void execute() {
		try {
			Thread.sleep(interval);
			socket.ping();
		} catch (InterruptedException e) {
		}
	}

}
