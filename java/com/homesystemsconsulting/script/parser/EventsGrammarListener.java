// Generated from EventsGrammar.g4 by ANTLR 4.3
package com.homesystemsconsulting.script.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link EventsGrammarParser}.
 */
public interface EventsGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#trigger}.
	 * @param ctx the parse tree
	 */
	void enterTrigger(@NotNull EventsGrammarParser.TriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#trigger}.
	 * @param ctx the parse tree
	 */
	void exitTrigger(@NotNull EventsGrammarParser.TriggerContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#stableEvent}.
	 * @param ctx the parse tree
	 */
	void enterStableEvent(@NotNull EventsGrammarParser.StableEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#stableEvent}.
	 * @param ctx the parse tree
	 */
	void exitStableEvent(@NotNull EventsGrammarParser.StableEventContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#booleanComparison}.
	 * @param ctx the parse tree
	 */
	void enterBooleanComparison(@NotNull EventsGrammarParser.BooleanComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#booleanComparison}.
	 * @param ctx the parse tree
	 */
	void exitBooleanComparison(@NotNull EventsGrammarParser.BooleanComparisonContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#notExpression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(@NotNull EventsGrammarParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#notExpression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(@NotNull EventsGrammarParser.NotExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(@NotNull EventsGrammarParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(@NotNull EventsGrammarParser.OrExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#ruleLine}.
	 * @param ctx the parse tree
	 */
	void enterRuleLine(@NotNull EventsGrammarParser.RuleLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#ruleLine}.
	 * @param ctx the parse tree
	 */
	void exitRuleLine(@NotNull EventsGrammarParser.RuleLineContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#rules}.
	 * @param ctx the parse tree
	 */
	void enterRules(@NotNull EventsGrammarParser.RulesContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#rules}.
	 * @param ctx the parse tree
	 */
	void exitRules(@NotNull EventsGrammarParser.RulesContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#transientEvent}.
	 * @param ctx the parse tree
	 */
	void enterTransientEvent(@NotNull EventsGrammarParser.TransientEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#transientEvent}.
	 * @param ctx the parse tree
	 */
	void exitTransientEvent(@NotNull EventsGrammarParser.TransientEventContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(@NotNull EventsGrammarParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(@NotNull EventsGrammarParser.AndExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#numberComparison}.
	 * @param ctx the parse tree
	 */
	void enterNumberComparison(@NotNull EventsGrammarParser.NumberComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#numberComparison}.
	 * @param ctx the parse tree
	 */
	void exitNumberComparison(@NotNull EventsGrammarParser.NumberComparisonContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#atomExpression}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpression(@NotNull EventsGrammarParser.AtomExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#atomExpression}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpression(@NotNull EventsGrammarParser.AtomExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(@NotNull EventsGrammarParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(@NotNull EventsGrammarParser.EventContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(@NotNull EventsGrammarParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(@NotNull EventsGrammarParser.ActionContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#unknownComparison}.
	 * @param ctx the parse tree
	 */
	void enterUnknownComparison(@NotNull EventsGrammarParser.UnknownComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#unknownComparison}.
	 * @param ctx the parse tree
	 */
	void exitUnknownComparison(@NotNull EventsGrammarParser.UnknownComparisonContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(@NotNull EventsGrammarParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(@NotNull EventsGrammarParser.ParseContext ctx);

	/**
	 * Enter a parse tree produced by {@link EventsGrammarParser#stringComparison}.
	 * @param ctx the parse tree
	 */
	void enterStringComparison(@NotNull EventsGrammarParser.StringComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link EventsGrammarParser#stringComparison}.
	 * @param ctx the parse tree
	 */
	void exitStringComparison(@NotNull EventsGrammarParser.StringComparisonContext ctx);
}