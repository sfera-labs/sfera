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

package cc.sferalabs.sfera.web.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Abstract class for API JSON messages.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class JsonMessage {

	private final JSONObject obj;

	/**
	 * @param obj
	 */
	private JsonMessage(JSONObject obj) {
		this.obj = obj;
	}

	/**
	 * Constructs a JSON message parsing the specified string.
	 * 
	 * @param json the string to parse
	 */
	public JsonMessage(String json) {
		this(new JSONObject(json));
	}

	/**
	 * Constructs an empty JSON message.
	 */
	public JsonMessage() {
		this(new JSONObject());
	}

	/**
	 * Sets the attribute of this message specified by {@code key} to the specified
	 * {@code value}.
	 * 
	 * @param key   attribute to set
	 * @param value value for the attribute to set
	 */
	public void put(String key, Object value) {
		if (value == null) {
			value = JSONObject.NULL;
		}
		obj.put(key, value);
	}

	/**
	 * Returns the value of the attribute of this message specified by {@code key},
	 * or {@code null} if this message does not have such attribute.
	 * 
	 * @param <T> Type to cast the returned value to
	 * 
	 * @param key attribute to get
	 * @return the value of the attribute, or {@code null} if no such attribute is
	 *         found
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) obj.opt(key);
	}

	/**
	 * Returns a set containing the attributes of this message.
	 * 
	 * @return a set containing the attributes of this message
	 */
	public Set<String> getAttributes() {
		return obj.keySet();
	}

	/**
	 * Returns this JSON message in string format.
	 * 
	 * @return this JSON message in string format
	 * @throws JSONException If the object contains an invalid number
	 */
	public String toJsonString() throws JSONException {
		try {
			return obj.toString(0);
		} catch (Throwable t) {
			if (t instanceof JSONException) {
				throw t;
			}
			throw getError(obj.toMap(), null, t);
		}
	}

	/**
	 * @param map
	 * @param key
	 * @param err
	 * @return
	 */
	private static JSONException getError(Map<?, ?> map, String key, Throwable err) {
		Object k = null;
		Object v = null;
		String vStr = null;
		try {
			for (Entry<?, ?> me : map.entrySet()) {
				k = me.getKey();
				v = me.getValue();
				try {
					HashMap<String, Object> l = new HashMap<>();
					l.put(k.toString(), v);
					JSONObject jo = new JSONObject(l);

					jo.toString(0);

					k = null;
					v = null;
				} catch (Throwable t) {
					break;
				}
			}
			if (v != null) {
				key = key == null ? k.toString() : (key + " > " + k);
				map = null;
				if (v instanceof Map) {
					map = (Map<?, ?>) v;
				}
				if (v instanceof JSONObject) {
					map = ((JSONObject) v).toMap();
				}
				if (map != null) {
					return getError(map, key, err);
				}
			}
		} catch (Throwable t) {
		}

		try {
			vStr = v.toString();
		} catch (Throwable ve) {
			vStr = null;
		}

		return new JSONException("Error writing JSON value for '" + key + "' (" + vStr + "): " + err, err);
	}

}
