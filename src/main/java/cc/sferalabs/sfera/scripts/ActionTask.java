/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.scripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.sferalabs.sfera.core.services.Task;
import cc.sferalabs.sfera.events.Event;

/**
 * Class representing a script action
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class ActionTask extends Task {

	private static final Logger logger = LoggerFactory.getLogger(ActionTask.class);

	private final Event triggerEvent;
	private final Rule rule;

	/**
	 * 
	 * @param triggerEvent
	 *            the trigger event
	 * @param rule
	 *            the rule
	 */
	ActionTask(Event triggerEvent, Rule rule) {
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
			Map<String, Object> preBindings = new HashMap<>(b);
			b.put("_e", triggerEvent);

			rule.action.eval(b);

			for (Entry<String, Object> e : b.entrySet()) {
				String key = e.getKey();
				if (preBindings.containsKey(key)) {
					Object val = e.getValue();
					Object preVal = preBindings.get(key);
					if (preVal != val) {
						if (rule.fileScope.replace(key, val) == null) {
							for (Bindings imp : rule.imports) {
								if (imp.replace(key, val) != null) {
									break;
								}
							}
						}
					}
				}
			}
			logger.debug("Action executed. File '{}' line {}", rule.scriptFile, rule.startLine);
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
