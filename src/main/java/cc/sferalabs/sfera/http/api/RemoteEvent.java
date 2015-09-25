package cc.sferalabs.sfera.http.api;

import java.io.IOException;
import java.util.Objects;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.StringEvent;

/**
 * Event generated when requested via remote API.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class RemoteEvent extends StringEvent {

	/**
	 *
	 */
	private static class RemoteNode implements Node {

		@Override
		public String getId() {
			return "remote";
		}
	}

	private static final RemoteNode REMOTE_NODE = new RemoteNode();

	private final String username;
	private final JsonMessage resp;

	/**
	 * Construct a RemoteEvent
	 * 
	 * @param id
	 *            the event id
	 * @param value
	 *            the event value
	 * @param username
	 *            the username of the user associated to this event
	 * @param resp
	 *            the response object to be used to send a reply
	 * @throws NullPointerException
	 *             if {@code id} or {@code value} are {@code null}
	 */
	public RemoteEvent(String id, String value, String username, JsonMessage resp)
			throws NullPointerException {
		super(REMOTE_NODE, Objects.requireNonNull(id, "id must not be null"),
				Objects.requireNonNull(value, "value must not be null"));
		this.username = username;
		this.resp = resp;
	}

	/**
	 * Returns the user associated to this event.
	 * 
	 * @return the user
	 */
	public User getUser() {
		return Access.getUser(username);
	}

	/**
	 * Sends the specified result as a response to the remote which generated
	 * the event.
	 * 
	 * @param result
	 *            the result to send
	 * @throws IOException
	 *             if unable to send the response
	 * @throws IllegalStateException
	 *             if this event has already been handled
	 */
	public void reply(String result) throws IOException {
		resp.sendResult(result);
	}

}
