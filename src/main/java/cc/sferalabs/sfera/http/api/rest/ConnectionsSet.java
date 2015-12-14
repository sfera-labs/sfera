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
	 * Returns the connection with the specified ID.
	 * 
	 * @param id
	 *            the ID of the connection to be returned
	 * @return the connection with the specified ID
	 */
	public Connection get(String id) {
		if (map == null) {
			return null;
		}
		return map.get(id);
	}

	/**
	 * Adds the specified connection to this set.
	 * 
	 * @param connection
	 *            the connection to add
	 */
	public void put(Connection connection) {
		if (map == null) {
			map = new HashMap<>();
		}
		map.put(connection.getId(), connection);
	}

	/**
	 * Returns the contained connections.
	 * 
	 * @return the contained connections
	 */
	public Collection<Connection> values() {
		if (map == null) {
			return new ArrayList<>();
		}
		return map.values();
	}

	/**
	 * Removes the connection with the specified ID.
	 * 
	 * @param id
	 *            the ID of the connection to be removed
	 * @return the removed connection
	 */
	Connection remove(String id) {
		if (map == null) {
			return null;
		}
		return map.remove(id);
	}

}
