package cc.sferalabs.sfera.http.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.http.api.HttpEvent;

@SuppressWarnings("serial")
public class EventServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "event";

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws Exception {
		String eid = req.getParameter("eid");
		String eval = req.getParameter("eval");
		try {
			resp.setAsyncContext(req.startAsync());
			HttpEvent httpEvent = new HttpEvent(eid, eval, req.getRemoteUser(), resp);
			Bus.post(httpEvent);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

}