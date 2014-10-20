package test.apps;

import java.util.EventListener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.homesystemsconsulting.apps.Application;
import com.homesystemsconsulting.events.NumberEvent;


public class TestApp extends Application implements EventListener {

	public TestApp(String id) {
		super(id);
	}
	
	@Override
	public boolean onRemove() {
		return true;
	}
	
	@Subscribe
	@AllowConcurrentEvents
    public void handleMyEvent(NumberEvent event) throws Exception {
		log.info("NumberEvent: " + event.getId() + " = " + event.getValue());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {}
		log.info("EXIT: " + event.getId() + " = " + event.getValue());
    }
}
