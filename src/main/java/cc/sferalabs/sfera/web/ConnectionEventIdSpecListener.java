/**
 * 
 */
package cc.sferalabs.sfera.web;

import java.util.Objects;

import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.EventIdSpecListener;

/**
 * Extension of {@link EventIdSpecListener} filtering {@link ConnectionEvent}
 * events.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class ConnectionEventIdSpecListener extends EventIdSpecListener {

	private final String connectionId;

	/**
	 * Construct a HttpConnectionEventIdSpecListener.
	 * 
	 * @param spec
	 *            the event ID specification
	 * @param connectionId
	 *            the connection ID to be used to filter {@link ConnectionEvent}
	 *            events
	 */
	protected ConnectionEventIdSpecListener(String spec, String connectionId) {
		super(spec);
		this.connectionId = Objects.requireNonNull(connectionId, "connectionId must not be null");
	}

	/**
	 * Returns the connection ID
	 * 
	 * @return the connection ID
	 */
	public String getConnectionId() {
		return connectionId;
	}

	@Override
	protected boolean matches(Event event) {
		if (event instanceof ConnectionEvent) {
			if (!connectionId.equals(((ConnectionEvent) event).getConnectionId())) {
				return false;
			}
		}
		return super.matches(event);
	}

}