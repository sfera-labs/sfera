package cc.sferalabs.sfera.script;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.antlr.v4.runtime.misc.NotNull;

import cc.sferalabs.sfera.script.parser.SferaScriptGrammarBaseListener;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.GlobalScopeInitContext;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.LocalScopeInitContext;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.RuleLineContext;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.StableEventContext;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.TransientEventContext;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.TriggerContext;

public class ScriptGrammarListener extends SferaScriptGrammarBaseListener {
	
	private final Compilable engine;
	private final Path scriptFile;

	final HashMap<String, HashSet<Rule>> triggerActionsMap = new HashMap<String, HashSet<Rule>>();
	final ArrayList<String> errors = new ArrayList<String>();
	final Bindings localScope;
	
	private Rule currentRule;
	
	/**
	 * 
	 * @param scriptFile
	 * @param fileSystem 
	 * @param engine
	 */
	public ScriptGrammarListener(Path scriptFile, FileSystem fileSystem, Compilable engine) {
		ScriptEngine se = (ScriptEngine) engine;
		this.scriptFile = scriptFile;
		this.engine = engine;
		this.localScope = se.createBindings();
		
		se.put(ScriptLoader.VAR_NAME, new ScriptLoader(se, fileSystem, se.getBindings(ScriptContext.ENGINE_SCOPE)));
		this.localScope.put(ScriptLoader.VAR_NAME, new ScriptLoader(se, fileSystem, localScope));
	}
	
	@Override
	public void enterLocalScopeInit(@NotNull LocalScopeInitContext ctx) {
		String action = ctx.Script().getText();
		action = action.substring(1, action.length() - 1);
		try {
			CompiledScript cs = engine.compile(action);
			
			try {
				cs.eval(localScope);
			} catch (Throwable e) {
				int line = ctx.getStart().getLine();
				if (e instanceof ScriptException) {
					if (((ScriptException) e).getLineNumber() >= 0) {
						line += ((ScriptException) e).getLineNumber() - 1;
					}
				}
				errors.add("line " + line + " - evaluation error: " + e);
			}
			
		} catch (Throwable e) {
			int line = ctx.getStart().getLine();
			if (e instanceof ScriptException) {
				if (((ScriptException) e).getLineNumber() >= 0) {
					line += ((ScriptException) e).getLineNumber() - 1;
				}
			}
			errors.add("line " + line + " - compilation error: " + e);
		}
	}
	
	@Override
	public void enterGlobalScopeInit(@NotNull GlobalScopeInitContext ctx) {
		String action = ctx.Script().getText();
		action = action.substring(1, action.length() - 1);
		try {
			CompiledScript cs = engine.compile(action);
			
			try {
				cs.eval();
			} catch (Throwable e) {
				int line = ctx.getStart().getLine();
				if (e instanceof ScriptException) {
					if (((ScriptException) e).getLineNumber() >= 0) {
						line += ((ScriptException) e).getLineNumber() - 1;
					}
				}
				errors.add("line " + line + " - evaluation error: " + e);
			}
			
		} catch (Throwable e) {
			int line = ctx.getStart().getLine();
			if (e instanceof ScriptException) {
				if (((ScriptException) e).getLineNumber() >= 0) {
					line += ((ScriptException) e).getLineNumber() - 1;
				}
			}
			errors.add("line " + line + " - compilation error: " + e);
		}
	}

	@Override
	public void enterRuleLine(@NotNull RuleLineContext ctx) {
		currentRule = null;
		TriggerContext condition = ctx.trigger();
		String action = ctx.action().Script().getText();
		action = action.substring(1, action.length() - 1);
		try {
			currentRule = new Rule(condition, action, scriptFile, engine, localScope);
		} catch (ScriptException e) {
			int line = ctx.getStart().getLine();
			if (e.getLineNumber() >= 0) {
				line += e.getLineNumber() - 1;
			}
			errors.add("line " + line + " - " + e);
		}
	}
	
	@Override
	public void enterStableEvent(@NotNull StableEventContext ctx) {
		addTrigger(ctx.getChild(0).getChild(0).getText());
	}

	@Override
	public void enterTransientEvent(@NotNull TransientEventContext ctx) {
		addTrigger(ctx.getChild(0).getText());
	}
	
	/**
	 * 
	 * @param id
	 */
	private void addTrigger(String id) {
		if (currentRule != null) {
			HashSet<Rule> actions = triggerActionsMap.get(id);
			if (actions == null) {
				actions = new HashSet<Rule>();
				triggerActionsMap.put(id, actions);
			}
			actions.add(currentRule);
		}
	}
}
