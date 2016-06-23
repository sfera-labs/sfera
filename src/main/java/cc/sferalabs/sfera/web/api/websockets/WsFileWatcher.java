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

package cc.sferalabs.sfera.web.api.websockets;

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
