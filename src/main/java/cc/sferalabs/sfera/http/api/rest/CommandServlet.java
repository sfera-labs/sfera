package cc.sferalabs.sfera.http.api.rest;

import java.io.IOException;
import java.util.Enumeration;

import javax.script.ScriptException;
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

	private static final Logger logger = LoggerFactory.getLogger(CommandServlet.class);

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req, RestResponse resp)
			throws ServletException, IOException {
		Enumeration<String> params = req.getParameterNames();
		if (!params.hasMoreElements()) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No command specified");
			return;
		}

		String command = params.nextElement();
		String param = req.getParameter(command);
		if (param != null && !param.isEmpty()) {
			command += "=" + param;
		}
		Object res = null;
		try {
			logger.info("Command: {} User: {}", command, req.getRemoteUser());
			res = ScriptsEngine.evalNodeAction(command);
		} catch (IllegalArgumentException | ScriptException e) {
			logger.debug("Command error", e);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Command error: " + e.getMessage());
			return;
		}
		resp.sendResult(res);
	}

}
