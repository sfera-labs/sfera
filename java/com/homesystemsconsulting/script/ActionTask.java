package com.homesystemsconsulting.script;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.events.Event;

public class ActionTask extends Task {

	private final Event triggerEvent;
	private final Rule rule;

	/**
	 * 
	 * @param triggerEvent
	 * @param rule
	 */
	public ActionTask(Event triggerEvent, Rule rule) {
		super("script:" + rule.scriptFile + ":" + rule.startLine);
		this.triggerEvent = triggerEvent;
		this.rule = rule;
	}

	@Override
	public void execute() {
		try {
			Bindings b = rule.action.getEngine().createBindings();
			b.putAll(rule.action.getEngine().getBindings(ScriptContext.ENGINE_SCOPE));
			b.put("ev", triggerEvent); // add "ev" variable
			rule.action.eval(b);
			SferaScriptEngine.LOG.info("action executed - file '" + rule.scriptFile + "' line " + rule.startLine);
		} catch (Throwable e) {
			int line = rule.startLine;
			if (e instanceof ScriptException) {
				if (((ScriptException) e).getLineNumber() >= 0) {
					line += ((ScriptException) e).getLineNumber() - 1;
				}
			}
			SferaScriptEngine.LOG.error("Error executing action - file '" + rule.scriptFile + "' line " + line + ": " + e.getLocalizedMessage());
		}
	}
}
