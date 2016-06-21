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

package cc.sferalabs.sfera.scripts.parser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.antlr.v4.runtime.tree.TerminalNode;

import cc.sferalabs.sfera.scripts.Rule;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarBaseListener;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser.ImportLineContext;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser.InitContext;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser.RuleLineContext;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser.StableEventContext;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser.TransientEventContext;
import cc.sferalabs.sfera.scripts.parser.antlr.SferaScriptGrammarParser.TriggerContext;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class ScriptGrammarListener extends SferaScriptGrammarBaseListener {

	private final Path scriptFile;
	private final ScriptEngine engine;
	private final Bindings fileScope;
	private final Map<String, Bindings> libraries;

	private final List<Bindings> imports = new ArrayList<>();
	private final HashMap<String, Set<Rule>> triggerRulesMap = new HashMap<String, Set<Rule>>();
	private final List<Object> errors = new ArrayList<>();

	private Rule currentRule;

	/**
	 * 
	 * @param scriptFile
	 *            the path of the script file
	 * @param libraries
	 *            the map of library-files and respective bindings
	 * @param engine
	 *            the script engine
	 */
	public ScriptGrammarListener(Path scriptFile, Map<String, Bindings> libraries,
			ScriptEngine engine) {
		this.scriptFile = scriptFile;
		this.engine = engine;
		this.fileScope = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		this.libraries = libraries;
	}

	/**
	 * @return the list of errors
	 */
	public List<Object> getErrors() {
		return errors;
	}

	/**
	 * @return the trigger-rules map
	 */
	public HashMap<String, Set<Rule>> getTriggerRulesMap() {
		return triggerRulesMap;
	}

	@Override
	public void enterImportLine(ImportLineContext ctx) {
		try {
			String text = ctx.getText();
			String importPath = text.substring(text.indexOf(' ') + 1, text.length() - 1).trim();

			Path libPath;
			try {
				if (importPath.startsWith("/")) {
					libPath = Paths.get(importPath.substring(1));
				} else {
					libPath = scriptFile.getParent().resolve(importPath);
					libPath = libPath.subpath(1, libPath.getNameCount());
				}
			} catch (Throwable e) {
				throw new Exception("Illegal import path: " + importPath, e);
			}

			Bindings lib = libraries.get(libPath.normalize().toString());
			if (lib != null) {
				imports.add(lib);
			} else {
				int line = ctx.getStart().getLine();
				errors.add("line " + line + " - import error: file '" + libPath + "' not found");
			}

		} catch (Throwable e) {
			int line = ctx.getStart().getLine();
			errors.add(new Exception("line " + line + ": " + e.getMessage(), e));
		}
	}

	@Override
	public void enterInit(InitContext ctx) {
		String action = getAction(ctx.Script());
		try {
			Bindings b = engine.createBindings();
			for (Bindings imp : imports) {
				b.putAll(imp);
			}
			b.putAll(fileScope);

			engine.eval(action, b);

			for (Entry<String, Object> e : b.entrySet()) {
				String key = e.getKey();
				Object val = e.getValue();
				boolean replaced = false;
				for (Bindings imp : imports) {
					if (imp.replace(key, val) != null) {
						replaced = true;
						break;
					}
				}
				if (!replaced) {
					fileScope.put(key, val);
				}
			}
		} catch (Throwable e) {
			int line = ctx.getStart().getLine();
			if (e instanceof ScriptException) {
				if (((ScriptException) e).getLineNumber() >= 0) {
					line += ((ScriptException) e).getLineNumber() - 1;
				}
			}
			errors.add(new Exception("line " + line + ": " + e.getMessage(), e));
		}
	}

	@Override
	public void enterRuleLine(RuleLineContext ctx) {
		currentRule = null;
		TriggerContext condition = ctx.trigger();
		String action = getAction(ctx.action().Script());
		try {
			currentRule = new Rule(condition, action, scriptFile, engine, fileScope, imports);
		} catch (ScriptException e) {
			int line = ctx.getStart().getLine();
			if (e.getLineNumber() >= 0) {
				line += e.getLineNumber() - 1;
			}
			errors.add(new Exception("line " + line + ": " + e.getMessage(), e));
		}
	}

	/**
	 * 
	 * @param script
	 * @return
	 */
	private String getAction(TerminalNode script) {
		String action = script.getText();
		return action.substring(1, action.length() - 1);
	}

	@Override
	public void enterStableEvent(StableEventContext ctx) {
		addTrigger(ctx.getChild(0).getChild(0).getText());
	}

	@Override
	public void enterTransientEvent(TransientEventContext ctx) {
		addTrigger(ctx.getChild(0).getText());
	}

	/**
	 * 
	 * @param id
	 */
	private void addTrigger(String id) {
		if (currentRule != null) {
			Set<Rule> rules = triggerRulesMap.get(id);
			if (rules == null) {
				rules = new HashSet<Rule>();
				triggerRulesMap.put(id, rules);
			}
			rules.add(currentRule);
		}
	}
}
