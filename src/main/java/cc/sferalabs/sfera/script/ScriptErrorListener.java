package cc.sferalabs.sfera.script;

import java.util.ArrayList;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ScriptErrorListener extends BaseErrorListener {
    
	public final ArrayList<String> errors = new ArrayList<String>();
	
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e) {

        errors.add("line " + line + " - " + msg);
    }
}