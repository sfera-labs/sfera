package cc.sferalabs.sfera.script;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.Task;
import cc.sferalabs.sfera.events.Event;

public class ActionTask extends Task {

	private static final Logger logger = LogManager.getLogger();

	private final Event triggerEvent;
	private final Rule rule;
	private final Bindings localScope;

	/**
	 * 
	 * @param triggerEvent
	 * @param rule
	 * @param localScope
	 */
	public ActionTask(Event triggerEvent, Rule rule, Bindings localScope) {
		super("script:" + rule.scriptFile + ":" + rule.startLine);
		this.triggerEvent = triggerEvent;
		this.rule = rule;
		this.localScope = localScope;
	}

	@Override
	public void execute() {
		try {
			ScriptEngine engine = rule.action.getEngine();
			Bindings b = engine.createBindings();
			// add global (directory) scope
			b.putAll(engine.getBindings(ScriptContext.ENGINE_SCOPE));
			// add local (file) scope
			b.putAll(localScope);
			// add "_e" variable
			b.put("_e", triggerEvent);
			rule.action.eval(b);
			logger.info("Action executed - file '{}' line {}", rule.scriptFile,
					rule.startLine);
		} catch (Throwable e) {
			int line = rule.startLine;
			if (e instanceof ScriptException) {
				if (((ScriptException) e).getLineNumber() >= 0) {
					line += ((ScriptException) e).getLineNumber() - 1;
				}
			}
			logger.error("Error executing action - file '{}' line {}: {}",
					rule.scriptFile, line, e.getLocalizedMessage());
			logger.catching(e);
		}
	}
}
