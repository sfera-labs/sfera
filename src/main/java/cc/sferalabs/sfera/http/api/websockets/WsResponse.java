package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;

import cc.sferalabs.sfera.http.api.HttpResponse;

class WsResponse extends HttpResponse {

	private final ApiSocket socket;

	/**
	 * 
	 * @param socket
	 */
	WsResponse(ApiSocket socket) {
		this.socket = socket;
	}

	@Override
	protected void doSend(String text) throws IOException {
		socket.getRemote().sendString(text);
	}

}
