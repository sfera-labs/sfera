/**
 * 
 */
package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import cc.sferalabs.sfera.core.services.Task;
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
	private final Map<Path, UUID> uuids = new HashMap<>();

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
			uuids.put(path, FilesWatcher.register(path, new EventsSender(file), false));
		}
	}

	/**
	 * 
	 */
	void destroy() {
		for (Entry<Path, UUID> e : uuids.entrySet()) {
			FilesWatcher.unregister(e.getKey(), e.getValue());
		}
	}

	/**
	 *
	 * @author Giampiero Baggiani
	 *
	 * @version 1.0.0
	 *
	 */
	private class EventsSender extends Task {

		private final String path;

		/**
		 * @param path
		 */
		EventsSender(String path) {
			super("WS file events sender");
			this.path = path;
		}

		@Override
		protected void execute() {
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
