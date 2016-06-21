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

/**
 * Abstract class for events with numeric value.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class NumberEvent extends BaseEvent {

	private final Number value;

	/**
	 * Constructs a {@code NumberEvent} event with the specified ID, source and
	 * value.
	 * 
	 * @param source
	 *            the source node
	 * @param id
	 *            the event ID
	 * @param value
	 *            the event value
	 */
	public NumberEvent(Node source, String id, Number value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public Number getValue() {
		return value;
	}

}
