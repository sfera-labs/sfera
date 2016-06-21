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

import java.util.Objects;

import cc.sferalabs.sfera.web.ConnectionEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ConnectionUISetEvent extends UISetEvent implements ConnectionEvent {

	/**
	 * Construct a ConnectionUISetEvent
	 * 
	 * @param componentId
	 *            the component ID
	 * @param attribute
	 *            the attribute to set
	 * @param value
	 *            the value to assign
	 * @param connectionId
	 *            the connection ID
	 */
	ConnectionUISetEvent(String componentId, String attribute, Object value, String connectionId) {
		super(componentId, attribute, value,
				Objects.requireNonNull(connectionId, "connectionId must not be null"));
	}

}