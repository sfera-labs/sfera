package com.homesystemsconsulting.script;

import java.nio.file.Path;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import com.homesystemsconsulting.core.TasksManager;
import com.homesystemsconsulting.events.Event;
import com.homesystemsconsulting.script.parser.SferaScriptGrammarParser.TriggerContext;

public class Rule {
	
	final TriggerCondition condition;
	final CompiledScript action;
	final Path scriptFile;
	final int startLine;
	
	/**
	 * 
	 * @param condition
	 * @param action
	 * @param scriptFile
	 * @param engine
	 * @throws ScriptException
	 */
	public Rule(TriggerContext condition, String action, Path scriptFile, Compilable engine) throws ScriptException {
		this.condition = new TriggerCondition(condition);
		this.action = engine.compile(action);
		this.scriptFile = scriptFile;
		this.startLine = condition.getStart().getLine();
	}

	/**
	 * 
	 * @param event
	 */
	public void execute(Event event) {
		TasksManager.DEFAULT.execute(new ActionTask(event, this));
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public boolean eval(Event event) {
		try {
			return condition.eval(event);
		} catch (Exception e) {
			SferaScriptEngine.LOG.error("Error evaluating trigger condition - file " + scriptFile + ": " + e.getLocalizedMessage());
			return false;
		}
	}
}
