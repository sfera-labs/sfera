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

import cc.sferalabs.sfera.web.api.OutgoingMessage;

/**
 * Class representing an outgoing message for a WebSocket connection.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class OutgoingWsMessage extends OutgoingMessage {

	private final ApiSocket socket;

	/**
	 * Constructs an OutgoingWsMessage with the specified type and for the
	 * specified WebSocket.
	 * 
	 * @param type
	 *            the type of message
	 * @param socket
	 *            the WebSocket to send the message on
	 */
	OutgoingWsMessage(String type, ApiSocket socket) {
		this.socket = socket;
		put("type", type);
	}

	@Override
	protected void doSend(String text) throws IOException {
		socket.send(text);
	}

}
