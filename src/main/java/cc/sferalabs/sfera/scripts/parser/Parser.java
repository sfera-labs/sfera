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
package cc.sferalabs.sfera.scripts.parser;

import java.io.IOException;
import java.io.Reader;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarLexer;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser;

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
