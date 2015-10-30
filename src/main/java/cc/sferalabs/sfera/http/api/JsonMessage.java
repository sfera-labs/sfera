package cc.sferalabs.sfera.http.api;

import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Abstract class for API JSON messages.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class JsonMessage {

	private static final JSONParser PARSER = new JSONParser();

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
	 * @param json
	 *            the string to parse
	 * @throws ParseException
	 *             if a parsing error occurs
	 */
	public JsonMessage(String json) throws ParseException {
		this((JSONObject) PARSER.parse(json));
	}

	/**
	 * Constructs an empty JSON message.
	 */
	public JsonMessage() {
		this(new JSONObject());
	}

	/**
	 * Sets the attribute of this message specified by {@code key} to the
	 * specified {@code value}.
	 * 
	 * @param key
	 *            attribute to set
	 * @param value
	 *            value for the attribute to set
	 */
	@SuppressWarnings("unchecked")
	public void put(String key, Object value) {
		obj.put(key, value);
	}

	/**
	 * Returns the value of the attribute of this message specified by
	 * {@code key}, or {@code null} if this message does not have such
	 * attribute.
	 * 
	 * @param <T>
	 *            Type to cast the returned value to
	 * 
	 * @param key
	 *            attribute to get
	 * @return the value of the attribute, or {@code null} if no such attribute
	 *         is found
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) obj.get(key);
	}

	/**
	 * Returns a set containing the attributes of this message.
	 * 
	 * @return a set containing the attributes of this message
	 */
	@SuppressWarnings("unchecked")
	public Set<String> getAttributes() {
		return obj.keySet();
	}

	/**
	 * Returns this JSON message in string format.
	 * 
	 * @return this JSON message in string format
	 */
	protected String toJsonString() {
		return obj.toJSONString();
	}

}
