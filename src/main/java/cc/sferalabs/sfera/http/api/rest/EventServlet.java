package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.RemoteEvent;

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
			RemoteEvent remoteEvent = new RemoteEvent(eid, eval, req.getRemoteUser(), resp);
			Bus.post(remoteEvent);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

}
