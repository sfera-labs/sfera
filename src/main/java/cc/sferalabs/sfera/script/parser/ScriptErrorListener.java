package cc.sferalabs.sfera.script.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptErrorListener extends BaseErrorListener {

	private final List<Object> errors = new ArrayList<>();

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
			int charPositionInLine, String msg, RecognitionException e) {
		errors.add("line " + line + " - " + msg);
	}

	/**
	 * Returns the list of errors.
	 * 
	 * @return the list of errors
	 */
	public List<Object> getErrors() {
		return errors;
	}
}