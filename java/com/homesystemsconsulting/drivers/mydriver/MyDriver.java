package com.homesystemsconsulting.drivers.mydriver;

import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.events.Bus;
import com.homesystemsconsulting.events.NumberEvent;
import com.homesystemsconsulting.events.StringEvent;


public class MyDriver extends Driver {
	
	private int count = 0;

	public MyDriver(String id) {
		super(id);
	}
	
	@Override
	public boolean onInit() {
		return true;
	}

	@Override
	public boolean loop() throws InterruptedException {
		
		Thread.sleep(1000);
		
		Bus.post(new NumberEvent(this, "count", count++));
		
		Thread.sleep(1000);
		
		Bus.postIfChanged(new StringEvent(this, "text", "sssss"));
		
//		post(new StringEvent(this, "text", "foo"));
		
//		log.error("Ciaoooooo!");
//		log.warning("Ciaoooooo!");
//		log.info("Ciaoooooo!");
//		log.debug("Ciaoooooo!");
//		log.verbose("Ciaoooooo!");
		
//		System.out.println(System.currentTimeMillis() + " Posting dp1");
//		post(new MyEvent(this, "dp1", true));
//		System.out.println(System.currentTimeMillis() + " Posted dp1");
//		System.out.println(System.currentTimeMillis() + " Posting dp2");
//		post(new MyEvent2(this, "dp2", "ciao"));
//		System.out.println(System.currentTimeMillis() + " Posted dp2");
//		System.out.println(System.currentTimeMillis() + " Posting dp3");
//		post(new MyEvent(this, "dp3", true));
//		System.out.println(System.currentTimeMillis() + " Posted dp3");
		
//		Thread.sleep(1000);
		
//		System.out.println(System.currentTimeMillis() + " Posting dp1");
//		post(new MyEvent(this, "dp1", false));
//		System.out.println(System.currentTimeMillis() + " Posted dp1");
//		System.out.println(System.currentTimeMillis() + " Posting dp2");
//		post(new MyEvent2(this, "dp2", "mamma"));
//		System.out.println(System.currentTimeMillis() + " Posted dp2");
//		System.out.println(System.currentTimeMillis() + " Posting dp3");
//		post(new MyEvent(this, "dp3", false));
//		System.out.println(System.currentTimeMillis() + " Posted dp3");
		
//		Thread.sleep(3000);
		
		return true;
	}
	
	@Override
	public void onQuit() {
		// TODO Auto-generated method stub
	}
	
	public void doSomething(String param) {
		System.out.println(getId() + ": wait for it...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		System.out.println(getId() + ": doing something with " + param);
	}
}
