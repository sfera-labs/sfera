package com.homesystemsconsulting.script;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.script.Compilable;
import javax.script.ScriptException;

import org.antlr.v4.runtime.misc.NotNull;

import com.homesystemsconsulting.script.parser.EventsGrammarBaseListener;
import com.homesystemsconsulting.script.parser.EventsGrammarParser;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.TriggerContext;

public class TriggerActionMapListener extends EventsGrammarBaseListener {
	
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
	public void enterRuleLine(@NotNull EventsGrammarParser.RuleLineContext ctx) {
		currentConditionAction = null;
		TriggerContext condition = ctx.trigger();
		String action = ctx.action().Script().getText();
		action = action.substring(1, action.length() - 1).trim();
		try {
			currentConditionAction = new Rule(condition, action, scriptFile, engine);
		} catch (ScriptException e) {
			int line = ctx.getStart().getLine();
			if (e.getLineNumber() >= 0) {
				line += e.getLineNumber() - 1;
			}
			errors.add("line " + line + " - script syntax error");
		}
	}
	
	@Override
	public void enterStableEvent(@NotNull EventsGrammarParser.StableEventContext ctx) {
		addTrigger(ctx.getChild(0).getChild(0).getText());
	}

	@Override
	public void enterTransientEvent(@NotNull EventsGrammarParser.TransientEventContext ctx) {
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
