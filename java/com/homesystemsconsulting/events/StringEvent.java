package com.homesystemsconsulting.events;


public class StringEvent extends Event {
	
	private final String value;

	public StringEvent(Node source, String id, String value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
