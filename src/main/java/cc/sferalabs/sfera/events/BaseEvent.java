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

import java.lang.ref.WeakReference;

/**
 * Base {@link Event} implementation.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class BaseEvent implements Event {

	private final WeakReference<Node> source;
	private final String id;
	private final String subId;
	private final long timestamp;

	/**
	 * Constructs a {@code BaseEvent} event with the specified ID and the specified
	 * {@code Node} as source.
	 * 
	 * @param source
	 *            the source node
	 * @param id
	 *            the event ID
	 */
	public BaseEvent(Node source, String id) {
		this.timestamp = System.currentTimeMillis();
		this.source = new WeakReference<Node>(source);
		this.id = source.getId() + "." + id;
		this.subId = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getSubId() {
		return subId;
	}

	@Override
	public Node getSource() {
		return source.get();
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public Object getSimpleValue() {
		return getValue();
	}

	@Override
	public boolean isLoacal() {
		return false;
	}
}
