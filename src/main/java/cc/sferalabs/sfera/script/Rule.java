package cc.sferalabs.sfera.script;

import java.nio.file.Path;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.TriggerContext;

public class Rule {

	private static final Logger logger = LogManager.getLogger();

	final TriggerCondition condition;
	final CompiledScript action;
	final Path scriptFile;
	final int startLine;
	final Scope globalScope;
	final Scope localScope;

	/**
	 * 
	 * @param condition
	 * @param action
	 * @param scriptFile
	 * @param engine
	 * @param globalScope
	 * @param localScope
	 * @throws ScriptException
	 */
	public Rule(TriggerContext condition, String action, Path scriptFile, Compilable engine,
			Scope globalScope, Scope localScope) throws ScriptException {
		this.condition = new TriggerCondition(condition);
		this.action = engine.compile(action);
		this.scriptFile = scriptFile;
		this.startLine = condition.getStart().getLine();
		this.globalScope = globalScope;
		this.localScope = localScope;
	}

	/**
	 * 
	 * @param event
	 */
	public void execute(Event event) {
		TasksManager.execute(new ActionTask(event, this));
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
			logger.error("Error evaluating trigger condition. File: " + scriptFile, e);
			return false;
		}
	}
}
