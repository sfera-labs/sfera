package com.homesystemsconsulting.script;

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
	
	public final HashMap<String, HashSet<ConditionAction>> triggerActionsMap = new HashMap<String, HashSet<ConditionAction>>();
	public final ArrayList<String> errors = new ArrayList<String>();
	public final String appName;
	public final String scriptFile;
	private final Compilable engine;
	
	private ConditionAction currentConditionAction;
	
	/**
	 * 
	 * @param appName
	 * @param scriptFile
	 * @param engine 
	 */
	public TriggerActionMapListener(String appName, String scriptFile, Compilable engine) {
		this.appName = appName;
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
			currentConditionAction = new ConditionAction(condition, action, appName, scriptFile, engine);
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
			HashSet<ConditionAction> actions = triggerActionsMap.get(id);
			if (actions == null) {
				actions = new HashSet<ConditionAction>();
				triggerActionsMap.put(id, actions);
			}
			actions.add(currentConditionAction);
		}
	}
}
