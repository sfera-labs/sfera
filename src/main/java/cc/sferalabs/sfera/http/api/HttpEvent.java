package cc.sferalabs.sfera.http.api;

import java.io.IOException;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.StringEvent;

public class HttpEvent extends StringEvent {

	/**
	 *
	 */
	private static class HttpNode implements Node {

		@Override
		public String getId() {
			return "http";
		}
	}

	private static final HttpNode httpNode = new HttpNode();

	private final String username;
	private final HttpResponse resp;
	private boolean handled = false;

	/**
	 * 
	 * @param id
	 * @param value
	 * @param username
	 * @param resp
	 */
	public HttpEvent(String id, String value, String username, HttpResponse resp) {
		super(httpNode, id, value);
		this.username = username;
		this.resp = resp;
	}

	/**
	 * 
	 * @return
	 */
	public User getUser() {
		return Access.getUser(username);
	}

	/**
	 * 
	 * @param result
	 * @throws IOException
	 *             if unable to send the response
	 * @throws IllegalStateException
	 *             if this event has already been handled
	 */
	public synchronized void reply(String result) throws IOException {
		if (handled) {
			throw new IllegalStateException("Already handled");
		}
		resp.sendResult(result);
		handled = true;
	}

}
