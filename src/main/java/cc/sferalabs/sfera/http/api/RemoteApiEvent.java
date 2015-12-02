package cc.sferalabs.sfera.http.api;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cc.sferalabs.sfera.access.Access;
import cc.sferalabs.sfera.access.User;
import cc.sferalabs.sfera.events.StringEvent;

/**
 * Event generated when requested via remote API.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class RemoteApiEvent extends StringEvent {

	private final HttpServletRequest request;
	private final String connectionId;

	/**
	 * Construct a RemoteEvent
	 * 
	 * @param id
	 *            the event id
	 * @param value
	 *            the event value
	 * @param request
	 *            the request associated with this event
	 * @param connectionId
	 *            the connection ID associated with this event
	 * @throws NullPointerException
	 *             if any of the parameters are {@code null}
	 */
	public RemoteApiEvent(String id, String value, HttpServletRequest request, String connectionId)
			throws NullPointerException {
		super(HttpRemoteNode.getInstance(), Objects.requireNonNull(id, "id must not be null"),
				value);
		this.request = Objects.requireNonNull(request, "request must not be null");
		this.connectionId = connectionId;
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

	/**
	 * Returns the connection ID associated with this event.
	 * 
	 * @return the connection ID associated with this event
	 */
	public String getConnectionId() {
		return connectionId;
	}

}
