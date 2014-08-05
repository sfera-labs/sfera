package com.homesystemsconsulting.events;


public class NumberEvent extends Event {
	
	private final double value;

	public NumberEvent(Node source, String id, double value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

}
