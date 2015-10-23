/**
 * 
 */
package cc.sferalabs.sfera.http;

import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.events.EventIdSpecListener;
import cc.sferalabs.sfera.ui.UISessionSetEvent;

/**
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class SessionFilterEventIdSpecListener extends EventIdSpecListener {

	private final String session;

	/**
	 * 
	 * @param spec
	 * @param session
	 */
	protected SessionFilterEventIdSpecListener(String spec, String session) {
		super(spec);
		this.session = session;
	}

	@Override
	protected boolean matches(Event event) {
		if (event instanceof UISessionSetEvent) {
			String thisSession = session;
			if (!thisSession.equals(((UISessionSetEvent) event).getSession())) {
				return false;
			}
		}
		return super.matches(event);
	}

}
