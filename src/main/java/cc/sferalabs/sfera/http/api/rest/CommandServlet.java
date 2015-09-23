package cc.sferalabs.sfera.http.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.script.ScriptsEngine;

@SuppressWarnings("serial")
public class CommandServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "command";

	private final static Logger logger = LoggerFactory.getLogger(CommandServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws Exception {
		try {
			Object res = null;
			for (String command : req.getParameterMap().keySet()) {
				if (command.indexOf('.') > 0) { // driver command
					String param = req.getParameter(command);
					res = ScriptsEngine.executeDriverAction(command, param, req.getRemoteUser());
					break;
				}
			}
			resp.sendResult(res);

		} catch (Exception e) {
			logger.error("Command error", e);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Command error: " + e.getMessage());
		}
	}

}
