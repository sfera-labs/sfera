/**
 * 
 */
package cc.sferalabs.sfera.web.api;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.scripts.ScriptsEngine;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class CommandExecutor {

	private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

	public static Object exec(String cmd, HttpServletRequest httpRequest, String connectionId,
			String user) throws IllegalArgumentException, ScriptException {
		logger.info("Command: {} User: {}", cmd, user);
		Map<String, Object> b = new HashMap<>();
		b.put("httpRequest", httpRequest);
		b.put("connectionId", connectionId);
		return ScriptsEngine.evalNodeAction(cmd, b);
	}

}
