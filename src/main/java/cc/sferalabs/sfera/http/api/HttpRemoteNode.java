/**
 * 
 */
package cc.sferalabs.sfera.http.api;

import cc.sferalabs.sfera.events.Node;

/**
 * Node used as source for {@link RemoteApiEvent} events.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class HttpRemoteNode extends Node {

	private final static HttpRemoteNode INSTANCE = new HttpRemoteNode();

	/**
	 * Construct a HttpRemoteNode
	 */
	private HttpRemoteNode() {
		super("http");
	}

	/**
	 * @return the instance
	 */
	public static HttpRemoteNode getInstance() {
		return INSTANCE;
	}
}
