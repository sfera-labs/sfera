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
	 * 
	 * @param sessionId
	 */
	public Connection() {
		this.id = "" + count.getAndIncrement();
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param nodes
	 */
	public void subscribe(String nodes) {
		if (subscription != null) {
			subscription.destroy();
		}
		subscription = new PollingSubscription(nodes, id);
	}

	/**
	 * 
	 */
	public void destroy() {
		if (subscription != null) {
			subscription.destroy();
		}
	}

	/**
	 * @return
	 */
	public PollingSubscription getSubscription() {
		return subscription;
	}

}
