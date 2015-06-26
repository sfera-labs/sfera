package cc.sferalabs.sfera.http.api;

import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;

import com.google.common.eventbus.Subscribe;

public class Subscription implements EventListener {

	private final String id;
	private final BlockingQueue<Event> changes = new LinkedBlockingQueue<Event>();
	private Set<String> nodes;
	private long lastAckTs;
	private Map<String, Event> lastPolled = new HashMap<String, Event>();

	/**
	 * 
	 */
	public Subscription() {
		this.id = UUID.randomUUID().toString();
		Bus.register(this);
	}

	/**
	 * 
	 * @param nodes
	 */
	public void setNodes(String nodes) {
		if (!nodes.equals("*")) {
			String[] ns = nodes.split(",");
			this.nodes = new HashSet<String>(Arrays.asList(ns));
		} else {
			this.nodes = null;
		}

		for (Event e : Bus.getCurrentState().values()) {
			monitorChanges(e);
		}
	}

	/**
	 * 
	 * @param ackTs
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized Map<String, Event> pollChanges(long ackTs,
			long timeout, TimeUnit unit) throws InterruptedException {
		Map<String, Event> map;
		if (ackTs > lastAckTs) {
			map = new HashMap<String, Event>();
		} else {
			map = lastPolled;
			if (map.size() > 0) {
				timeout = 0;
			}
		}
		Event e = changes.poll(timeout, unit);
		while (e != null) {
			map.put(e.getId(), e);
			e = changes.poll();
		}

		lastAckTs = ackTs;
		lastPolled = map;

		return map;
	}

	/**
	 * 
	 * @param event
	 */
	@Subscribe
	public void monitorChanges(Event event) {
		if (nodes == null || nodes.contains(event.getId())) {
			changes.add(event);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}
}
