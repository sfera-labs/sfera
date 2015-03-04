package cc.sferalabs.sfera.core;

import cc.sferalabs.sfera.events.Node;

public class SystemNode implements Node {

	public static final SystemNode INSTANCE = new SystemNode();

	@Override
	public String getId() {
		return "system";
	}
}
