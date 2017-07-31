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
package cc.sferalabs.sfera.ui;

import cc.sferalabs.sfera.core.services.AutoStartService;
import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Node;

/**
 * Class representing the UI node.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class UI extends Node implements AutoStartService {

	private static UI INSTANCE;

	/**
	 * 
	 */
	public UI() {
		super("ui");
	}

	@Override
	public void init() throws Exception {
		INSTANCE = this;
	}

	@Override
	public void quit() throws Exception {
	}

	/**
	 * Returns the UI node instance.
	 * 
	 * @return the UI node instance
	 */
	public static UI getInstance() {
		return INSTANCE;
	}

	/**
	 * Sets the specified attribute of the specified component to the specified
	 * value.
	 * 
	 * @param componentId
	 *            ID of the addressed component
	 * @param attribute
	 *            attribute to set
	 * @param value
	 *            value to assign
	 */
	public void set(String componentId, String attribute, Object value) {
		Bus.post(new UISetEvent(componentId, attribute, value, null));
	}

	/**
	 * Sets the specified attribute of the specified component to the specified
	 * value only for the specified connection.
	 * 
	 * @param componentId
	 *            ID of the addressed component
	 * @param attribute
	 *            attribute to set
	 * @param value
	 *            value to assign
	 * @param connectionId
	 *            ID of the addressed connection
	 */
	public void set(String componentId, String attribute, Object value, String connectionId) {
		Bus.post(new ConnectionUISetEvent(componentId, attribute, value, connectionId));
	}

}
