package cc.sferalabs.sfera.drivers.webserver.access;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cc.sferalabs.sfera.drivers.webserver.HttpRequestHeader;

public class Token {
	
	public static int maxAgeSeconds;
	
	private final String uuid;
	private final User user;
	private final String userAgent;
	private final long expirationTime;

	private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<String, Subscription>();

	/**
	 * 
	 * @param user
	 * @param httpRequestHeader
	 */
	public Token(User user, HttpRequestHeader httpRequestHeader) {
		this.uuid = UUID.randomUUID().toString();
		this.user = user;
		this.userAgent = httpRequestHeader.getUserAgent();
		this.expirationTime = System.currentTimeMillis() + (maxAgeSeconds * 1000);
	}

	/**
	 * 
	 * @return
	 */
	public String getUUID() {
		return uuid;
	}
	
	/**
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * 
	 * @param httpRequestHeader
	 * @return
	 */
	public boolean match(HttpRequestHeader httpRequestHeader) {
		return this.userAgent == null || this.userAgent.equals(httpRequestHeader.getUserAgent());
	}

	/**
	 * 
	 * @return
	 */
	public boolean isExpired() {
		return System.currentTimeMillis() > this.expirationTime;
	}

	/**
	 * 
	 * @param id
	 * @param nodes
	 */
	public String subscribe(String id, String nodes) {
		Subscription s = (id == null) ? null : subscriptions.get(id);
		if (s == null) {
			id = UUID.randomUUID().toString();
			s = new Subscription();
			subscriptions.put(id, s);
		}
		
		s.setNodes(nodes);
		
		return id;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Subscription getSubscription(String id) {
		return subscriptions.get(id);
	}
}
