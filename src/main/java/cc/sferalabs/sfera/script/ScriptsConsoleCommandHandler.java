/**
 * 
 */
package cc.sferalabs.sfera.script;

import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger logger = LoggerFactory
			.getLogger(ScriptsConsoleCommandHandler.class);

	static final ScriptsConsoleCommandHandler INSTANCE = new ScriptsConsoleCommandHandler();

	/**
	 * 
	 */
	private ScriptsConsoleCommandHandler() {
	}

	@Override
	public void accept(String cmd) {
		if (cmd.startsWith("eval ")) {
			String script = cmd.substring(5).trim();
			if (script.startsWith("{")) {
				if (!script.endsWith("}")) {
					logger.warn("Missing closing '}'");
					return;
				}
				script = script.substring(1, script.length() - 1).trim();
				try {
					ScriptsEngine.eval(script, null);
				} catch (ScriptException e) {
					logger.warn("Error evaluating script", e);
				}
			} else {
				String[] file_line = script.split(" ");
				String file = file_line[0].trim();
				int line = Integer.parseInt(file_line[1].trim());
				for (Rule r : ScriptsEngine.getRules()) {
					if (r.getScriptFile().toString().equals(ScriptsLoader.SCRIPTS_DIR + "/" + file)
							&& r.getStartLine() == line) {
						r.executeAction(null);
						return;
					}
				}
				logger.warn("No rule found");
			}
		} else {
			logger.warn("Unkown command");
		}
	}

	@Override
	public String[] getHelp() {
		return new String[] { "eval { <script> }", "eval <file> <line_num>" };
	}

}
