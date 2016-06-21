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

import cc.sferalabs.sfera.events.Node;

/**
 * Wrapper for nodes defined in scripts.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptNodeWrapper extends Node {

	private final Object scriptNode;

	/**
	 * Constructs a ScriptNodeWrapper.
	 * 
	 * @param id
	 *            the node ID
	 * @param scriptNode
	 *            the script node to wrap
	 */
	public ScriptNodeWrapper(String id, Object scriptNode) {
		super(id);
		this.scriptNode = scriptNode;
	}

	/**
	 * Returns the script node.
	 * 
	 * @return the script node
	 */
	public Object getScriptNode() {
		return scriptNode;
	}

}
