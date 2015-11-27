/**
 * 
 */
package cc.sferalabs.sfera.script.parser;

import java.io.IOException;
import java.io.Reader;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarLexer;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Parser {

	/**
	 * 
	 * @param input
	 * @param errorListener
	 * @return
	 */
	public static SferaScriptGrammarParser getParser(String input,
			ScriptErrorListener errorListener) {
		return getParser(new ANTLRInputStream(input), errorListener);
	}

	/**
	 * 
	 * @param r
	 * @param errorListener
	 * @return
	 * @throws IOException
	 */
	public static SferaScriptGrammarParser getParser(Reader r, ANTLRErrorListener errorListener)
			throws IOException {
		return getParser(new ANTLRInputStream(r), errorListener);
	}

	/**
	 * 
	 * @param input
	 * @param errorListener
	 * @return
	 */
	private static SferaScriptGrammarParser getParser(CharStream input,
			ANTLRErrorListener errorListener) {
		SferaScriptGrammarLexer lexer = new SferaScriptGrammarLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SferaScriptGrammarParser parser = new SferaScriptGrammarParser(tokens);

		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		return parser;
	}

}
