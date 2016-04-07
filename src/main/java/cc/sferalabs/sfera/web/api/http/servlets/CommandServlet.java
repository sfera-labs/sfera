package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.CommandExecutor;
import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.RestResponse;

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
		Connection connection = ConnectServlet.getConnection(req, resp);
		if (connection == null) {
			return;
		}
		String cmd = req.getParameter("cmd");
		if (cmd == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Command not specified");
			return;
		}
		Object res = null;
		try {
			res = CommandExecutor.exec(cmd, req, connection.getId(), req.getRemoteUser());
		} catch (IllegalArgumentException | ScriptException e) {
			logger.debug("Command error", e);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Command error: " + e.getMessage());
			return;
		}
		resp.sendResult(res);
	}

}
