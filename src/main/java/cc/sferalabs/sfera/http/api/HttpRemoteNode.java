/**
 * 
 */
package cc.sferalabs.sfera.http.api;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.events.Node;

/**
 * Node used as source for {@link HttpApiEvent} events.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpRemoteNode implements Node {

	private final HttpServletRequest request;

	/**
	 * Construct a HttpRemoteNode
	 * 
	 * @param request
	 *            the request associated with this remote
	 */
	public HttpRemoteNode(HttpServletRequest request) {
		this.request = Objects.requireNonNull(request, "request must not be null");
	}

	@Override
	public String getId() {
		return "http";
	}

	/**
	 * Returns the user associated with this remote.
	 * 
	 * @return the user
	 */
	public User getUser() {
		return Access.getUser(request.getRemoteUser());
	}

	/**
	 * Returns the request associated with this remote.
	 * 
	 * @return the request
	 */
	public HttpServletRequest getHttpRequest() {
		return request;
	}

}
