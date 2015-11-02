package cc.sferalabs.sfera.http.api.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A set of {@link PollingSubscription}s.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class SubscriptionsSet implements Serializable {

	private static final long serialVersionUID = 4456127014431500700L;

	private transient Map<String, PollingSubscription> map;

	/**
	 * 
	 * @param id
	 * @return
	 */
	PollingSubscription get(String id) {
		if (map == null) {
			return null;
		}
		return map.get(id);
	}

	/**
	 * 
	 * @param subscription
	 * @return
	 */
	PollingSubscription put(PollingSubscription subscription) {
		if (map == null) {
			map = new HashMap<>();
		}
		return map.put(subscription.getId(), subscription);
	}

	/**
	 * @return the contained subscriptions
	 */
	public Collection<PollingSubscription> values() {
		if (map == null) {
			return new ArrayList<>();
		}
		return map.values();
	}

	/**
	 * @param id
	 * @return
	 */
	PollingSubscription remove(String id) {
		if (map == null) {
			return null;
		}
		return map.remove(id);
	}

}
