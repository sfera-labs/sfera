package cc.sferalabs.sfera.events;

import cc.sferalabs.sfera.core.SystemNode;

public class SystemEvent extends BaseEvent {
	
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
