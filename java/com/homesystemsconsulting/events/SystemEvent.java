package com.homesystemsconsulting.events;

import com.homesystemsconsulting.core.SystemNode;

public class SystemEvent extends Event {
	
	private final Object value;

	public SystemEvent(String id, Object value) {
		super(SystemNode.INSTANCE, id);
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}
}
