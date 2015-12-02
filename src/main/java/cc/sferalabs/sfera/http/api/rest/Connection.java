/**
 * 
 */
package cc.sferalabs.sfera.http.api.rest;

import java.util.UUID;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Connection {

	private final String id;
	private PollingSubscription subscription;

	/**
	 * 
	 */
	public Connection() {
		this.id = UUID.randomUUID().toString();
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
