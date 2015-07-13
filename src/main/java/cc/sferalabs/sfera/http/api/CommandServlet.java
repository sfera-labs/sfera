package cc.sferalabs.sfera.http.api;

import java.util.Map.Entry;

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
		logger.info("Command: {} User: {}", req.getParameterMap(), req.getRemoteUser());

		for (Entry<String, String[]> command : req.getParameterMap().entrySet()) {
			String cmdName = command.getKey();
			for (String cmdValue : command.getValue()) {
				try {
					ScriptsEngine.executeDriverCommand(cmdName, cmdValue);
				} catch (Exception e) {
					String msg = e.getMessage();
					msg = "Command '" + cmdName + "=" + cmdValue + "' error: " + msg;
					logger.error(msg);
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
				}
			}
		}

		// TODO write response
	}

}
