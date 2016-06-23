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
