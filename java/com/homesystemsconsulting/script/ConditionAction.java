package com.homesystemsconsulting.script;

import java.util.List;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.core.TasksManager;
import com.homesystemsconsulting.events.Event;
import com.homesystemsconsulting.events.EventsMonitor;
import com.homesystemsconsulting.script.parser.EventsGrammarParser;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.AndExpressionContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.AtomExpressionContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.BooleanComparisonContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.EventContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.NotExpressionContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.NumberComparisonContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.OrExpressionContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.StableEventContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.StringComparisonContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.TransientEventContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.UnknownComparisonContext;
import com.homesystemsconsulting.util.logging.SystemLogger;

public class ConditionAction extends Task {

	private final EventsGrammarParser.TriggerContext condition;
	private final String action;
	private final CompiledScript script;
	private final String appName;
	private final String scriptFile;
	
	/**
	 * 
	 * @param condition
	 * @param action
	 * @param appName
	 * @param scriptFile
	 * @param engine
	 * @throws ScriptException
	 */
	public ConditionAction(EventsGrammarParser.TriggerContext condition, String action, String appName, String scriptFile, Compilable engine) throws ScriptException {
		super("app." + appName + ".script:" + scriptFile + ":" + condition.getStart().getLine());
		this.condition = condition;
		this.action = action;
		this.script = engine.compile(action);
		this.appName = appName;
		this.scriptFile = scriptFile;
	}

	/**
	 * 
	 * @return
	 */
	public EventsGrammarParser.TriggerContext getCondition() {
		return condition;
	}

