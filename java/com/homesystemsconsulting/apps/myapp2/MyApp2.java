package com.homesystemsconsulting.apps.myapp2;

import java.util.EventListener;

import com.google.common.eventbus.Subscribe;
import com.homesystemsconsulting.apps.Application;
import com.homesystemsconsulting.events.NumberEvent;


public class MyApp2 extends Application implements EventListener {

	@Override
	public boolean onInit() {
		return true;
	}
	
	@Override
	public boolean onQuit() {
		return true;
	}
	
	@Subscribe
    public void handleMyEvent(NumberEvent event) {
		log.debug("MyEvent: " + event.getId() + " = " + event.getValue());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {}
		log.debug("EXIT: " + event.getId() + " = " + event.getValue());
    }
}
