package cc.sferalabs.sfera.web;

import java.util.EventListener;

import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.SessionHandler;

import com.google.common.eventbus.Subscribe;

import cc.sferalabs.sfera.access.AccessChangeEvent;
import cc.sferalabs.sfera.events.Bus;

/**
 * Session chache taking care of invalidating sessions when their associated
 * user is updated.
 *
 * @author giampiero
 *
 */
public class AuthenticationSessionCache extends DefaultSessionCache implements EventListener {

	/**
	 * Construct an {@link AuthenticationSessionCache}
	 * 
	 * @param manager
	 */
	AuthenticationSessionCache(SessionHandler manager) {
		super(manager);
		Bus.register(this);
	}

	@Subscribe
	public void onAccessChangeEvent(AccessChangeEvent e) {
		for (HttpSession s : _sessions.values()) {
			try {
				String username = (String) s.getAttribute(AuthenticationRequestWrapper.SESSION_ATTR_USERNAME);
				if (username != null && username.equals(e.getValue())) {
					s.invalidate();
				}
			} catch (Exception ex) {
			}
		}
	}

}
