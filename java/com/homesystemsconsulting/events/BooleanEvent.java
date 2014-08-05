package com.homesystemsconsulting.events;


public class BooleanEvent extends Event {
	
	private final boolean value;

	public BooleanEvent(Node source, String id, Boolean value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

}
