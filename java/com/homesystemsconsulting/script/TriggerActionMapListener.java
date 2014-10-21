package com.homesystemsconsulting.script;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.script.Compilable;
import javax.script.ScriptException;

import org.antlr.v4.runtime.misc.NotNull;

import com.homesystemsconsulting.script.parser.SferaScriptGrammarBaseListener;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarParser.InitContext;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarParser.RuleLineContext;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarParser.StableEventContext;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarParser.TransientEventContext;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarParser.TriggerContext;

public class TriggerActionMapListener extends SferaScriptGrammarBaseListener {
	
	public final HashMap<String, HashSet<Rule>> triggerActionsMap = new HashMap<String, HashSet<Rule>>();
	public final ArrayList<String> errors = new ArrayList<String>();
	public final Path scriptFile;
	private final Compilable engine;
	
	private Rule currentConditionAction;
	
	/**
	 * 
	 * @param scriptFile
	 * @param engine
	 */
	public TriggerActionMapListener(Path scriptFile, Compilable engine) {
		this.scriptFile = scriptFile;
		this.engine = engine;
	}
	
	@Override
	public void enterInit(@NotNull InitContext ctx) {
		String action = ctx.Script().getText();
		action = action.substring(1, action.length() - 1);
		try {
			engine.compile(action).eval();
		} catch (Throwable e) {
			int line = ctx.getStart().getLine();
			if (e instanceof ScriptException) {
				if (((ScriptException) e).getLineNumber() >= 0) {
					line += ((ScriptException) e).getLineNumber() - 1;
				}
			}
			errors.add("line " + line + ": " + e.getLocalizedMessage());
		}
	}

	@Override
	public void enterRuleLine(@NotNull RuleLineContext ctx) {
		currentConditionAction = null;
		TriggerContext condition = ctx.trigger();
		String action = ctx.action().Script().getText();
		action = action.substring(1, action.length() - 1);
		try {
			currentConditionAction = new Rule(condition, action, scriptFile, engine);
		} catch (ScriptException e) {
			int line = ctx.getStart().getLine();
			if (e.getLineNumber() >= 0) {
				line += e.getLineNumber() - 1;
			}
			errors.add("line " + line + ": " + e.getLocalizedMessage());
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
		if (currentConditionAction != null) {
			HashSet<Rule> actions = triggerActionsMap.get(id);
			if (actions == null) {
				actions = new HashSet<Rule>();
				triggerActionsMap.put(id, actions);
			}
			actions.add(currentConditionAction);
		}
	}
}
