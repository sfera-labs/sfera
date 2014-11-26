// Generated from SferaScriptGrammar.g4 by ANTLR 4.3
package cc.sferalabs.sfera.script.parser;
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
		T__1=1, T__0=2, Script=3, FinalNodeId=4, BooleanLiteral=5, Unknown=6, 
		NodeId=7, NumberLiteral=8, StringLiteral=9, LPAREN=10, RPAREN=11, DOT=12, 
		ET=13, NE=14, GT=15, LT=16, GE=17, LE=18, OR=19, AND=20, NOT=21, COLON=22, 
		WS=23, COMMENT=24, LINE_COMMENT=25;
	public static final String[] tokenNames = {
		"<INVALID>", "'local'", "'global'", "Script", "FinalNodeId", "BooleanLiteral", 
		"'unknown'", "NodeId", "NumberLiteral", "StringLiteral", "'('", "')'", 
		"'.'", "'=='", "'!='", "'>'", "'<'", "'>='", "'<='", "'||'", "'&&'", "'!'", 
		"':'", "WS", "COMMENT", "LINE_COMMENT"
	};
	public static final int
		RULE_parse = 0, RULE_init = 1, RULE_localScopeInit = 2, RULE_globalScopeInit = 3, 
		RULE_rules = 4, RULE_ruleLine = 5, RULE_action = 6, RULE_trigger = 7, 
		RULE_orExpression = 8, RULE_andExpression = 9, RULE_notExpression = 10, 
		RULE_atomExpression = 11, RULE_event = 12, RULE_stableEvent = 13, RULE_stringComparison = 14, 
		RULE_numberComparison = 15, RULE_booleanComparison = 16, RULE_unknownComparison = 17, 
		RULE_transientEvent = 18;
	public static final String[] ruleNames = {
		"parse", "init", "localScopeInit", "globalScopeInit", "rules", "ruleLine", 
		"action", "trigger", "orExpression", "andExpression", "notExpression", 
		"atomExpression", "event", "stableEvent", "stringComparison", "numberComparison", 
		"booleanComparison", "unknownComparison", "transientEvent"
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
			setState(38); init();
			setState(40);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FinalNodeId) | (1L << NodeId) | (1L << LPAREN) | (1L << NOT))) != 0)) {
				{
				setState(39); rules();
				}
			}

			setState(42); match(EOF);
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
		public LocalScopeInitContext localScopeInit() {
			return getRuleContext(LocalScopeInitContext.class,0);
		}
		public GlobalScopeInitContext globalScopeInit() {
			return getRuleContext(GlobalScopeInitContext.class,0);
		}
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
		int _la;
		try {
			setState(56);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(45);
				_la = _input.LA(1);
				if (_la==T__1 || _la==Script) {
					{
					setState(44); localScopeInit();
					}
				}

				setState(48);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(47); globalScopeInit();
					}
				}

				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(50); globalScopeInit();
					}
				}

				setState(54);
				_la = _input.LA(1);
				if (_la==T__1 || _la==Script) {
					{
					setState(53); localScopeInit();
					}
				}

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

	public static class LocalScopeInitContext extends ParserRuleContext {
		public TerminalNode Script() { return getToken(SferaScriptGrammarParser.Script, 0); }
		public LocalScopeInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localScopeInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterLocalScopeInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitLocalScopeInit(this);
		}
	}

	public final LocalScopeInitContext localScopeInit() throws RecognitionException {
		LocalScopeInitContext _localctx = new LocalScopeInitContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_localScopeInit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(58); match(T__1);
				}
			}

			setState(61); match(Script);
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

	public static class GlobalScopeInitContext extends ParserRuleContext {
		public TerminalNode Script() { return getToken(SferaScriptGrammarParser.Script, 0); }
		public GlobalScopeInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globalScopeInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterGlobalScopeInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitGlobalScopeInit(this);
		}
	}

	public final GlobalScopeInitContext globalScopeInit() throws RecognitionException {
		GlobalScopeInitContext _localctx = new GlobalScopeInitContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_globalScopeInit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63); match(T__0);
			setState(64); match(Script);
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
		enterRule(_localctx, 8, RULE_rules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(66); ruleLine();
				}
				}
				setState(69); 
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
		enterRule(_localctx, 10, RULE_ruleLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71); trigger();
			setState(72); match(COLON);
			setState(73); action();
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
		enterRule(_localctx, 12, RULE_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75); match(Script);
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
		enterRule(_localctx, 14, RULE_trigger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77); orExpression();
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
		enterRule(_localctx, 16, RULE_orExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79); andExpression();
			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(80); match(OR);
				setState(81); andExpression();
				}
				}
				setState(86);
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
		enterRule(_localctx, 18, RULE_andExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87); notExpression();
			setState(92);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(88); match(AND);
				setState(89); notExpression();
				}
				}
				setState(94);
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
		enterRule(_localctx, 20, RULE_notExpression);
		try {
			setState(98);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(95); match(NOT);
				setState(96); atomExpression();
				}
				break;
			case FinalNodeId:
			case NodeId:
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(97); atomExpression();
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
		enterRule(_localctx, 22, RULE_atomExpression);
		try {
			setState(105);
			switch (_input.LA(1)) {
			case FinalNodeId:
			case NodeId:
				enterOuterAlt(_localctx, 1);
				{
				setState(100); event();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(101); match(LPAREN);
				setState(102); orExpression();
				setState(103); match(RPAREN);
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
		enterRule(_localctx, 24, RULE_event);
		try {
			setState(109);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(107); stableEvent();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(108); transientEvent();
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
		enterRule(_localctx, 26, RULE_stableEvent);
		try {
			setState(115);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(111); stringComparison();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(112); numberComparison();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(113); booleanComparison();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(114); unknownComparison();
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
		enterRule(_localctx, 28, RULE_stringComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117); match(FinalNodeId);
			setState(118);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(119); match(StringLiteral);
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
		enterRule(_localctx, 30, RULE_numberComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121); match(FinalNodeId);
			setState(122);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(123); match(NumberLiteral);
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
		enterRule(_localctx, 32, RULE_booleanComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125); match(FinalNodeId);
			setState(126);
			_la = _input.LA(1);
			if ( !(_la==ET || _la==NE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(127); match(BooleanLiteral);
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
		enterRule(_localctx, 34, RULE_unknownComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129); match(FinalNodeId);
			setState(130);
			_la = _input.LA(1);
			if ( !(_la==ET || _la==NE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(131); match(Unknown);
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
		enterRule(_localctx, 36, RULE_transientEvent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\33\u008a\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\3\2\5\2+\n\2\3\2\3\2\3\3\5\3\60\n\3\3\3\5\3\63"+
		"\n\3\3\3\5\3\66\n\3\3\3\5\39\n\3\5\3;\n\3\3\4\5\4>\n\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\6\6\6F\n\6\r\6\16\6G\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3"+
		"\n\7\nU\n\n\f\n\16\nX\13\n\3\13\3\13\3\13\7\13]\n\13\f\13\16\13`\13\13"+
		"\3\f\3\f\3\f\5\fe\n\f\3\r\3\r\3\r\3\r\3\r\5\rl\n\r\3\16\3\16\5\16p\n\16"+
		"\3\17\3\17\3\17\3\17\5\17v\n\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21"+
		"\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\2\2\25\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&\2\5\3\2\17\24\3\2\17\20\4\2\6\6\t"+
		"\t\u0086\2(\3\2\2\2\4:\3\2\2\2\6=\3\2\2\2\bA\3\2\2\2\nE\3\2\2\2\fI\3\2"+
		"\2\2\16M\3\2\2\2\20O\3\2\2\2\22Q\3\2\2\2\24Y\3\2\2\2\26d\3\2\2\2\30k\3"+
		"\2\2\2\32o\3\2\2\2\34u\3\2\2\2\36w\3\2\2\2 {\3\2\2\2\"\177\3\2\2\2$\u0083"+
		"\3\2\2\2&\u0087\3\2\2\2(*\5\4\3\2)+\5\n\6\2*)\3\2\2\2*+\3\2\2\2+,\3\2"+
		"\2\2,-\7\2\2\3-\3\3\2\2\2.\60\5\6\4\2/.\3\2\2\2/\60\3\2\2\2\60\62\3\2"+
		"\2\2\61\63\5\b\5\2\62\61\3\2\2\2\62\63\3\2\2\2\63;\3\2\2\2\64\66\5\b\5"+
		"\2\65\64\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\679\5\6\4\28\67\3\2\2\289\3"+
		"\2\2\29;\3\2\2\2:/\3\2\2\2:\65\3\2\2\2;\5\3\2\2\2<>\7\3\2\2=<\3\2\2\2"+
		"=>\3\2\2\2>?\3\2\2\2?@\7\5\2\2@\7\3\2\2\2AB\7\4\2\2BC\7\5\2\2C\t\3\2\2"+
		"\2DF\5\f\7\2ED\3\2\2\2FG\3\2\2\2GE\3\2\2\2GH\3\2\2\2H\13\3\2\2\2IJ\5\20"+
		"\t\2JK\7\30\2\2KL\5\16\b\2L\r\3\2\2\2MN\7\5\2\2N\17\3\2\2\2OP\5\22\n\2"+
		"P\21\3\2\2\2QV\5\24\13\2RS\7\25\2\2SU\5\24\13\2TR\3\2\2\2UX\3\2\2\2VT"+
		"\3\2\2\2VW\3\2\2\2W\23\3\2\2\2XV\3\2\2\2Y^\5\26\f\2Z[\7\26\2\2[]\5\26"+
		"\f\2\\Z\3\2\2\2]`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_\25\3\2\2\2`^\3\2\2\2a"+
		"b\7\27\2\2be\5\30\r\2ce\5\30\r\2da\3\2\2\2dc\3\2\2\2e\27\3\2\2\2fl\5\32"+
		"\16\2gh\7\f\2\2hi\5\22\n\2ij\7\r\2\2jl\3\2\2\2kf\3\2\2\2kg\3\2\2\2l\31"+
		"\3\2\2\2mp\5\34\17\2np\5&\24\2om\3\2\2\2on\3\2\2\2p\33\3\2\2\2qv\5\36"+
		"\20\2rv\5 \21\2sv\5\"\22\2tv\5$\23\2uq\3\2\2\2ur\3\2\2\2us\3\2\2\2ut\3"+
		"\2\2\2v\35\3\2\2\2wx\7\6\2\2xy\t\2\2\2yz\7\13\2\2z\37\3\2\2\2{|\7\6\2"+
		"\2|}\t\2\2\2}~\7\n\2\2~!\3\2\2\2\177\u0080\7\6\2\2\u0080\u0081\t\3\2\2"+
		"\u0081\u0082\7\7\2\2\u0082#\3\2\2\2\u0083\u0084\7\6\2\2\u0084\u0085\t"+
		"\3\2\2\u0085\u0086\7\b\2\2\u0086%\3\2\2\2\u0087\u0088\t\4\2\2\u0088\'"+
		"\3\2\2\2\20*/\62\658:=GV^dkou";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}