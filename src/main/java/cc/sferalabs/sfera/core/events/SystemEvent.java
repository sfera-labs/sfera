package cc.sferalabs.sfera.core.events;

import cc.sferalabs.sfera.core.SystemNode;
import cc.sferalabs.sfera.events.BaseEvent;

public abstract class SystemEvent extends BaseEvent {

	public SystemEvent(String id) {
		super(SystemNode.getInstance(), id);
	}

}
