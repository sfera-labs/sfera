package cc.sferalabs.sfera.http.api;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cc.sferalabs.sfera.events.StringEvent;

/**
 * Event generated when requested via remote API.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpApiEvent extends StringEvent {

	/**
	 * Construct a RemoteEvent
	 * 
	 * @param id
	 *            the event id
	 * @param value
	 *            the event value
	 * @param request
	 *            the request associated with this event
	 * @param response
	 *            the response object to be used to send a reply
	 * @throws NullPointerException
	 *             if any of the parameters are {@code null}
	 */
	public HttpApiEvent(String id, String value, HttpServletRequest request, JsonMessage response)
			throws NullPointerException {
		super(new HttpRemoteNode(request, response),
				Objects.requireNonNull(id, "id must not be null"),
				Objects.requireNonNull(value, "value must not be null"));
	}

	@Override
	public HttpRemoteNode getSource() {
		return (HttpRemoteNode) super.getSource();
	}

}
