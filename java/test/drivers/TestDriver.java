package test.drivers;

import java.util.ArrayList;
import java.util.List;

import com.homesystemsconsulting.core.Configuration;
import com.homesystemsconsulting.drivers.Driver;
import com.homesystemsconsulting.events.Bus;
import com.homesystemsconsulting.events.NumberEvent;
import com.homesystemsconsulting.events.StringEvent;

public class TestDriver extends Driver {
	
	public class Zone {
		boolean active;
		
		public boolean getActive() {
			return active;
		}
		
		public void setActive(boolean a) {
			active = a;
		}
	}
	
	private int cc = 0;
	private List<Zone> zz = new ArrayList<TestDriver.Zone>();

	public TestDriver(String id) {
		super(id);
		ZoneEvent.SOURCE = this;
		for (int i = 0; i < 10; i++) 
			zz.add(new Zone());
	}

	@Override
	protected boolean onInit(Configuration configuration)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean loop() throws InterruptedException {
		Thread.sleep(1000);
		
		Bus.post(new NumberEvent(this, "count", cc++));
		
		Thread.sleep(1000);
		
		Bus.post(new StringEvent(this, "text", "s" + cc));
		
		for (int i = 0; i < zz.size(); i++) {
			Zone z = zz.get(i);
			Bus.postIfChanged(new ZoneEvent(i, z.active));
		}
		
		
		
		/*
		post(new StringEvent(this, "text", "foo"));
		
		log.error("Ciaoooooo!");
		log.warning("Ciaoooooo!");
		log.info("Ciaoooooo!");
		log.debug("Ciaoooooo!");
		log.verbose("Ciaoooooo!");
		
		System.out.println(System.currentTimeMillis() + " Posting dp1");
		post(new MyEvent(this, "dp1", true));
		System.out.println(System.currentTimeMillis() + " Posted dp1");
		System.out.println(System.currentTimeMillis() + " Posting dp2");
		post(new MyEvent2(this, "dp2", "ciao"));
		System.out.println(System.currentTimeMillis() + " Posted dp2");
		System.out.println(System.currentTimeMillis() + " Posting dp3");
		post(new MyEvent(this, "dp3", true));
		System.out.println(System.currentTimeMillis() + " Posted dp3");
		
		Thread.sleep(1000);
		
		System.out.println(System.currentTimeMillis() + " Posting dp1");
		post(new MyEvent(this, "dp1", false));
		System.out.println(System.currentTimeMillis() + " Posted dp1");
		System.out.println(System.currentTimeMillis() + " Posting dp2");
		post(new MyEvent2(this, "dp2", "mamma"));
		System.out.println(System.currentTimeMillis() + " Posted dp2");
		System.out.println(System.currentTimeMillis() + " Posting dp3");
		post(new MyEvent(this, "dp3", false));
		System.out.println(System.currentTimeMillis() + " Posted dp3");
		
		Thread.sleep(3000);
		*/
		
		return true;
	}

	public int getCount() {
		return cc;
	}
	
	public String getText() {
		return "s" + cc;
	}
	
	public void setCount(int c) {
		cc = c;
	}
	
	public Zone zone(int index) {
		return zz.get(index);
	}
	
	@Override
	protected void onQuit() throws InterruptedException {
		// TODO Auto-generated method stub

	}
}
