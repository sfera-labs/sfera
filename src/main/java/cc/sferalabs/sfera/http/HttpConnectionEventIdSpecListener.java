/**
 * 
 */
package cc.sferalabs.sfera.http;

import java.util.Objects;

import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.EventIdSpecListener;

/**
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class HttpConnectionEventIdSpecListener extends EventIdSpecListener {

	private final String connectionId;

	/**
	 * 
	 * @param spec
	 * @param connectionId
	 */
	protected HttpConnectionEventIdSpecListener(String spec, String connectionId) {
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
		if (event instanceof HttpConnectionEvent) {
			if (!connectionId.equals(((HttpConnectionEvent) event).getConnectionId())) {
				return false;
			}
		}
		return super.matches(event);
	}

}