	@Override
	public void execute() {
		try {
			script.eval();
		} catch (ScriptException e) {
			int line = condition.getStart().getLine();
			if (e.getLineNumber() >= 0) {
				line += e.getLineNumber() - 1;
			}
			SystemLogger.getLogger("app." + appName).error("Error executing action - file '" + scriptFile + "' line " + line + ": " + e.getLocalizedMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAction() {
		return action;
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public boolean checkCondition(Event event) {
		return eval(condition.orExpression(), event);
	}

	/**
	 * 
	 * @param ctx
	 * @param event
	 * @return
	 */
	private boolean eval(OrExpressionContext ctx, Event event) {
		List<AndExpressionContext> ands = ctx.andExpression();
		
		boolean res = eval(ands.get(0), event);
		for (int i = 1; i < ands.size(); i++) {
			res = res || eval(ands.get(i), event);
		}
		
		return res;
	}

	/**
	 * 
	 * @param ctx
	 * @param event
	 * @return
	 */
	private boolean eval(AndExpressionContext ctx, Event event) {
		List<NotExpressionContext> nots = ctx.notExpression();
		
		boolean res = eval(nots.get(0), event);
		for (int i = 1; i < nots.size(); i++) {
			res = res && eval(nots.get(i), event);
		}
		
		return res;
	}

	/**
	 * 
	 * @param ctx
	 * @param event
	 * @return
	 */
	private boolean eval(NotExpressionContext ctx, Event event) {
		if (ctx.NOT() != null) {
			return !eval(ctx.atomExpression(), event);
		} else {
			return eval(ctx.atomExpression(), event);
		}
	}

	/**
	 * 
	 * @param ctx
	 * @param event
	 * @return
	 */
	private boolean eval(AtomExpressionContext ctx, Event event) {
		if (ctx.event() != null) {
			return eval(ctx.event(), event);
		} else {
			return eval(ctx.orExpression(), event);
		}
	}

	/**
	 * 
	 * @param ctx
	 * @param event
	 * @return
	 */
	private boolean eval(EventContext ctx, Event event) {
		if (ctx.stableEvent() != null) {
			return eval(ctx.stableEvent());
		} else {
			return eval(ctx.transientEvent(), event);
		}
	}

	/**
	 * 
	 * @param ctx
	 * @param event
	 * @return
	 */
	private boolean eval(TransientEventContext ctx, Event event) {
		return event.getId().equals(ctx.getText()) || event.getId().startsWith(ctx.getText() + ".");
	}
	
	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private boolean eval(StableEventContext ctx) {
		if (ctx.stringComparison() != null) {
			return eval(ctx.stringComparison());
		} else if (ctx.numberComparison() != null) {
			return eval(ctx.numberComparison());
		} else if (ctx.booleanComparison() != null) {
			return eval(ctx.booleanComparison());
		} else { // 'unknown' comparison
			return eval(ctx.unknownComparison());
		}
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private boolean eval(StringComparisonContext ctx) {
		Object value = EventsMonitor.getEventValue(ctx.FinalNodeId().getText());
		
		if (value == null) {
			return false;
		}
		
		if (!(value instanceof String)) {
			int line = ctx.getStart().getLine();
			SystemLogger.getLogger("app." + appName).error("Error executing action - file '" + scriptFile + "' line " + line + ": Type error: " + ctx.FinalNodeId().getText() + " not a string");
			return false;
		}
		
		String literal = ctx.StringLiteral().getText();
		literal = literal.substring(1, literal.length() - 1);
		
		if (ctx.ET() != null) {
			return literal.equals(value);
		} else if (ctx.NE() != null) {
			return !literal.equals(value);
		} else if (ctx.GT() != null) {
			return literal.compareTo((String) value) < 0;
		} else if (ctx.LT() != null) {
			return literal.compareTo((String) value) > 0;
		} else if (ctx.GE() != null) {
			return literal.compareTo((String) value) <= 0;
		} else { // LE
			return literal.compareTo((String) value) >= 0;
		}
	}
	
	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private boolean eval(NumberComparisonContext ctx) {
		Object value = EventsMonitor.getEventValue(ctx.FinalNodeId().getText());
		
		if (value == null) {
			return false;
		}
		
		if (!(value instanceof Double)) {
			int line = ctx.getStart().getLine();
			SystemLogger.getLogger("app." + appName).error("Error executing action - file '" + scriptFile + "' line " + line + ": Type error: " + ctx.FinalNodeId().getText() + " not a number");
			return false;
		}
		
		if (ctx.ET() != null) {
			return Double.parseDouble(ctx.NumberLiteral().getText()) == (Double) value;
		} else if (ctx.NE() != null) {
			return Double.parseDouble(ctx.NumberLiteral().getText()) != (Double) value;
		} else if (ctx.GT() != null) {
			return Double.parseDouble(ctx.NumberLiteral().getText()) > (Double) value;
		} else if (ctx.LT() != null) {
			return Double.parseDouble(ctx.NumberLiteral().getText()) < (Double) value;
		} else if (ctx.GE() != null) {
			return Double.parseDouble(ctx.NumberLiteral().getText()) >= (Double) value;
		} else { // LE
			return Double.parseDouble(ctx.NumberLiteral().getText()) <= (Double) value;
		}
	}
	
	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private boolean eval(BooleanComparisonContext ctx) {
		Object value = EventsMonitor.getEventValue(ctx.FinalNodeId().getText());
		
		if (value == null) {
			return false;
		}
		
		if (!(value instanceof Boolean)) {
			int line = ctx.getStart().getLine();
			SystemLogger.getLogger("app." + appName).error("Error executing action - file '" + scriptFile + "' line " + line + ": Type error: " + ctx.FinalNodeId().getText() + " not a boolean");
			return false;
		}
		
		if (ctx.ET() != null) {
			return Boolean.parseBoolean(ctx.BooleanLiteral().getText()) == (Boolean) value;
		} else { // NE
			return Boolean.parseBoolean(ctx.BooleanLiteral().getText()) != (Boolean) value;
		}
	}
	
	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private boolean eval(UnknownComparisonContext ctx) {
		Object value = EventsMonitor.getEventValue(ctx.FinalNodeId().getText());
		
		if (ctx.ET() != null) {
			return value == null;
		} else { // NE
			return value != null;
		}
	}

	/**
	 * 
	 * @param triggerEvent
	 */
	public void execute(Event triggerEvent) {
		script.getEngine().put("ev", triggerEvent);
		TasksManager.DEFAULT.execute(this);
	}
}
