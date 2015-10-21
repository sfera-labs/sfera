package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;

import cc.sferalabs.sfera.http.api.JsonMessage;

/**
 * Class representing an outgoing message for a WebSocket connection.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class OutgoingMessage extends JsonMessage {

	private final ApiSocket socket;

	/**
	 * Constructs an OutgoingMessage with the specified type and for the
	 * specified WebSocket.
	 * 
	 * @param type
	 *            the type of message
	 * @param socket
	 *            the WebSocket to send the message on
	 */
	OutgoingMessage(String type, ApiSocket socket) {
		this.socket = socket;
		put("type", type);
	}

	@Override
	protected void doSend(String text) throws IOException {
		socket.send(text);
	}

}
