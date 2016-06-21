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

import cc.sferalabs.sfera.events.ObjectEvent;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class UIEvent extends ObjectEvent {

	private final String connectionId;

	/**
	 * 
	 * @param id
	 *            event ID
	 * @param value
	 *            event value
	 * @param connectionId
	 *            connection ID
	 */
	public UIEvent(String id, Object value, String connectionId) {
		super(UI.getInstance(), id, value);
		this.connectionId = connectionId;
	}

	/**
	 * @return the connection ID
	 */
	public String getConnectionId() {
		return connectionId;
	}

}
