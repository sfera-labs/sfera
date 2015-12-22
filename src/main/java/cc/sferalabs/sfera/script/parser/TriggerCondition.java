package cc.sferalabs.sfera.script.parser;

import java.util.List;

import cc.sferalabs.sfera.events.Bus;
import cc.sferalabs.sfera.events.Event;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.AndExpressionContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.AtomExpressionContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.BooleanComparisonContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.EventContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.NotExpressionContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.NumberComparisonContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.OrExpressionContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.StableEventContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.StringComparisonContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.TerminalNodeContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.TransientEventContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.TriggerContext;
import cc.sferalabs.sfera.script.parser.antlr.SferaScriptGrammarParser.UnknownComparisonContext;

/**
 * Class representing a script trigger condition
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
class TriggerCondition {

	private final TriggerContext condition;

	/**
	 * Construct a TriggerCondition.
	 * 
	 * @param condition
	 *            the trigger condition context
	 */
	TriggerCondition(TriggerContext condition) {
		this.condition = condition;
	}

	/**
	 * Evaluates this condition using the specified event as trigger.
	 * 
	 * @param event
	 *            the trigger event
	 * @return the result of the condition evaluation
	 * @throws Exception
	 *             if an error occurs
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
		String eventId = event.getId();
		String condition = ctx.getText();
		if (eventId.startsWith(condition)) {
			if (eventId.length() == condition.length()) { // equal
				return true;
			}

			if (eventId.charAt(condition.length()) == '.'
					|| eventId.charAt(condition.length()) == '(') {
				/*
				 * meaning: eventId.startsWith(condition + ".") ||
				 * eventId.startsWith(condition + "(")
				 */
				return true;
			}
		}

		return false;
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
	 * @return
	 */
	private Object getEventValue(TerminalNodeContext ctx) {
		Event event = Bus.getEvent(ctx.getText());
		if (event == null) {
			return null;
		}

		return event.getScriptConditionValue();
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	private boolean eval(StringComparisonContext ctx) throws Exception {
		Object value = getEventValue(ctx.terminalNode());

		if (value == null) {
			return false;
		}

		if (!(value instanceof String)) {
			int line = ctx.getStart().getLine();
			throw new Exception("line " + line + ": Type error: " + ctx.terminalNode().getText()
					+ " not a String");
		}

		String literal = ctx.StringLiteral().getText();
		literal = literal.substring(1, literal.length() - 1);

		String stringValue = (String) value;

		if (ctx.ET() != null) {
			return stringValue.equals(literal);
		} else if (ctx.NE() != null) {
			return !stringValue.equals(literal);
		} else if (ctx.GT() != null) {
			return stringValue.compareTo(literal) > 0;
		} else if (ctx.LT() != null) {
			return stringValue.compareTo(literal) < 0;
		} else if (ctx.GE() != null) {
			return stringValue.compareTo(literal) >= 0;
		} else { // LE
			return stringValue.compareTo(literal) <= 0;
		}
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	private boolean eval(NumberComparisonContext ctx) throws Exception {
		Object value = getEventValue(ctx.terminalNode());

		if (value == null) {
			return false;
		}

		if (!(value instanceof Number)) {
			int line = ctx.getStart().getLine();
			throw new Exception("line " + line + ": Type error: " + ctx.terminalNode().getText()
					+ " not a number");
		}

		double literal = Double.parseDouble(ctx.NumberLiteral().getText());
		double doubleValue = ((Number) value).doubleValue();

		if (ctx.ET() != null) {
			return doubleValue == literal;
		} else if (ctx.NE() != null) {
			return doubleValue != literal;
		} else if (ctx.GT() != null) {
			return doubleValue > literal;
		} else if (ctx.LT() != null) {
			return doubleValue < literal;
		} else if (ctx.GE() != null) {
			return doubleValue >= literal;
		} else { // LE
			return doubleValue <= literal;
		}
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	private boolean eval(BooleanComparisonContext ctx) throws Exception {
		Object value = getEventValue(ctx.terminalNode());

		if (value == null) {
			return false;
		}

		if (!(value instanceof Boolean)) {
			int line = ctx.getStart().getLine();
			throw new Exception("line " + line + ": Type error: " + ctx.terminalNode().getText()
					+ " not a boolean");
		}

		boolean literal = Boolean.parseBoolean(ctx.BooleanLiteral().getText());
		boolean booleanValue = (boolean) value;

		if (ctx.ET() != null) {
			return booleanValue == literal;
		} else { // NE
			return booleanValue != literal;
		}
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private boolean eval(UnknownComparisonContext ctx) {
		Object value = getEventValue(ctx.terminalNode());

		if (ctx.ET() != null) {
			return value == null;
		} else { // NE
			return value != null;
		}
	}

}
