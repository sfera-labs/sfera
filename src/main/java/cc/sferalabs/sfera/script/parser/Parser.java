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
 * Utility class for building {@link SferaScriptGrammarParser}s.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public abstract class Parser {

	/**
	 * Builds a {@link SferaScriptGrammarParser} for the specified reader and
	 * with the specified error listener.
	 * 
	 * @param input
	 *            the input
	 * @param errorListener
	 *            the error listener
	 * @return the built parser
	 */
	public static SferaScriptGrammarParser getParser(String input,
			ScriptErrorListener errorListener) {
		return getParser(new ANTLRInputStream(input), errorListener);
	}

	/**
	 * Builds a {@link SferaScriptGrammarParser} for the specified reader and
	 * with the specified error listener.
	 * 
	 * @param r
	 *            the reader
	 * @param errorListener
	 *            the error listener
	 * @return the built parser
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static SferaScriptGrammarParser getParser(Reader r, ANTLRErrorListener errorListener)
			throws IOException {
		return getParser(new ANTLRInputStream(r), errorListener);
	}

	/**
	 * Builds a {@link SferaScriptGrammarParser} for the specified input and
	 * with the specified error listener.
	 * 
	 * @param input
	 *            the input
	 * @param errorListener
	 *            the error listener
	 * @return the built parser
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
