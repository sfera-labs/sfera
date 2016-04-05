/**
 * 
 */
package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import cc.sferalabs.sfera.util.files.FilesWatcher;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class WsFileWatcher {

	private final ApiSocket socket;
	private final Set<UUID> uuids = new HashSet<>();

	/**
	 * 
	 * @param socket
	 *            the WS socket
	 * @param files
	 *            comma-separated list of file names
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	WsFileWatcher(ApiSocket socket, String files) throws IOException {
		this.socket = socket;
		for (String file : files.split(",")) {
			Path path = Paths.get(file);
			uuids.add(FilesWatcher.register(path, "WebSocket file events sender",
					new EventsSender(file), false, false));
		}
	}

	/**
	 * 
	 */
	void destroy() {
		for (UUID uuid : uuids) {
			FilesWatcher.unregister(uuid);
		}
	}

	/**
	 *
	 * @author Giampiero Baggiani
	 *
	 * @version 1.0.0
	 *
	 */
	private class EventsSender implements Runnable {

		private final String path;

		/**
		 * @param path
		 */
		EventsSender(String path) {
			this.path = path;
		}

		@Override
		public void run() {
			try {
				OutgoingWsMessage msg = new OutgoingWsMessage("event", socket);
				Map<String, Long> eventsMap = new HashMap<>();
				eventsMap.put(path, System.currentTimeMillis());
				msg.send("files", eventsMap);
			} catch (Exception e) {
				socket.onWebSocketError(e);
			}
		}

	}

}
