package cc.sferalabs.sfera.drivers.iono;

import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.NumberEvent;

public class IonoAnalogEvent extends NumberEvent implements IonoEvent {
	
	public IonoAnalogEvent(Node source, String id, Object object) {
		super(source, id, (Double) object);
	}

}
