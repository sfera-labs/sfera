package cc.sferalabs.sfera.web.api.http.servlets;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.web.api.CommandExecutor;
import cc.sferalabs.sfera.web.api.ErrorMessage;
import cc.sferalabs.sfera.web.api.http.Connection;
import cc.sferalabs.sfera.web.api.http.HttpResponse;
import cc.sferalabs.sfera.web.api.http.MissingRequiredParamException;

/**
 * API servlet handling driver commands.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
@SuppressWarnings("serial")
public class CommandServlet extends ConnectionRequiredApiServlet {

	public static final String PATH = ApiServlet.PATH + "command";

	private static final Logger logger = LoggerFactory.getLogger(CommandServlet.class);

	@Override
	protected void processConnectionRequest(HttpServletRequest req, HttpResponse resp,
			Connection connection) throws ServletException, IOException {
		try {
			String cmd = getRequiredParameter("cmd", req, resp);
			Object res = null;
			try {
				res = CommandExecutor.exec(cmd, req, connection.getId(), req.getRemoteUser());
			} catch (IllegalArgumentException | ScriptException e) {
				logger.debug("Command error", e);
				resp.sendErrors(HttpServletResponse.SC_BAD_REQUEST,
						new ErrorMessage(0, "Command error: " + e.getMessage()));
				return;
			}
			resp.sendResult(res);
		} catch (MissingRequiredParamException e) {
		}

	}

}
