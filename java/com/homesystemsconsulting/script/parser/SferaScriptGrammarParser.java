// Generated from SferaScriptGrammar.g4 by ANTLR 4.3
package com.homesystemsconsulting.script.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SferaScriptGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Script=1, FinalNodeId=2, BooleanLiteral=3, Unknown=4, NodeId=5, NumberLiteral=6, 
		StringLiteral=7, LPAREN=8, RPAREN=9, DOT=10, ET=11, NE=12, GT=13, LT=14, 
		GE=15, LE=16, OR=17, AND=18, NOT=19, COLON=20, WS=21, COMMENT=22, LINE_COMMENT=23;
	public static final String[] tokenNames = {
		"<INVALID>", "Script", "FinalNodeId", "BooleanLiteral", "'unknown'", "NodeId", 
		"NumberLiteral", "StringLiteral", "'('", "')'", "'.'", "'=='", "'!='", 
		"'>'", "'<'", "'>='", "'<='", "'||'", "'&&'", "'!'", "':'", "WS", "COMMENT", 
		"LINE_COMMENT"
	};
	public static final int
		RULE_parse = 0, RULE_init = 1, RULE_rules = 2, RULE_ruleLine = 3, RULE_action = 4, 
		RULE_trigger = 5, RULE_orExpression = 6, RULE_andExpression = 7, RULE_notExpression = 8, 
		RULE_atomExpression = 9, RULE_event = 10, RULE_stableEvent = 11, RULE_stringComparison = 12, 
		RULE_numberComparison = 13, RULE_booleanComparison = 14, RULE_unknownComparison = 15, 
		RULE_transientEvent = 16;
	public static final String[] ruleNames = {
		"parse", "init", "rules", "ruleLine", "action", "trigger", "orExpression", 
		"andExpression", "notExpression", "atomExpression", "event", "stableEvent", 
		"stringComparison", "numberComparison", "booleanComparison", "unknownComparison", 
		"transientEvent"
	};

	@Override
	public String getGrammarFileName() { return "SferaScriptGrammar.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SferaScriptGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ParseContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SferaScriptGrammarParser.EOF, 0); }
		public RulesContext rules() {
			return getRuleContext(RulesContext.class,0);
		}
		public InitContext init() {
			return getRuleContext(InitContext.class,0);
		}
		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitParse(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35);
			_la = _input.LA(1);
			if (_la==Script) {
				{
				setState(34); init();
				}
			}

			setState(38);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FinalNodeId) | (1L << NodeId) | (1L << LPAREN) | (1L << NOT))) != 0)) {
				{
				setState(37); rules();
				}
			}

			setState(40); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InitContext extends ParserRuleContext {
		public TerminalNode Script() { return getToken(SferaScriptGrammarParser.Script, 0); }
		public InitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitInit(this);
		}
	}

	public final InitContext init() throws RecognitionException {
		InitContext _localctx = new InitContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_init);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42); match(Script);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RulesContext extends ParserRuleContext {
		public RuleLineContext ruleLine(int i) {
			return getRuleContext(RuleLineContext.class,i);
		}
		public List<RuleLineContext> ruleLine() {
			return getRuleContexts(RuleLineContext.class);
		}
		public RulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rules; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterRules(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitRules(this);
		}
	}

	public final RulesContext rules() throws RecognitionException {
		RulesContext _localctx = new RulesContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_rules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(44); ruleLine();
				}
				}
				setState(47); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FinalNodeId) | (1L << NodeId) | (1L << LPAREN) | (1L << NOT))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleLineContext extends ParserRuleContext {
		public TriggerContext trigger() {
			return getRuleContext(TriggerContext.class,0);
		}
		public TerminalNode COLON() { return getToken(SferaScriptGrammarParser.COLON, 0); }
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public RuleLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterRuleLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitRuleLine(this);
		}
	}

	public final RuleLineContext ruleLine() throws RecognitionException {
		RuleLineContext _localctx = new RuleLineContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_ruleLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49); trigger();
			setState(50); match(COLON);
			setState(51); action();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionContext extends ParserRuleContext {
		public TerminalNode Script() { return getToken(SferaScriptGrammarParser.Script, 0); }
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitAction(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53); match(Script);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerContext extends ParserRuleContext {
		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class,0);
		}
		public TriggerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trigger; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterTrigger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitTrigger(this);
		}
	}

	public final TriggerContext trigger() throws RecognitionException {
		TriggerContext _localctx = new TriggerContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_trigger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55); orExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrExpressionContext extends ParserRuleContext {
		public List<AndExpressionContext> andExpression() {
			return getRuleContexts(AndExpressionContext.class);
		}
		public AndExpressionContext andExpression(int i) {
			return getRuleContext(AndExpressionContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(SferaScriptGrammarParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(SferaScriptGrammarParser.OR, i);
		}
		public OrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitOrExpression(this);
		}
	}

	public final OrExpressionContext orExpression() throws RecognitionException {
		OrExpressionContext _localctx = new OrExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_orExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57); andExpression();
			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(58); match(OR);
				setState(59); andExpression();
				}
				}
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndExpressionContext extends ParserRuleContext {
		public TerminalNode AND(int i) {
			return getToken(SferaScriptGrammarParser.AND, i);
		}
		public List<NotExpressionContext> notExpression() {
			return getRuleContexts(NotExpressionContext.class);
		}
		public List<TerminalNode> AND() { return getTokens(SferaScriptGrammarParser.AND); }
		public NotExpressionContext notExpression(int i) {
			return getRuleContext(NotExpressionContext.class,i);
		}
		public AndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitAndExpression(this);
		}
	}

	public final AndExpressionContext andExpression() throws RecognitionException {
		AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_andExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65); notExpression();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(66); match(AND);
				setState(67); notExpression();
				}
				}
				setState(72);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NotExpressionContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(SferaScriptGrammarParser.NOT, 0); }
		public AtomExpressionContext atomExpression() {
			return getRuleContext(AtomExpressionContext.class,0);
		}
		public NotExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterNotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitNotExpression(this);
		}
	}

	public final NotExpressionContext notExpression() throws RecognitionException {
		NotExpressionContext _localctx = new NotExpressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_notExpression);
		try {
			setState(76);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(73); match(NOT);
				setState(74); atomExpression();
				}
				break;
			case FinalNodeId:
			case NodeId:
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(75); atomExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomExpressionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(SferaScriptGrammarParser.LPAREN, 0); }
		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SferaScriptGrammarParser.RPAREN, 0); }
		public EventContext event() {
			return getRuleContext(EventContext.class,0);
		}
		public AtomExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterAtomExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitAtomExpression(this);
		}
	}

	public final AtomExpressionContext atomExpression() throws RecognitionException {
		AtomExpressionContext _localctx = new AtomExpressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_atomExpression);
		try {
			setState(83);
			switch (_input.LA(1)) {
			case FinalNodeId:
			case NodeId:
				enterOuterAlt(_localctx, 1);
				{
				setState(78); event();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(79); match(LPAREN);
				setState(80); orExpression();
				setState(81); match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventContext extends ParserRuleContext {
		public TransientEventContext transientEvent() {
			return getRuleContext(TransientEventContext.class,0);
		}
		public StableEventContext stableEvent() {
			return getRuleContext(StableEventContext.class,0);
		}
		public EventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitEvent(this);
		}
	}

	public final EventContext event() throws RecognitionException {
		EventContext _localctx = new EventContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_event);
		try {
			setState(87);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(85); stableEvent();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(86); transientEvent();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StableEventContext extends ParserRuleContext {
		public UnknownComparisonContext unknownComparison() {
			return getRuleContext(UnknownComparisonContext.class,0);
		}
		public StringComparisonContext stringComparison() {
			return getRuleContext(StringComparisonContext.class,0);
		}
		public BooleanComparisonContext booleanComparison() {
			return getRuleContext(BooleanComparisonContext.class,0);
		}
		public NumberComparisonContext numberComparison() {
			return getRuleContext(NumberComparisonContext.class,0);
		}
		public StableEventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stableEvent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterStableEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitStableEvent(this);
		}
	}

	public final StableEventContext stableEvent() throws RecognitionException {
		StableEventContext _localctx = new StableEventContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_stableEvent);
		try {
			setState(93);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(89); stringComparison();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(90); numberComparison();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(91); booleanComparison();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(92); unknownComparison();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringComparisonContext extends ParserRuleContext {
		public TerminalNode ET() { return getToken(SferaScriptGrammarParser.ET, 0); }
		public TerminalNode GE() { return getToken(SferaScriptGrammarParser.GE, 0); }
		public TerminalNode FinalNodeId() { return getToken(SferaScriptGrammarParser.FinalNodeId, 0); }
		public TerminalNode StringLiteral() { return getToken(SferaScriptGrammarParser.StringLiteral, 0); }
		public TerminalNode LT() { return getToken(SferaScriptGrammarParser.LT, 0); }
		public TerminalNode GT() { return getToken(SferaScriptGrammarParser.GT, 0); }
		public TerminalNode LE() { return getToken(SferaScriptGrammarParser.LE, 0); }
		public TerminalNode NE() { return getToken(SferaScriptGrammarParser.NE, 0); }
		public StringComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringComparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterStringComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitStringComparison(this);
		}
	}

	public final StringComparisonContext stringComparison() throws RecognitionException {
		StringComparisonContext _localctx = new StringComparisonContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_stringComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95); match(FinalNodeId);
			setState(96);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(97); match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberComparisonContext extends ParserRuleContext {
		public TerminalNode ET() { return getToken(SferaScriptGrammarParser.ET, 0); }
		public TerminalNode GE() { return getToken(SferaScriptGrammarParser.GE, 0); }
		public TerminalNode FinalNodeId() { return getToken(SferaScriptGrammarParser.FinalNodeId, 0); }
		public TerminalNode NumberLiteral() { return getToken(SferaScriptGrammarParser.NumberLiteral, 0); }
		public TerminalNode LT() { return getToken(SferaScriptGrammarParser.LT, 0); }
		public TerminalNode GT() { return getToken(SferaScriptGrammarParser.GT, 0); }
		public TerminalNode LE() { return getToken(SferaScriptGrammarParser.LE, 0); }
		public TerminalNode NE() { return getToken(SferaScriptGrammarParser.NE, 0); }
		public NumberComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberComparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterNumberComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitNumberComparison(this);
		}
	}

	public final NumberComparisonContext numberComparison() throws RecognitionException {
		NumberComparisonContext _localctx = new NumberComparisonContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_numberComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99); match(FinalNodeId);
			setState(100);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(101); match(NumberLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanComparisonContext extends ParserRuleContext {
		public TerminalNode ET() { return getToken(SferaScriptGrammarParser.ET, 0); }
		public TerminalNode FinalNodeId() { return getToken(SferaScriptGrammarParser.FinalNodeId, 0); }
		public TerminalNode BooleanLiteral() { return getToken(SferaScriptGrammarParser.BooleanLiteral, 0); }
		public TerminalNode NE() { return getToken(SferaScriptGrammarParser.NE, 0); }
		public BooleanComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanComparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterBooleanComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitBooleanComparison(this);
		}
	}

	public final BooleanComparisonContext booleanComparison() throws RecognitionException {
		BooleanComparisonContext _localctx = new BooleanComparisonContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_booleanComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103); match(FinalNodeId);
			setState(104);
			_la = _input.LA(1);
			if ( !(_la==ET || _la==NE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(105); match(BooleanLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnknownComparisonContext extends ParserRuleContext {
		public TerminalNode ET() { return getToken(SferaScriptGrammarParser.ET, 0); }
		public TerminalNode FinalNodeId() { return getToken(SferaScriptGrammarParser.FinalNodeId, 0); }
		public TerminalNode Unknown() { return getToken(SferaScriptGrammarParser.Unknown, 0); }
		public TerminalNode NE() { return getToken(SferaScriptGrammarParser.NE, 0); }
		public UnknownComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unknownComparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterUnknownComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitUnknownComparison(this);
		}
	}

	public final UnknownComparisonContext unknownComparison() throws RecognitionException {
		UnknownComparisonContext _localctx = new UnknownComparisonContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_unknownComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107); match(FinalNodeId);
			setState(108);
			_la = _input.LA(1);
			if ( !(_la==ET || _la==NE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(109); match(Unknown);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TransientEventContext extends ParserRuleContext {
		public TerminalNode FinalNodeId() { return getToken(SferaScriptGrammarParser.FinalNodeId, 0); }
		public TerminalNode NodeId() { return getToken(SferaScriptGrammarParser.NodeId, 0); }
		public TransientEventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transientEvent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterTransientEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitTransientEvent(this);
		}
	}

	public final TransientEventContext transientEvent() throws RecognitionException {
		TransientEventContext _localctx = new TransientEventContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_transientEvent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			_la = _input.LA(1);
			if ( !(_la==FinalNodeId || _la==NodeId) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\31t\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22\3\2\5"+
		"\2&\n\2\3\2\5\2)\n\2\3\2\3\2\3\3\3\3\3\4\6\4\60\n\4\r\4\16\4\61\3\5\3"+
		"\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\7\b?\n\b\f\b\16\bB\13\b\3\t\3\t"+
		"\3\t\7\tG\n\t\f\t\16\tJ\13\t\3\n\3\n\3\n\5\nO\n\n\3\13\3\13\3\13\3\13"+
		"\3\13\5\13V\n\13\3\f\3\f\5\fZ\n\f\3\r\3\r\3\r\3\r\5\r`\n\r\3\16\3\16\3"+
		"\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3"+
		"\22\3\22\3\22\2\2\23\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"\2\5\3\2"+
		"\r\22\3\2\r\16\4\2\4\4\7\7m\2%\3\2\2\2\4,\3\2\2\2\6/\3\2\2\2\b\63\3\2"+
		"\2\2\n\67\3\2\2\2\f9\3\2\2\2\16;\3\2\2\2\20C\3\2\2\2\22N\3\2\2\2\24U\3"+
		"\2\2\2\26Y\3\2\2\2\30_\3\2\2\2\32a\3\2\2\2\34e\3\2\2\2\36i\3\2\2\2 m\3"+
		"\2\2\2\"q\3\2\2\2$&\5\4\3\2%$\3\2\2\2%&\3\2\2\2&(\3\2\2\2\')\5\6\4\2("+
		"\'\3\2\2\2()\3\2\2\2)*\3\2\2\2*+\7\2\2\3+\3\3\2\2\2,-\7\3\2\2-\5\3\2\2"+
		"\2.\60\5\b\5\2/.\3\2\2\2\60\61\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\7"+
		"\3\2\2\2\63\64\5\f\7\2\64\65\7\26\2\2\65\66\5\n\6\2\66\t\3\2\2\2\678\7"+
		"\3\2\28\13\3\2\2\29:\5\16\b\2:\r\3\2\2\2;@\5\20\t\2<=\7\23\2\2=?\5\20"+
		"\t\2><\3\2\2\2?B\3\2\2\2@>\3\2\2\2@A\3\2\2\2A\17\3\2\2\2B@\3\2\2\2CH\5"+
		"\22\n\2DE\7\24\2\2EG\5\22\n\2FD\3\2\2\2GJ\3\2\2\2HF\3\2\2\2HI\3\2\2\2"+
		"I\21\3\2\2\2JH\3\2\2\2KL\7\25\2\2LO\5\24\13\2MO\5\24\13\2NK\3\2\2\2NM"+
		"\3\2\2\2O\23\3\2\2\2PV\5\26\f\2QR\7\n\2\2RS\5\16\b\2ST\7\13\2\2TV\3\2"+
		"\2\2UP\3\2\2\2UQ\3\2\2\2V\25\3\2\2\2WZ\5\30\r\2XZ\5\"\22\2YW\3\2\2\2Y"+
		"X\3\2\2\2Z\27\3\2\2\2[`\5\32\16\2\\`\5\34\17\2]`\5\36\20\2^`\5 \21\2_"+
		"[\3\2\2\2_\\\3\2\2\2_]\3\2\2\2_^\3\2\2\2`\31\3\2\2\2ab\7\4\2\2bc\t\2\2"+
		"\2cd\7\t\2\2d\33\3\2\2\2ef\7\4\2\2fg\t\2\2\2gh\7\b\2\2h\35\3\2\2\2ij\7"+
		"\4\2\2jk\t\3\2\2kl\7\5\2\2l\37\3\2\2\2mn\7\4\2\2no\t\3\2\2op\7\6\2\2p"+
		"!\3\2\2\2qr\t\4\2\2r#\3\2\2\2\13%(\61@HNUY_";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}