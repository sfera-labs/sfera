package com.homesystemsconsulting.drivers.mydriver.events;

import com.homesystemsconsulting.events.Node;
import com.homesystemsconsulting.events.StringEvent;

public class MyEvent2 extends StringEvent {

	public MyEvent2(Node source, String id, String value) {
		super(source, id, value);
	}
}
