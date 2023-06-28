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

package cc.sferalabs.sfera.events;

import java.util.Objects;

/**
 * Class representing a Node.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Node {

	private final String id;
	
	/**
	 * @param id
	 * @param subId
	 * @throws IllegalArgumentException
	 */
	Node(String id, String subId) throws IllegalArgumentException {
		this.id = checkId(id) + "." + checkId(subId);
		Nodes.put(this);
	}

	/**
	 * Constructs a {@code Node} with the specified ID and adds it to the list
	 * of existing nodes.
	 * 
	 * @param id
	 *            the node ID
	 * @throws IllegalArgumentException
	 *             if a node with the same ID has been already created
	 */
	public Node(String id) throws IllegalArgumentException {
		this.id = checkId(id);
		Nodes.put(this);
	}

	/**
	 * @param id
	 * @return
	 */
	private static String checkId(String id) {
		Objects.requireNonNull(id, "ID cannot be null");
		if (id.contains(".")) {
			throw new IllegalArgumentException("Nodes' IDs cannot contain a '.'");
		}
		return id;
	}

	/**
	 * Returns the node ID
	 * 
	 * @return the node ID
	 */
	public final String getId() {
		return id;
	}

	/**
	 * Removes the node from the list of existing nodes.
	 */
	public void destroy() {
		Nodes.remove(id);
	}
}
