package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.HttpApiEvent;

/**
 * <p>
 * API servlet handlig event triggering requests.
 * </p>
 * <p>
 * A successful request to this servlet will trigger an {@link HttpApiEvent}
 * event whose source is an {@link cc.sferalabs.sfera.http.api.HttpRemoteNode
 * HttpRemoteNode} instance, the ID will correspond to the value of the
 * specified 'eid' request parameter and the value must be specified in the
 * 'eval' request parameter.
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

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		String eid = req.getParameter("eid");
		String eval = req.getParameter("eval");
		try {
			resp.setAsyncContext(req.startAsync());
			HttpApiEvent remoteEvent = new HttpApiEvent(eid, eval, req, resp);
			Bus.post(remoteEvent);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

}
