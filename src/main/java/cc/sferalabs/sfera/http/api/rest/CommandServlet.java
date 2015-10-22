package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.script.ScriptsEngine;

/**
 * API servlet handling driver commands.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class CommandServlet extends AuthorizedUserServlet {

	public static final String PATH = ApiServlet.PATH + "command";

	private final static Logger logger = LoggerFactory.getLogger(CommandServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
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
