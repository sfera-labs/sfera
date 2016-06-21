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
package cc.sferalabs.sfera.scripts;

import javax.script.ScriptException;

import cc.sferalabs.sfera.console.ConsoleCommandHandler;
import cc.sferalabs.sfera.console.ConsoleSession;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptsConsoleCommandHandler implements ConsoleCommandHandler {

	static final ScriptsConsoleCommandHandler INSTANCE = new ScriptsConsoleCommandHandler();

	/**
	 * 
	 */
	private ScriptsConsoleCommandHandler() {
	}

	@Override
	public String getKey() {
		return "script";
	}

	@Override
	public String accept(String cmd, ConsoleSession session) {
		if (cmd.startsWith("eval ")) {
			String script = cmd.substring(5).trim();
			if (script.startsWith("{")) {
				if (!script.endsWith("}")) {
					return "Syntax error: closing '}' missing";
				}
				script = script.substring(1, script.length() - 1).trim();
				try {
					ScriptsEngine.eval(script, null);
					return null;
				} catch (ScriptException e) {
					return "Error evaluating script: " + e;
				}
			} else {
				String[] file_line = script.split(" ");
				String file = file_line[0].trim();
				int line = Integer.parseInt(file_line[1].trim());
				for (Rule r : ScriptsEngine.getRules()) {
					if (r.getScriptFile().toString().equals(ScriptsLoader.SCRIPTS_DIR + "/" + file)
							&& r.getStartLine() == line) {
						r.executeAction(null);
						return null;
					}
				}
				return "Error: no rule found";
			}
		} else {
			return "Unkown command";
		}
	}

}
