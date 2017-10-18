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
 * Base interface for all the events
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public interface Event {

	/**
	 * Returns the event ID
	 * 
	 * @return the event ID
	 */
	public String getId();

	/**
	 * Returns the event ID without the source ID prefix
	 * 
	 * @return the event ID without the source ID prefix
	 */
	public String getSubId();

	/**
	 * Returns the source node that generated this event
	 * 
	 * @return the source node
	 */
	public Object getSource();

	/**
	 * Returns the timestamp in milliseconds of the moment this event was
	 * created
	 * 
	 * @return the event creation timestamp
	 */
	public long getTimestamp();

	/**
	 * Returns the value of this event
	 * 
	 * @return the value of this event
	 */
	public Object getValue();

	/**
	 * <p>
	 * Returns a simple-form value used in trigger conditions' comparisons.
	 * </p>
	 * <p>
	 * For events with simple values (i.e. boolean, numeric or String values)
	 * this method should just return the value returned by {@link #getValue()}.
	 * </p>
	 * <p>
	 * Events with structured values (e.g. objects with several attributes)
	 * should return a simple value that represents the attribute of the
	 * structured value that is most likely used in trigger conditions in
	 * scripts.
	 * </p>
	 * <p>
	 * For instance, an event representing a received text message, whose value
	 * is an object with a {@code sender}, a {@code sentTime} and a
	 * {@code content} attribute, might have this method returning the
	 * {@code content} String, so that the following trigger condition can be
	 * used:
	 * </p>
	 * 
	 * <pre>
	 *     node.message == "hello" : { ... }
	 * </pre>
	 * 
	 * @return the simple-form value of this event
	 */
	public Object getSimpleValue();
	
	/**
	 * Returns whether this event is local only (i.e. not reported by the Web API).
	 *  
	 * @return {@code true} if this event is local, {@code false} otherwise
	 */
	public boolean isLoacal();

}
