package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.HttpApiEvent;

/**
 * <p>
 * API servlet handling requests for events triggering.
 * </p>
 * <p>
 * The triggered events will be instances of {@link HttpApiEvent} and the source
 * node an {@link cc.sferalabs.sfera.http.api.HttpRemoteNode HttpRemoteNode}
 * instance.
 * </p>
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class EventServlet extends AuthorizedUserServlet {

	public static final String PATH = ApiServlet.PATH + "event";

	private static final Logger logger = LoggerFactory.getLogger(EventServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		Enumeration<String> params = req.getParameterNames();
		if (!params.hasMoreElements()) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No event specified");
			return;
		}

		String id = params.nextElement();
		String val = req.getParameter(id);
		HttpApiEvent remoteEvent = new HttpApiEvent(id, val, req);
		logger.info("Event: {} = {} User: {}", id, val, req.getRemoteUser());
		Bus.post(remoteEvent);
		resp.sendResult("ok");
	}

}
