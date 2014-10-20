// Generated from EventsGrammar.g4 by ANTLR 4.3
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
public class EventsGrammarParser extends Parser {
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
		RULE_parse = 0, RULE_rules = 1, RULE_ruleLine = 2, RULE_action = 3, RULE_trigger = 4, 
		RULE_orExpression = 5, RULE_andExpression = 6, RULE_notExpression = 7, 
		RULE_atomExpression = 8, RULE_event = 9, RULE_stableEvent = 10, RULE_stringComparison = 11, 
		RULE_numberComparison = 12, RULE_booleanComparison = 13, RULE_unknownComparison = 14, 
		RULE_transientEvent = 15;
	public static final String[] ruleNames = {
		"parse", "rules", "ruleLine", "action", "trigger", "orExpression", "andExpression", 
		"notExpression", "atomExpression", "event", "stableEvent", "stringComparison", 
		"numberComparison", "booleanComparison", "unknownComparison", "transientEvent"
	};

	@Override
	public String getGrammarFileName() { return "EventsGrammar.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public EventsGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ParseContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(EventsGrammarParser.EOF, 0); }
		public RulesContext rules() {
			return getRuleContext(RulesContext.class,0);
		}
		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitParse(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FinalNodeId) | (1L << NodeId) | (1L << LPAREN) | (1L << NOT))) != 0)) {
				{
				setState(32); rules();
				}
			}

			setState(35); match(EOF);
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
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterRules(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitRules(this);
		}
	}

	public final RulesContext rules() throws RecognitionException {
		RulesContext _localctx = new RulesContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_rules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(37); ruleLine();
				}
				}
				setState(40); 
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
		public TerminalNode COLON() { return getToken(EventsGrammarParser.COLON, 0); }
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public RuleLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterRuleLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitRuleLine(this);
		}
	}

	public final RuleLineContext ruleLine() throws RecognitionException {
		RuleLineContext _localctx = new RuleLineContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_ruleLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42); trigger();
			setState(43); match(COLON);
			setState(44); action();
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
		public TerminalNode Script() { return getToken(EventsGrammarParser.Script, 0); }
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitAction(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46); match(Script);
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
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterTrigger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitTrigger(this);
		}
	}

	public final TriggerContext trigger() throws RecognitionException {
		TriggerContext _localctx = new TriggerContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_trigger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48); orExpression();
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
		public List<TerminalNode> OR() { return getTokens(EventsGrammarParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(EventsGrammarParser.OR, i);
		}
		public OrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitOrExpression(this);
		}
	}

	public final OrExpressionContext orExpression() throws RecognitionException {
		OrExpressionContext _localctx = new OrExpressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_orExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50); andExpression();
			setState(55);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(51); match(OR);
				setState(52); andExpression();
				}
				}
				setState(57);
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
			return getToken(EventsGrammarParser.AND, i);
		}
		public List<NotExpressionContext> notExpression() {
			return getRuleContexts(NotExpressionContext.class);
		}
		public List<TerminalNode> AND() { return getTokens(EventsGrammarParser.AND); }
		public NotExpressionContext notExpression(int i) {
			return getRuleContext(NotExpressionContext.class,i);
		}
		public AndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitAndExpression(this);
		}
	}

	public final AndExpressionContext andExpression() throws RecognitionException {
		AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_andExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58); notExpression();
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(59); match(AND);
				setState(60); notExpression();
				}
				}
				setState(65);
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
		public TerminalNode NOT() { return getToken(EventsGrammarParser.NOT, 0); }
		public AtomExpressionContext atomExpression() {
			return getRuleContext(AtomExpressionContext.class,0);
		}
		public NotExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterNotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitNotExpression(this);
		}
	}

	public final NotExpressionContext notExpression() throws RecognitionException {
		NotExpressionContext _localctx = new NotExpressionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_notExpression);
		try {
			setState(69);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(66); match(NOT);
				setState(67); atomExpression();
				}
				break;
			case FinalNodeId:
			case NodeId:
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(68); atomExpression();
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
		public TerminalNode LPAREN() { return getToken(EventsGrammarParser.LPAREN, 0); }
		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(EventsGrammarParser.RPAREN, 0); }
		public EventContext event() {
			return getRuleContext(EventContext.class,0);
		}
		public AtomExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterAtomExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitAtomExpression(this);
		}
	}

	public final AtomExpressionContext atomExpression() throws RecognitionException {
		AtomExpressionContext _localctx = new AtomExpressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_atomExpression);
		try {
			setState(76);
			switch (_input.LA(1)) {
			case FinalNodeId:
			case NodeId:
				enterOuterAlt(_localctx, 1);
				{
				setState(71); event();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(72); match(LPAREN);
				setState(73); orExpression();
				setState(74); match(RPAREN);
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
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitEvent(this);
		}
	}

	public final EventContext event() throws RecognitionException {
		EventContext _localctx = new EventContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_event);
		try {
			setState(80);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(78); stableEvent();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(79); transientEvent();
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
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterStableEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitStableEvent(this);
		}
	}

	public final StableEventContext stableEvent() throws RecognitionException {
		StableEventContext _localctx = new StableEventContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_stableEvent);
		try {
			setState(86);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(82); stringComparison();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(83); numberComparison();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(84); booleanComparison();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(85); unknownComparison();
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
		public TerminalNode ET() { return getToken(EventsGrammarParser.ET, 0); }
		public TerminalNode GE() { return getToken(EventsGrammarParser.GE, 0); }
		public TerminalNode FinalNodeId() { return getToken(EventsGrammarParser.FinalNodeId, 0); }
		public TerminalNode StringLiteral() { return getToken(EventsGrammarParser.StringLiteral, 0); }
		public TerminalNode LT() { return getToken(EventsGrammarParser.LT, 0); }
		public TerminalNode GT() { return getToken(EventsGrammarParser.GT, 0); }
		public TerminalNode LE() { return getToken(EventsGrammarParser.LE, 0); }
		public TerminalNode NE() { return getToken(EventsGrammarParser.NE, 0); }
		public StringComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringComparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterStringComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitStringComparison(this);
		}
	}

	public final StringComparisonContext stringComparison() throws RecognitionException {
		StringComparisonContext _localctx = new StringComparisonContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_stringComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88); match(FinalNodeId);
			setState(89);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(90); match(StringLiteral);
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
		public TerminalNode ET() { return getToken(EventsGrammarParser.ET, 0); }
		public TerminalNode GE() { return getToken(EventsGrammarParser.GE, 0); }
		public TerminalNode FinalNodeId() { return getToken(EventsGrammarParser.FinalNodeId, 0); }
		public TerminalNode NumberLiteral() { return getToken(EventsGrammarParser.NumberLiteral, 0); }
		public TerminalNode LT() { return getToken(EventsGrammarParser.LT, 0); }
		public TerminalNode GT() { return getToken(EventsGrammarParser.GT, 0); }
		public TerminalNode LE() { return getToken(EventsGrammarParser.LE, 0); }
		public TerminalNode NE() { return getToken(EventsGrammarParser.NE, 0); }
		public NumberComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberComparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterNumberComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitNumberComparison(this);
		}
	}

	public final NumberComparisonContext numberComparison() throws RecognitionException {
		NumberComparisonContext _localctx = new NumberComparisonContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_numberComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92); match(FinalNodeId);
			setState(93);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(94); match(NumberLiteral);
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
		public TerminalNode ET() { return getToken(EventsGrammarParser.ET, 0); }
		public TerminalNode FinalNodeId() { return getToken(EventsGrammarParser.FinalNodeId, 0); }
		public TerminalNode BooleanLiteral() { return getToken(EventsGrammarParser.BooleanLiteral, 0); }
		public TerminalNode NE() { return getToken(EventsGrammarParser.NE, 0); }
		public BooleanComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanComparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterBooleanComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitBooleanComparison(this);
		}
	}

	public final BooleanComparisonContext booleanComparison() throws RecognitionException {
		BooleanComparisonContext _localctx = new BooleanComparisonContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_booleanComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96); match(FinalNodeId);
			setState(97);
			_la = _input.LA(1);
			if ( !(_la==ET || _la==NE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(98); match(BooleanLiteral);
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
		public TerminalNode ET() { return getToken(EventsGrammarParser.ET, 0); }
		public TerminalNode FinalNodeId() { return getToken(EventsGrammarParser.FinalNodeId, 0); }
		public TerminalNode Unknown() { return getToken(EventsGrammarParser.Unknown, 0); }
		public TerminalNode NE() { return getToken(EventsGrammarParser.NE, 0); }
		public UnknownComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unknownComparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterUnknownComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitUnknownComparison(this);
		}
	}

	public final UnknownComparisonContext unknownComparison() throws RecognitionException {
		UnknownComparisonContext _localctx = new UnknownComparisonContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_unknownComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100); match(FinalNodeId);
			setState(101);
			_la = _input.LA(1);
			if ( !(_la==ET || _la==NE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(102); match(Unknown);
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
		public TerminalNode FinalNodeId() { return getToken(EventsGrammarParser.FinalNodeId, 0); }
		public TerminalNode NodeId() { return getToken(EventsGrammarParser.NodeId, 0); }
		public TransientEventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transientEvent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).enterTransientEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof EventsGrammarListener ) ((EventsGrammarListener)listener).exitTransientEvent(this);
		}
	}

	public final TransientEventContext transientEvent() throws RecognitionException {
		TransientEventContext _localctx = new TransientEventContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_transientEvent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\31m\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\5\2$\n\2\3"+
		"\2\3\2\3\3\6\3)\n\3\r\3\16\3*\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7"+
		"\3\7\7\78\n\7\f\7\16\7;\13\7\3\b\3\b\3\b\7\b@\n\b\f\b\16\bC\13\b\3\t\3"+
		"\t\3\t\5\tH\n\t\3\n\3\n\3\n\3\n\3\n\5\nO\n\n\3\13\3\13\5\13S\n\13\3\f"+
		"\3\f\3\f\3\f\5\fY\n\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\2\2\22\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\32\34\36 \2\5\3\2\r\22\3\2\r\16\4\2\4\4\7\7f\2#\3\2\2\2\4(\3"+
		"\2\2\2\6,\3\2\2\2\b\60\3\2\2\2\n\62\3\2\2\2\f\64\3\2\2\2\16<\3\2\2\2\20"+
		"G\3\2\2\2\22N\3\2\2\2\24R\3\2\2\2\26X\3\2\2\2\30Z\3\2\2\2\32^\3\2\2\2"+
		"\34b\3\2\2\2\36f\3\2\2\2 j\3\2\2\2\"$\5\4\3\2#\"\3\2\2\2#$\3\2\2\2$%\3"+
		"\2\2\2%&\7\2\2\3&\3\3\2\2\2\')\5\6\4\2(\'\3\2\2\2)*\3\2\2\2*(\3\2\2\2"+
		"*+\3\2\2\2+\5\3\2\2\2,-\5\n\6\2-.\7\26\2\2./\5\b\5\2/\7\3\2\2\2\60\61"+
		"\7\3\2\2\61\t\3\2\2\2\62\63\5\f\7\2\63\13\3\2\2\2\649\5\16\b\2\65\66\7"+
		"\23\2\2\668\5\16\b\2\67\65\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:\r"+
		"\3\2\2\2;9\3\2\2\2<A\5\20\t\2=>\7\24\2\2>@\5\20\t\2?=\3\2\2\2@C\3\2\2"+
		"\2A?\3\2\2\2AB\3\2\2\2B\17\3\2\2\2CA\3\2\2\2DE\7\25\2\2EH\5\22\n\2FH\5"+
		"\22\n\2GD\3\2\2\2GF\3\2\2\2H\21\3\2\2\2IO\5\24\13\2JK\7\n\2\2KL\5\f\7"+
		"\2LM\7\13\2\2MO\3\2\2\2NI\3\2\2\2NJ\3\2\2\2O\23\3\2\2\2PS\5\26\f\2QS\5"+
		" \21\2RP\3\2\2\2RQ\3\2\2\2S\25\3\2\2\2TY\5\30\r\2UY\5\32\16\2VY\5\34\17"+
		"\2WY\5\36\20\2XT\3\2\2\2XU\3\2\2\2XV\3\2\2\2XW\3\2\2\2Y\27\3\2\2\2Z[\7"+
		"\4\2\2[\\\t\2\2\2\\]\7\t\2\2]\31\3\2\2\2^_\7\4\2\2_`\t\2\2\2`a\7\b\2\2"+
		"a\33\3\2\2\2bc\7\4\2\2cd\t\3\2\2de\7\5\2\2e\35\3\2\2\2fg\7\4\2\2gh\t\3"+
		"\2\2hi\7\6\2\2i\37\3\2\2\2jk\t\4\2\2k!\3\2\2\2\n#*9AGNRX";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}