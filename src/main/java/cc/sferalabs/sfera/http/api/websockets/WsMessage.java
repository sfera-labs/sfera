package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;

import cc.sferalabs.sfera.http.api.JsonMessage;

class WsMessage extends JsonMessage {

	private final ApiSocket socket;

	/**
	 * 
	 * @param type
	 * @param socket
	 */
	WsMessage(String type, ApiSocket socket) {
		this.socket = socket;
		put("type", type);
	}

	@Override
	protected void doSend(String text) throws IOException {
		socket.getRemote().sendString(text);
	}

}
