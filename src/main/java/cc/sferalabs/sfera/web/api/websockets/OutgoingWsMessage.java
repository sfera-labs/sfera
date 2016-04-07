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
