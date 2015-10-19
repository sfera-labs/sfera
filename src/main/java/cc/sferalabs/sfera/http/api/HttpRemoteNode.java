/**
 * 
 */
package cc.sferalabs.sfera.http.api;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.events.Node;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpRemoteNode implements Node {

	private final HttpServletRequest request;
	private final String username;
	private final JsonMessage response;

	/**
	 * @param req
	 */
	public HttpRemoteNode(HttpServletRequest request, String username, JsonMessage response) {
		this.request = Objects.requireNonNull(request, "request must not be null");
		this.username = Objects.requireNonNull(username, "username must not be null");
		this.response = Objects.requireNonNull(response, "response must not be null");
	}

	@Override
	public String getId() {
		return "http";
	}

	/**
	 * Returns the user associated with this event.
	 * 
	 * @return the user
	 */
	public User getUser() {
		return Access.getUser(username);
	}

	/**
	 * Returns the request associated with this remote.
	 * 
	 * @return the request
	 */
	public HttpServletRequest getHttpRequest() {
		return request;
	}

	/**
	 * Sends the specified result as a response to the remote.
	 * 
	 * @param result
	 *            the result to send
	 * @throws IOException
	 *             if unable to send the response
	 * @throws IllegalStateException
	 *             if this event has already been handled
	 */
	public void reply(String result) throws IOException {
		response.sendResult(result);
	}

}
