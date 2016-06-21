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
package cc.sferalabs.sfera.web.api.websockets;

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
	 *            the WS socket
	 * @param pingInterval
	 *            the ping messages interval
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
