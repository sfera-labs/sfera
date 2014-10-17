package com.homesystemsconsulting.script;

import java.util.List;

import com.homesystemsconsulting.events.Event;
import com.homesystemsconsulting.events.EventsMonitor;
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
import com.homesystemsconsulting.script.parser.EventsGrammarParser.TriggerContext;
import com.homesystemsconsulting.script.parser.EventsGrammarParser.UnknownComparisonContext;

public class TriggerCondition {

	private final TriggerContext condition;
	
	/**
	 * 
	 * @param condition
	 */
	public TriggerCondition(TriggerContext condition) {
		this.condition = condition;
	}

	/**
	 * 
	 * @param event
	 * @return
	 * @throws Exception 
	 */
	public boolean eval(Event event) throws Exception {
		return eval(condition.orExpression(), event);
	}

	/**
	 * 
	 * @param ctx
	 * @param event
	 * @return
	 * @throws Exception 
	 */
	private boolean eval(OrExpressionContext ctx, Event event) throws Exception {
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
	 * @throws Exception 
	 */
	private boolean eval(AndExpressionContext ctx, Event event) throws Exception {
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
	 * @throws Exception 
	 */
	private boolean eval(NotExpressionContext ctx, Event event) throws Exception {
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
	 * @throws Exception 
	 */
	private boolean eval(AtomExpressionContext ctx, Event event) throws Exception {
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
	 * @throws Exception 
	 */
	private boolean eval(EventContext ctx, Event event) throws Exception {
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
	 * @throws Exception 
	 */
	private boolean eval(StableEventContext ctx) throws Exception {
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
	 * @throws Exception 
	 */
	private boolean eval(StringComparisonContext ctx) throws Exception {
		Object value = EventsMonitor.getEventValue(ctx.FinalNodeId().getText());
		
		if (value == null) {
			return false;
		}
		
		if (!(value instanceof String)) {
			int line = ctx.getStart().getLine();
			throw new Exception("line " + line + ": Type error: " + ctx.FinalNodeId().getText() + " not a string");
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
	 * @throws Exception 
	 */
	private boolean eval(NumberComparisonContext ctx) throws Exception {
		Object value = EventsMonitor.getEventValue(ctx.FinalNodeId().getText());
		
		if (value == null) {
			return false;
		}
		
		if (!(value instanceof Double)) {
			int line = ctx.getStart().getLine();
			throw new Exception("line " + line + ": Type error: " + ctx.FinalNodeId().getText() + " not a number");
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
	 * @throws Exception 
	 */
	private boolean eval(BooleanComparisonContext ctx) throws Exception {
		Object value = EventsMonitor.getEventValue(ctx.FinalNodeId().getText());
		
		if (value == null) {
			return false;
		}
		
		if (!(value instanceof Boolean)) {
			int line = ctx.getStart().getLine();
			throw new Exception("line " + line + ": Type error: " + ctx.FinalNodeId().getText() + " not a boolean");
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

}
