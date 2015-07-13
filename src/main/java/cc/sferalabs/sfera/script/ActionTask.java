package cc.sferalabs.sfera.script;

import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.events.Event;

public class ActionTask extends Task {

	private static final Logger logger = LogManager.getLogger();

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
			Bindings b = new SimpleBindings();
			b.putAll(rule.globalScope.getBindings());
			b.putAll(rule.localScope.getBindings());
			b.put("_e", triggerEvent);
			rule.action.eval(b);
			Bindings nashornGlobal = (Bindings) b.get("nashorn.global");
			if (nashornGlobal != null) {
				for (Entry<String, Object> binding : nashornGlobal.entrySet()) {
					String name = binding.getKey();
					if (rule.localScope.getBindings().containsKey(name)) {
						rule.localScope.put(name, binding.getValue());
					}
					if (rule.globalScope.getBindings().containsKey(name)) {
						rule.globalScope.put(name, binding.getValue());
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
