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
	private final JsonMessage resp;

	/**
	 * 
	 * @param id
	 * @param value
	 * @param username
	 * @param resp
	 * @throws Exception
	 */
	public HttpEvent(String id, String value, String username, JsonMessage resp) throws Exception {
		super(httpNode, id, value);
		if (id == null) {
			throw new Exception("null event id");
		}
		if (value == null) {
			throw new Exception("null event value");
		}
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
	public void reply(String result) throws IOException {
		resp.sendResult(result);
	}

}
