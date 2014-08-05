package com.homesystemsconsulting.events;


public abstract class Event {
	
	protected final Node source;
	private final String id;
	private final long timestamp;

	public Event(Node source, String id) {
		this.timestamp = System.currentTimeMillis();
		this.source = source;
		this.id = source.getId() + "." + id;
	}

	public String getId() {
		return id;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public abstract Object getValue();
}
