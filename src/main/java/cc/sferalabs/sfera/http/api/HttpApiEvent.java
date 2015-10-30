package cc.sferalabs.sfera.http.api;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

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

	private final OutgoingMessage response;

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
	public HttpApiEvent(String id, String value, HttpServletRequest request, OutgoingMessage response)
			throws NullPointerException {
		super(new HttpRemoteNode(request), Objects.requireNonNull(id, "id must not be null"),
				Objects.requireNonNull(value, "value must not be null"));
		this.response = Objects.requireNonNull(response, "response must not be null");
	}

	@Override
	public HttpRemoteNode getSource() {
		return (HttpRemoteNode) super.getSource();
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
	public void reply(Object result) throws IOException {
		response.sendResult(result);
	}

}
