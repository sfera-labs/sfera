package cc.sferalabs.sfera.script;

import java.nio.file.Path;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import cc.sferalabs.sfera.core.TasksManager;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.TriggerContext;

public class Rule {
	
	final TriggerCondition condition;
	final CompiledScript action;
	final Path scriptFile;
	final int startLine;
	final Bindings localScope;
	
	/**
	 * 
	 * @param condition
	 * @param action
	 * @param scriptFile
	 * @param engine
	 * @param localScope 
	 * @throws ScriptException
	 */
	public Rule(TriggerContext condition, String action, Path scriptFile, Compilable engine, Bindings localScope) throws ScriptException {
		this.condition = new TriggerCondition(condition);
		this.action = engine.compile(action);
		this.scriptFile = scriptFile;
		this.startLine = condition.getStart().getLine();
		this.localScope = localScope;
	}

	/**
	 * 
	 * @param event
	 */
	public void execute(Event event) {
		TasksManager.DEFAULT.execute(new ActionTask(event, this, localScope));
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
			ScriptsEngine.LOG.error("Error evaluating trigger condition - file " + scriptFile + ": " + e.getLocalizedMessage());
			return false;
		}
	}
}
