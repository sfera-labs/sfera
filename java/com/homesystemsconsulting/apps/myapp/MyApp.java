package com.homesystemsconsulting.apps.myapp;

import java.util.EventListener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.homesystemsconsulting.apps.Application;
import com.homesystemsconsulting.events.NumberEvent;


public class MyApp extends Application implements EventListener {

	public MyApp(String id) {
		super(id);
	}
	
	@Override
	public boolean onRemove() {
		return true;
	}
	
	@Subscribe
	@AllowConcurrentEvents
    public void handleMyEvent(NumberEvent event) throws Exception {
		log.debug("MyEvent: " + event.getId() + " = " + event.getValue());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {}
		log.debug("EXIT: " + event.getId() + " = " + event.getValue());
    }
}
