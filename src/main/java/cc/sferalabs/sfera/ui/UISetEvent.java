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

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class UISetEvent extends UIEvent {

	/**
	 * Construct a UISetEvent
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
	UISetEvent(String componentId, String attribute, Object value, String connectionId) {
		super("set." + (connectionId == null ? "global" : connectionId) + "."
				+ Objects.requireNonNull(componentId, "componentId must not be null") + "."
				+ Objects.requireNonNull(attribute, "attribute must not be null"), value,
				connectionId);
	}

}
