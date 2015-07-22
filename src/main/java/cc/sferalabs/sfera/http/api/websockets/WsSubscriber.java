package cc.sferalabs.sfera.http.api.websockets;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.NodesSpecSubscriber;

public class WsSubscriber extends NodesSpecSubscriber {

	private static final Logger logger = LogManager.getLogger();

	private final RemoteEndpoint remote;
	private boolean isLast;

	/**
	 * 
	 * @param remote
	 * @param spec
	 */
	public WsSubscriber(RemoteEndpoint remote, String spec) {
		super();
		this.remote = remote;
		isLast = false;
		setNodesSpec(spec);
		for (Event e : Bus.getCurrentState().values()) {
			process(e);
		}
		isLast = true;
		sendMessage("", true);
	}

	@Override
	protected void handleEvent(Event event) {
		sendMessage(event.getId() + "=" + event.getValue() + ";", isLast);
	}

	/**
	 * 
	 * @param msg
	 * @param isLast
	 */
	private void sendMessage(String msg, boolean isLast) {
		logger.debug("Sending message: {}", msg);
		try {
			remote.sendPartialString(msg, isLast);
		} catch (IOException e) {
			// TODO add retry mechanism or drop connection
			logger.error("Error sending event", e);
		}
	}
}
