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
