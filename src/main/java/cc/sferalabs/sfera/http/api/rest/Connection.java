/**
 * 
 */
package cc.sferalabs.sfera.http.api.rest;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Connection {

	private static final AtomicLong count = new AtomicLong(77);

	private final String id;
	private PollingSubscription subscription;

	/**
	 * Constructs a {@code Connection}.
	 */
	public Connection() {
		this.id = "" + count.getAndIncrement();
	}

	/**
	 * Returns the connection ID.
	 * 
	 * @return the connection ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Creates a subscription to the specified nodes.
	 * 
	 * @param nodes
	 *            the nodes IDs specification
	 */
	public void subscribe(String nodes) {
		if (subscription != null) {
			subscription.destroy();
		}
		subscription = new PollingSubscription(nodes, id);
	}

	/**
	 * Destroys this connection.
	 */
	public void destroy() {
		if (subscription != null) {
			subscription.destroy();
		}
	}

	/**
	 * Returns the subscription.
	 * 
	 * @return the subscription
	 */
	public PollingSubscription getSubscription() {
		return subscription;
	}

}
