package com.homesystemsconsulting.drivers.webserver;

import com.homesystemsconsulting.events.Node;

public class GUINode implements Node {
	
	public static final GUINode INSTANCE = new GUINode();
	
	/**
	 * Private constructor for singleton instance
	 */
	private GUINode() {}

	@Override
	public String getId() {
		return "gui";
	}
}
