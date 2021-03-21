/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

/**
 * 
 */
package cc.sferalabs.sfera.web.api.http;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Connection {

	private static final AtomicLong count = new AtomicLong(77);

	private final String id;
	private final HttpSession session;
	private PollingSubscription subscription;

	/**
	 * Constructs a {@code Connection}.
	 * 
	 * @param session
	 *            the session owning this connection
	 */
	public Connection(HttpSession session) {
		this.id = "" + count.getAndIncrement();
		this.session = session;
	}

	/**
	 * Returns the connection ID.
	 * 
	 * @return the connection ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the session owning this connection.
	 * 
	 * @return the session owning this connection
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * Creates a subscription to the specified nodes.
	 * 
	 * @param nodes
	 *            the nodes IDs specification
	 */
	public void subscribe(String nodes) {
		unsubscribe();
		subscription = new PollingSubscription(nodes, this);
	}

	/**
	 * Terminates the current subscription (if any).
	 */
	public void unsubscribe() {
		if (subscription != null) {
			subscription.destroy();
			subscription = null;
		}
	}

	/**
	 * Returns whether this connection has a subscription.
	 * 
	 * @return {@code true} if this connection has a subscription, {@code false}
	 *         otherwise
	 */
	public boolean isSubscribed() {
		return subscription != null;
	}

	/**
	 * Destroys this connection.
	 */
	public void destroy() {
		unsubscribe();
	}

	/**
	 * Returns the subscription.
	 * 
	 * @return the subscription
	 */
	public PollingSubscription getSubscription() {
		return subscription;
	}

}
