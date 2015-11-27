package cc.sferalabs.sfera.http.api;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.events.StringEvent;
import cc.sferalabs.sfera.http.api.rest.EventServlet;

/**
 * Event generated when requested via remote API through the
 * {@link EventServlet} servlet.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpApiEvent extends StringEvent {

	private final HttpServletRequest request;

	/**
	 * Construct a RemoteEvent
	 * 
	 * @param id
	 *            the event id
	 * @param value
	 *            the event value
	 * @param request
	 *            the request associated with this event
	 * @throws NullPointerException
	 *             if any of the parameters are {@code null}
	 */
	public HttpApiEvent(String id, String value, HttpServletRequest request)
			throws NullPointerException {
		super(HttpRemoteNode.getInstance(), Objects.requireNonNull(id, "id must not be null"),
				value);
		this.request = Objects.requireNonNull(request, "request must not be null");
	}

	/**
	 * Returns the user associated with this event.
	 * 
	 * @return the user
	 */
	public User getUser() {
		return Access.getUser(request.getRemoteUser());
	}

	/**
	 * Returns the HTTP request associated with this event.
	 * 
	 * @return the request
	 */
	public HttpServletRequest getHttpRequest() {
		return request;
	}

}
