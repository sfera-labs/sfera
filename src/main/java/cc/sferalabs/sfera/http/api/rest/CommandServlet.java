package cc.sferalabs.sfera.http.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.script.ScriptsEngine;

@SuppressWarnings("serial")
public class CommandServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "command";

	private final static Logger logger = LogManager.getLogger();

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		try {
			Object res = null;
			for (String command : req.getParameterMap().keySet()) {
				if (command.indexOf('.') > 0) { // driver command
					String param = req.getParameter(command);
					res = ScriptsEngine.executeDriverCommand(command, param, req.getRemoteUser());
					break;
				}
			}

			RestResponse rr = new RestResponse(resp);
			rr.put("result", res);
			rr.send();

		} catch (Exception e) {
			logger.error("Command error", e);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Command error: " + e.getMessage());
		}
	}

}
