package cc.sferalabs.sfera.script;

import java.nio.file.Path;
import java.util.List;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.TasksManager;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.script.parser.SferaScriptGrammarParser.TriggerContext;

/**
 * Class representing a script rule.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class Rule {

	private static final Logger logger = LoggerFactory.getLogger(Rule.class);

	final TriggerCondition condition;
	final CompiledScript action;
	final Path scriptFile;
	final int startLine;
	final Bindings fileScope;
	final List<Bindings> imports;

	/**
	 * 
	 * @param condition
	 * @param action
	 * @param scriptFile
	 * @param engine
	 * @param fileScope
	 * @param imports
	 * @throws ScriptException
	 */
	Rule(TriggerContext condition, String action, Path scriptFile, ScriptEngine engine,
			Bindings fileScope, List<Bindings> imports) throws ScriptException {
		this.condition = new TriggerCondition(condition);
		this.action = ((Compilable) engine).compile(action);
		this.scriptFile = scriptFile;
		this.startLine = condition.getStart().getLine();
		this.fileScope = fileScope;
		this.imports = imports;
	}

	/**
	 * 
	 * @param event
	 */
	public void executeAction(Event event) {
		TasksManager.execute(new ActionTask(event, this));
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public boolean evalCondition(Event event) {
		try {
			return condition.eval(event);
		} catch (Exception e) {
			logger.error("Error evaluating trigger condition. File: " + scriptFile, e);
			return false;
		}
	}
}
