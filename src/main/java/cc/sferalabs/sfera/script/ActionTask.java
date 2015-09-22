package cc.sferalabs.sfera.script;

import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.events.Event;

public class ActionTask extends Task {

	private static final Logger logger = LoggerFactory.getLogger(ActionTask.class);

	private final Event triggerEvent;
	private final Rule rule;

	/**
	 * 
	 * @param triggerEvent
	 * @param rule
	 * @param localScope
	 */
	public ActionTask(Event triggerEvent, Rule rule) {
		super("script:" + rule.scriptFile + ":" + rule.startLine);
		this.triggerEvent = triggerEvent;
		this.rule = rule;
	}

	@Override
	protected void execute() {
		try {
			ScriptEngine engine = rule.action.getEngine();
			Bindings b = engine.createBindings();
			for (Bindings imp : rule.imports) {
				b.putAll(imp);
			}
			b.putAll(rule.fileScope);
			b.put("_e", triggerEvent);
			rule.action.eval(b);
			for (Entry<String, Object> e : b.entrySet()) {
				String key = e.getKey();
				Object val = e.getValue();
				if (rule.fileScope.replace(key, val) == null) {
					for (Bindings imp : rule.imports) {
						if (imp.replace(key, val) != null) {
							break;
						}
					}
				}
			}
			logger.info("Action executed. File '{}' line {}", rule.scriptFile, rule.startLine);
		} catch (Throwable e) {
			int line = rule.startLine;
			if (e instanceof ScriptException) {
				if (((ScriptException) e).getLineNumber() >= 0) {
					line += ((ScriptException) e).getLineNumber() - 1;
				}
			}
			logger.error("Error executing action. File '" + rule.scriptFile + "' line " + line, e);
		}
	}

}
