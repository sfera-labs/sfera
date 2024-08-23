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

/**
 * 
 */
package cc.sferalabs.sfera.events;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.scripts.ScriptNodeWrapper;
import cc.sferalabs.sfera.scripts.ScriptsEngine;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Nodes {

	private static final Logger logger = LoggerFactory.getLogger(Nodes.class);
	private static final Map<String, Node> nodes = new HashMap<>();

	/**
	 * Adds the specified node to the collection of nodes accessible via the
	 * {@link #get(String)} method.
	 * 
	 * @param node
	 *            the node to add
	 * 
	 * @throws IllegalArgumentException
	 *             if a node with the same ID has been already added
	 */
	public synchronized static void put(Node node) throws IllegalArgumentException {
		String id = node.getId();
		if (nodes.containsKey(id)) {
			throw new IllegalArgumentException("Node with ID '" + id + "' already exists");
		}
		nodes.put(id, node);
		if (!(node instanceof ScriptNodeWrapper)) {
			ScriptsEngine.putObjectInGlobalScope(id, node);
		}
		logger.debug("Node '{}' added", id);
	}

	/**
	 * Returns the node with the specified ID, or {@code null} if no node with
	 * the specified ID has been added.
	 * 
	 * @param id
	 *            the ID of the node to be returned
	 * @return the node with the specified ID, or {@code null} if no node with
	 *         the specified ID has been added
	 */
	public synchronized static Node get(String id) {
		return nodes.get(id);
	}

	/**
	 * Removes the node with the specified ID if it is present, together with all
	 * its SubNode's
	 * 
	 * @param id
	 *            the ID of the node to be removed
	 * @return the removed node, or {@code null} if there was no node with the
	 *         specified ID
	 */
	synchronized static Node remove(String id) {
		ScriptsEngine.removeFromGlobalScope(id);
		Node n = nodes.remove(id);
		if (n != null) {
			logger.debug("Node '{}' removed", id);
			nodes.entrySet().removeIf(e -> {
				Node sn = e.getValue();
				if ((sn instanceof SubNode) && ((SubNode) sn).getParent() == n) {
					logger.debug("SubNode '{}' removed", sn.getId());
					ScriptsEngine.removeFromGlobalScope(sn.getId());
					return true;
				}
				return false;
			});
		}
		return n;
	}

}
