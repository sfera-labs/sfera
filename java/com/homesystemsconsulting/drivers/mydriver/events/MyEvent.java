package com.homesystemsconsulting.drivers.mydriver.events;

import com.homesystemsconsulting.events.BooleanEvent;
import com.homesystemsconsulting.events.Node;


public class MyEvent extends BooleanEvent {

	public MyEvent(Node source, String id, boolean value) {
		super(source, id, value);
	}
}
