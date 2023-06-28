/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.web.api.http;

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

	private static final int CONNECTIONS_MAX = 500;

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
		if (map.size() >= CONNECTIONS_MAX) {
			throw new IllegalStateException("Too many connections");
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
