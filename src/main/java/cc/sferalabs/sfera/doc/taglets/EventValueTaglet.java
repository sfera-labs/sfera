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

package cc.sferalabs.sfera.doc.taglets;

import java.util.Map;

/**
 * Taglet sfera.event_val
 * <p>
 * Usage:
 * <p>
 * <b>&#64;sfera.event_val</b> &lt;event_value&gt; &lt;description&gt;
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class EventValueTaglet extends BaseTaglet {

	@Override
	public String getName() {
		return "sfera.event_val";
	}

	@Override
	protected String getHeader() {
		return "Values:";
	}

	@Override
	public boolean inType() {
		return true;
	}

	/**
	 * Register this Taglet.
	 * 
	 * @param tagletMap
	 *            the map to register this tag to.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void register(Map tagletMap) {
		EventValueTaglet tag = new EventValueTaglet();
		tagletMap.put(tag.getName(), tag);
	}

}