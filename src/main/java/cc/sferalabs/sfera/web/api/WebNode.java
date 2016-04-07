/**
 * 
 */
package cc.sferalabs.sfera.web.api;

import cc.sferalabs.sfera.events.Node;

/**
 * Node used as source for {@link WebApiEvent} events.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class WebNode extends Node {

	private final static WebNode INSTANCE = new WebNode();

	/**
	 * Construct a HttpRemoteNode
	 */
	private WebNode() {
		super("web");
	}

	/**
	 * @return the instance
	 */
	public static WebNode getInstance() {
		return INSTANCE;
	}
}
