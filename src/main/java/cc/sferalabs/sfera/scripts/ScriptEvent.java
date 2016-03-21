/**
 * 
 */
package cc.sferalabs.sfera.scripts;

import cc.sferalabs.sfera.events.Event;

/**
 * Class for events generated by script nodes.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptEvent implements Event {

	private final Object source;
	private final String id;
	private final String subId;
	private final long timestamp;
	private final Object value;

	/**
	 * Constructor.
	 * 
	 * @param source
	 *            the source node
	 * @param sourceId
	 *            the source node ID
	 * @param id
	 *            the event ID
	 * @param value
	 *            the event value
	 */
	ScriptEvent(Object source, String sourceId, String id, Object value) {
		this.timestamp = System.currentTimeMillis();
		this.source = source;
		this.id = sourceId + "." + id;
		this.subId = id;
		this.value = value;
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
	public Object getSource() {
		return source;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Object getScriptConditionValue() {
		return getValue();
	}

}