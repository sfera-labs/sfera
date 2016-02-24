// Generated from SferaScriptGrammar.g4 by ANTLR 4.5.1
package cc.sferalabs.sfera.scripts.parser.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SferaScriptGrammarParser}.
 */
public interface SferaScriptGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#parse}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterParse(SferaScriptGrammarParser.ParseContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#parse}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitParse(SferaScriptGrammarParser.ParseContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#imports}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterImports(SferaScriptGrammarParser.ImportsContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#imports}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitImports(SferaScriptGrammarParser.ImportsContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#importLine}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterImportLine(SferaScriptGrammarParser.ImportLineContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#importLine}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitImportLine(SferaScriptGrammarParser.ImportLineContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#init}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterInit(SferaScriptGrammarParser.InitContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#init}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitInit(SferaScriptGrammarParser.InitContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#rules}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterRules(SferaScriptGrammarParser.RulesContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#rules}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitRules(SferaScriptGrammarParser.RulesContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#ruleLine}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterRuleLine(SferaScriptGrammarParser.RuleLineContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#ruleLine}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitRuleLine(SferaScriptGrammarParser.RuleLineContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#action}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterAction(SferaScriptGrammarParser.ActionContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#action}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitAction(SferaScriptGrammarParser.ActionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#trigger}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterTrigger(SferaScriptGrammarParser.TriggerContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#trigger}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitTrigger(SferaScriptGrammarParser.TriggerContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#orExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterOrExpression(SferaScriptGrammarParser.OrExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#orExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitOrExpression(SferaScriptGrammarParser.OrExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#andExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterAndExpression(SferaScriptGrammarParser.AndExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#andExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitAndExpression(SferaScriptGrammarParser.AndExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#notExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterNotExpression(SferaScriptGrammarParser.NotExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#notExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitNotExpression(SferaScriptGrammarParser.NotExpressionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#atomExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterAtomExpression(SferaScriptGrammarParser.AtomExpressionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#atomExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitAtomExpression(SferaScriptGrammarParser.AtomExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#event}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterEvent(SferaScriptGrammarParser.EventContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#event}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitEvent(SferaScriptGrammarParser.EventContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#stableEvent}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterStableEvent(SferaScriptGrammarParser.StableEventContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#stableEvent}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitStableEvent(SferaScriptGrammarParser.StableEventContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#stringComparison}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterStringComparison(SferaScriptGrammarParser.StringComparisonContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#stringComparison}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitStringComparison(SferaScriptGrammarParser.StringComparisonContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#numberComparison}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterNumberComparison(SferaScriptGrammarParser.NumberComparisonContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#numberComparison}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitNumberComparison(SferaScriptGrammarParser.NumberComparisonContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#booleanComparison}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterBooleanComparison(SferaScriptGrammarParser.BooleanComparisonContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#booleanComparison}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitBooleanComparison(SferaScriptGrammarParser.BooleanComparisonContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#unknownComparison}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterUnknownComparison(SferaScriptGrammarParser.UnknownComparisonContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#unknownComparison}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitUnknownComparison(SferaScriptGrammarParser.UnknownComparisonContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#transientEvent}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterTransientEvent(SferaScriptGrammarParser.TransientEventContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#transientEvent}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitTransientEvent(SferaScriptGrammarParser.TransientEventContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#terminalNode}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterTerminalNode(SferaScriptGrammarParser.TerminalNodeContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link SferaScriptGrammarParser#terminalNode}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitTerminalNode(SferaScriptGrammarParser.TerminalNodeContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#subNode}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterSubNode(SferaScriptGrammarParser.SubNodeContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#subNode}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitSubNode(SferaScriptGrammarParser.SubNodeContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#parameters}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterParameters(SferaScriptGrammarParser.ParametersContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#parameters}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitParameters(SferaScriptGrammarParser.ParametersContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link SferaScriptGrammarParser#paramsList}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterParamsList(SferaScriptGrammarParser.ParamsListContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#paramsList}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitParamsList(SferaScriptGrammarParser.ParamsListContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#parameter}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterParameter(SferaScriptGrammarParser.ParameterContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#parameter}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitParameter(SferaScriptGrammarParser.ParameterContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#array}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterArray(SferaScriptGrammarParser.ArrayContext ctx);

	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#array}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitArray(SferaScriptGrammarParser.ArrayContext ctx);
}