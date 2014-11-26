package cc.sferalabs.sfera.drivers.iono;

import cc.sferalabs.sfera.events.BooleanEvent;
import cc.sferalabs.sfera.events.Node;

public class IonoDigitalEvent extends BooleanEvent implements IonoEvent {

	public IonoDigitalEvent(Node source, String id, Object object) {
		super(source, id, ((Long) object) == 1);
	}

}
