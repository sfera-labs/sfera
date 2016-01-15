/**
 * 
 */
package cc.sferalabs.sfera.script;

import javax.script.ScriptException;

import cc.sferalabs.sfera.console.ConsoleCommandHandler;
import cc.sferalabs.sfera.script.parser.Rule;
import cc.sferalabs.sfera.script.parser.ScriptsLoader;

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
	public String accept(String cmd) {
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

	@Override
	public String[] getHelp() {
		return new String[] { "eval { <script> }", "eval <file> <line_num>" };
	}

}
