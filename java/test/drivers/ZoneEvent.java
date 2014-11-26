package test.drivers;

import cc.sferalabs.sfera.events.BooleanEvent;


public class ZoneEvent extends BooleanEvent {

	public static TestDriver SOURCE;
	private int index;

	public ZoneEvent(int index, boolean value) {
		super(SOURCE, "zone(" + index + ").active", value);
		this.index = index;
	}
	
	public int getZoneIndex() {
		return index;
	}
}
