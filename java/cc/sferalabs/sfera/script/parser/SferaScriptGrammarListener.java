// Generated from SferaScriptGrammar.g4 by ANTLR 4.3
package cc.sferalabs.sfera.script.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SferaScriptGrammarParser}.
 */
public interface SferaScriptGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#init}.
	 * @param ctx the parse tree
	 */
	void enterInit(@NotNull SferaScriptGrammarParser.InitContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#init}.
	 * @param ctx the parse tree
	 */
	void exitInit(@NotNull SferaScriptGrammarParser.InitContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#numberComparison}.
	 * @param ctx the parse tree
	 */
	void enterNumberComparison(@NotNull SferaScriptGrammarParser.NumberComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#numberComparison}.
	 * @param ctx the parse tree
	 */
	void exitNumberComparison(@NotNull SferaScriptGrammarParser.NumberComparisonContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#unknownComparison}.
	 * @param ctx the parse tree
	 */
	void enterUnknownComparison(@NotNull SferaScriptGrammarParser.UnknownComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#unknownComparison}.
	 * @param ctx the parse tree
	 */
	void exitUnknownComparison(@NotNull SferaScriptGrammarParser.UnknownComparisonContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#transientEvent}.
	 * @param ctx the parse tree
	 */
	void enterTransientEvent(@NotNull SferaScriptGrammarParser.TransientEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#transientEvent}.
	 * @param ctx the parse tree
	 */
	void exitTransientEvent(@NotNull SferaScriptGrammarParser.TransientEventContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#rules}.
	 * @param ctx the parse tree
	 */
	void enterRules(@NotNull SferaScriptGrammarParser.RulesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#rules}.
	 * @param ctx the parse tree
	 */
	void exitRules(@NotNull SferaScriptGrammarParser.RulesContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#notExpression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(@NotNull SferaScriptGrammarParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#notExpression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(@NotNull SferaScriptGrammarParser.NotExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(@NotNull SferaScriptGrammarParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(@NotNull SferaScriptGrammarParser.ParseContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#trigger}.
	 * @param ctx the parse tree
	 */
	void enterTrigger(@NotNull SferaScriptGrammarParser.TriggerContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#trigger}.
	 * @param ctx the parse tree
	 */
	void exitTrigger(@NotNull SferaScriptGrammarParser.TriggerContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#booleanComparison}.
	 * @param ctx the parse tree
	 */
	void enterBooleanComparison(@NotNull SferaScriptGrammarParser.BooleanComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#booleanComparison}.
	 * @param ctx the parse tree
	 */
	void exitBooleanComparison(@NotNull SferaScriptGrammarParser.BooleanComparisonContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#ruleLine}.
	 * @param ctx the parse tree
	 */
	void enterRuleLine(@NotNull SferaScriptGrammarParser.RuleLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#ruleLine}.
	 * @param ctx the parse tree
	 */
	void exitRuleLine(@NotNull SferaScriptGrammarParser.RuleLineContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(@NotNull SferaScriptGrammarParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#orExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(@NotNull SferaScriptGrammarParser.OrExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#stringComparison}.
	 * @param ctx the parse tree
	 */
	void enterStringComparison(@NotNull SferaScriptGrammarParser.StringComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#stringComparison}.
	 * @param ctx the parse tree
	 */
	void exitStringComparison(@NotNull SferaScriptGrammarParser.StringComparisonContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(@NotNull SferaScriptGrammarParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(@NotNull SferaScriptGrammarParser.AndExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#atomExpression}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpression(@NotNull SferaScriptGrammarParser.AtomExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#atomExpression}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpression(@NotNull SferaScriptGrammarParser.AtomExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#globalScopeInit}.
	 * @param ctx the parse tree
	 */
	void enterGlobalScopeInit(@NotNull SferaScriptGrammarParser.GlobalScopeInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#globalScopeInit}.
	 * @param ctx the parse tree
	 */
	void exitGlobalScopeInit(@NotNull SferaScriptGrammarParser.GlobalScopeInitContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#localScopeInit}.
	 * @param ctx the parse tree
	 */
	void enterLocalScopeInit(@NotNull SferaScriptGrammarParser.LocalScopeInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#localScopeInit}.
	 * @param ctx the parse tree
	 */
	void exitLocalScopeInit(@NotNull SferaScriptGrammarParser.LocalScopeInitContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(@NotNull SferaScriptGrammarParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(@NotNull SferaScriptGrammarParser.ActionContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(@NotNull SferaScriptGrammarParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(@NotNull SferaScriptGrammarParser.EventContext ctx);

	/**
	 * Enter a parse tree produced by {@link SferaScriptGrammarParser#stableEvent}.
	 * @param ctx the parse tree
	 */
	void enterStableEvent(@NotNull SferaScriptGrammarParser.StableEventContext ctx);
	/**
	 * Exit a parse tree produced by {@link SferaScriptGrammarParser#stableEvent}.
	 * @param ctx the parse tree
	 */
	void exitStableEvent(@NotNull SferaScriptGrammarParser.StableEventContext ctx);
}