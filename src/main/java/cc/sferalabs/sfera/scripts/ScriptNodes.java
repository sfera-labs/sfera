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
package cc.sferalabs.sfera.scripts;

import java.util.ArrayList;
import java.util.List;

import cc.sferalabs.sfera.events.Bus;

/**
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class ScriptNodes {

	private static List<ScriptNodeWrapper> nodes = new ArrayList<>();

	/**
	 * Adds the specified script node to the collection of available nodes,
	 * wrapped in a {@link ScriptNodeWrapper}.
	 * <p>
	 * This method is solely called from sfera.js.
	 * </p>
	 * 
	 * @param id
	 *            the node ID
	 * @param node
	 *            the script node to be added
	 */
	public synchronized static void put(String id, Object node) {
		ScriptNodeWrapper wrap = new ScriptNodeWrapper(id, node);
		ScriptsEngine.putObjectInGlobalScope(id, node);
		nodes.add(wrap);
	}

	/**
	 * Destroys all the added nodes and clears the list.
	 */
	public synchronized static void clear() {
		for (ScriptNodeWrapper wrap : nodes) {
			wrap.destroy();
		}
		nodes.clear();
	}

	/**
	 * Posts a {@link ScriptEvent} to the Bus.
	 * <p>
	 * This method is solely called from sfera.js.
	 * </p>
	 * 
	 * @param source
	 *            the source node
	 * @param sourceId
	 *            the source node's ID
	 * @param id
	 *            the events ID
	 * @param value
	 *            the event value
	 */
	public synchronized static void postEvent(Object source, String sourceId, String id,
			Object value) {
		Bus.post(new ScriptEvent(source, sourceId, id, value));
	}

}
