package cc.sferalabs.sfera.scripts;

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
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser.TriggerContext;

/**
 * Class representing a script rule.
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Rule {

	private static final Logger logger = LoggerFactory.getLogger(Rule.class);

	final TriggerCondition condition;
	final CompiledScript action;
	final Path scriptFile;
	final int startLine;
	final Bindings fileScope;
	final List<Bindings> imports;

	/**
	 * Constructs a Rule.
	 * 
	 * @param condition
	 *            the trigger condition context
	 * @param action
	 *            the action
	 * @param scriptFile
	 *            path of the script file
	 * @param engine
	 *            the script engine
	 * @param fileScope
	 *            the file-scope bindings
	 * @param imports
	 *            the imported bindings
	 * @throws ScriptException
	 *             if compilation of the action fails
	 */
	public Rule(TriggerContext condition, String action, Path scriptFile, ScriptEngine engine,
			Bindings fileScope, List<Bindings> imports) throws ScriptException {
		this.condition = new TriggerCondition(condition);
		this.action = ((Compilable) engine).compile(action);
		this.scriptFile = scriptFile;
		this.startLine = condition.getStart().getLine();
		this.fileScope = fileScope;
		this.imports = imports;
	}

	/**
	 * @return the scriptFile
	 */
	public Path getScriptFile() {
		return scriptFile;
	}

	/**
	 * @return the startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Executes this rule's action using the specified event as trigger.
	 * 
	 * @param event
	 *            the trigger event
	 */
	public void executeAction(Event event) {
		TasksManager.execute(new ActionTask(event, this));
	}

	/**
	 * Evaluates this rule's condition using the specified event as trigger.
	 * 
	 * @param event
	 *            the trigger event
	 * @return the result of the evaluation
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
