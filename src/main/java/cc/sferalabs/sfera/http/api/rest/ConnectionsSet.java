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
public class ConnectionsSet implements Serializable {

	private static final long serialVersionUID = 4456127014431500700L;

	private transient Map<String, Connection> map;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Connection get(String id) {
		if (map == null) {
			return null;
		}
		return map.get(id);
	}

	/**
	 * 
	 * @param connection
	 * @return
	 */
	public Connection put(Connection connection) {
		if (map == null) {
			map = new HashMap<>();
		}
		return map.put(connection.getId(), connection);
	}

	/**
	 * @return the contained connections
	 */
	public Collection<Connection> values() {
		if (map == null) {
			return new ArrayList<>();
		}
		return map.values();
	}

	/**
	 * @param id
	 * @return
	 */
	Connection remove(String id) {
		if (map == null) {
			return null;
		}
		return map.remove(id);
	}

}
