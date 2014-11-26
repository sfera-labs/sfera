package cc.sferalabs.sfera.script;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import cc.sferalabs.sfera.core.Task;
import cc.sferalabs.sfera.events.Event;

public class ActionTask extends Task {

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
			ScriptsEngine.LOG.info("action executed - file '" + rule.scriptFile + "' line " + rule.startLine);
		} catch (Throwable e) {
			int line = rule.startLine;
			if (e instanceof ScriptException) {
				if (((ScriptException) e).getLineNumber() >= 0) {
					line += ((ScriptException) e).getLineNumber() - 1;
				}
			}
			ScriptsEngine.LOG.error("Error executing action - file '" + rule.scriptFile + "' line " + line + ": " + e.getLocalizedMessage());
		}
	}
}
