package cc.sferalabs.sfera.http.api.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionsSet implements Serializable {

	private static final long serialVersionUID = 4456127014431500700L;

	private transient Map<String, PollingSubscriber> map;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public PollingSubscriber get(String id) {
		if (map == null) {
			return null;
		}
		return map.get(id);
	}

	/**
	 * 
	 * @param id
	 * @param subscription
	 * @return
	 */
	public PollingSubscriber put(String id, PollingSubscriber subscription) {
		if (map == null) {
			map = new HashMap<>();
		}
		return map.put(id, subscription);
	}

}
