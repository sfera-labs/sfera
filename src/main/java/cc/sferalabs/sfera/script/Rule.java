package cc.sferalabs.sfera.script;

import java.nio.file.Path;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.TasksManager;
import cc.sferalabs.sfera.events.BaseEvent;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.TriggerContext;

public class Rule {

	private static final Logger logger = LogManager.getLogger();

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
	public Rule(TriggerContext condition, String action, Path scriptFile,
			Compilable engine, Bindings localScope) throws ScriptException {
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
	public void execute(BaseEvent event) {
		TasksManager.DEFAULT.execute(new ActionTask(event, this, localScope));
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public boolean eval(BaseEvent event) {
		try {
			return condition.eval(event);
		} catch (Exception e) {
			logger.error("Error evaluating trigger condition. File: " + scriptFile, e);
			return false;
		}
	}
}
