/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

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
