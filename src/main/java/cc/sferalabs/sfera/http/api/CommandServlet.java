package cc.sferalabs.sfera.http.api;

import java.io.IOException;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.drivers.Driver;
import cc.sferalabs.sfera.drivers.Drivers;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarLexer;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.TerminalNodeContext;

@SuppressWarnings("serial")
public class CommandServlet extends AuthorizedApiServlet {

	public static final String PATH = ApiServlet.PATH + "command";

	private final static Logger logger = LogManager.getLogger();
	private static final ScriptEngine scriptEngine = new ScriptEngineManager()
			.getEngineByName("nashorn");

	@Override
	protected void processAuthorizedRequest(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		logger.info("Command: {} User: {}", req.getParameterMap(),
				req.getRemoteUser());

		for (Entry<String, String[]> command : req.getParameterMap().entrySet()) {
			String cmdName = command.getKey();
			for (String cmdValue : command.getValue()) {
				CommandSyntaxErrorListener commandErrorListener = new CommandSyntaxErrorListener();

				SferaScriptGrammarParser parser = getParser(cmdName,
						commandErrorListener);
				TerminalNodeContext commandContext = parser.terminalNode();
				String error = commandErrorListener.getError();
				if (error != null) {
					commandError(cmdName, cmdValue, "invalid node syntax: "
							+ error, resp);
					return;
				}

				String driverName = commandContext.NodeId().getText();
				Driver driver = Drivers.getDriver(driverName);

				if (driver == null) {
					commandError(cmdName, cmdValue, "driver '" + driverName
							+ "' not found", resp);
					return;
				}

				String commandScript = cmdName;
				if (cmdValue != null) {
					parser = getParser(cmdValue, commandErrorListener);
					parser.paramsList();
					error = commandErrorListener.getError();
					if (error != null) {
						commandError(cmdName, cmdValue,
								"invalid value syntax: " + error, resp);
						return;
					}
					commandScript += "=" + cmdValue;

				} else if (!commandScript.endsWith(")")) {
					commandScript += "()";
				}

				Bindings bindings = new SimpleBindings();
				bindings.put(driverName, driver);
				scriptEngine.eval(commandScript, bindings);
			}

		}

		// TODO write response
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @param msg
	 * @param resp
	 * @throws IOException
	 */
	private void commandError(String name, String value, String msg,
			HttpServletResponse resp) throws IOException {
		msg = "Command '" + name + "=" + value + "' error: " + msg;
		logger.error(msg);
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
	}

	/**
	 * 
	 * @param input
	 * @param commandSyntaxErrorListener
	 * @return
	 */
	private static SferaScriptGrammarParser getParser(String input,
			CommandSyntaxErrorListener commandSyntaxErrorListener) {
		SferaScriptGrammarLexer lexer = new SferaScriptGrammarLexer(
				new ANTLRInputStream(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SferaScriptGrammarParser parser = new SferaScriptGrammarParser(tokens);

		lexer.removeErrorListeners();
		lexer.addErrorListener(commandSyntaxErrorListener);
		parser.removeErrorListeners();
		parser.addErrorListener(commandSyntaxErrorListener);

		return parser;
	}

	/**
	 * 
	 */
	class CommandSyntaxErrorListener extends BaseErrorListener {

		private String error = null;

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
				Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			error = msg;
		}

		/**
		 * 
		 * @return
		 */
		public String getError() {
			return error;
		}
	}

}
