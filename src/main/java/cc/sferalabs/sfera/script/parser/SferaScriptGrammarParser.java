// Generated from SferaScriptGrammar.g4 by ANTLR 4.5
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
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		Script=10, BooleanLiteral=11, Unknown=12, NodeId=13, NumberLiteral=14, 
		StringLiteral=15, ET=16, NE=17, GT=18, LT=19, GE=20, LE=21, OR=22, AND=23, 
		NOT=24, WS=25, COMMENT=26, LINE_COMMENT=27;
	public static final int
		RULE_parse = 0, RULE_init = 1, RULE_localScopeInit = 2, RULE_globalScopeInit = 3, 
		RULE_rules = 4, RULE_ruleLine = 5, RULE_action = 6, RULE_trigger = 7, 
		RULE_orExpression = 8, RULE_andExpression = 9, RULE_notExpression = 10, 
		RULE_atomExpression = 11, RULE_event = 12, RULE_stableEvent = 13, RULE_stringComparison = 14, 
		RULE_numberComparison = 15, RULE_booleanComparison = 16, RULE_unknownComparison = 17, 
		RULE_transientEvent = 18, RULE_terminalNode = 19, RULE_subNode = 20, RULE_parameters = 21, 
		RULE_paramsList = 22, RULE_parameter = 23, RULE_array = 24;
	public static final String[] ruleNames = {
		"parse", "init", "localScopeInit", "globalScopeInit", "rules", "ruleLine", 
		"action", "trigger", "orExpression", "andExpression", "notExpression", 
		"atomExpression", "event", "stableEvent", "stringComparison", "numberComparison", 
		"booleanComparison", "unknownComparison", "transientEvent", "terminalNode", 
		"subNode", "parameters", "paramsList", "parameter", "array"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'local'", "'global'", "':'", "'('", "')'", "'.'", "','", "'['", 
		"']'", null, null, "'unknown'", null, null, null, "'=='", "'!='", "'>'", 
		"'<'", "'>='", "'<='", "'||'", "'&&'", "'!'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, "Script", 
		"BooleanLiteral", "Unknown", "NodeId", "NumberLiteral", "StringLiteral", 
		"ET", "NE", "GT", "LT", "GE", "LE", "OR", "AND", "NOT", "WS", "COMMENT", 
		"LINE_COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SferaScriptGrammar.g4"; }

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
		public InitContext init() {
			return getRuleContext(InitContext.class,0);
		}
		public TerminalNode EOF() { return getToken(SferaScriptGrammarParser.EOF, 0); }
		public RulesContext rules() {
			return getRuleContext(RulesContext.class,0);
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
			setState(50);
			init();
			setState(52);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << NodeId) | (1L << NOT))) != 0)) {
				{
				setState(51);
				rules();
				}
			}

			setState(54);
			match(EOF);
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
			setState(68);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(57);
				_la = _input.LA(1);
				if (_la==T__0 || _la==Script) {
					{
					setState(56);
					localScopeInit();
					}
				}

				setState(60);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(59);
					globalScopeInit();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(63);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(62);
					globalScopeInit();
					}
				}

				setState(66);
				_la = _input.LA(1);
				if (_la==T__0 || _la==Script) {
					{
					setState(65);
					localScopeInit();
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
			setState(71);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(70);
				match(T__0);
				}
			}

			setState(73);
			match(Script);
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
			setState(75);
			match(T__1);
			setState(76);
			match(Script);
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
		public List<RuleLineContext> ruleLine() {
			return getRuleContexts(RuleLineContext.class);
		}
		public RuleLineContext ruleLine(int i) {
			return getRuleContext(RuleLineContext.class,i);
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
			setState(79); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(78);
				ruleLine();
				}
				}
				setState(81); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << NodeId) | (1L << NOT))) != 0) );
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
			setState(83);
			trigger();
			setState(84);
			match(T__2);
			setState(85);
			action();
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
			setState(87);
			match(Script);
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
			setState(89);
			orExpression();
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
			setState(91);
			andExpression();
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(92);
				match(OR);
				setState(93);
				andExpression();
				}
				}
				setState(98);
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
		public List<NotExpressionContext> notExpression() {
			return getRuleContexts(NotExpressionContext.class);
		}
		public NotExpressionContext notExpression(int i) {
			return getRuleContext(NotExpressionContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(SferaScriptGrammarParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(SferaScriptGrammarParser.AND, i);
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
			setState(99);
			notExpression();
			setState(104);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(100);
				match(AND);
				setState(101);
				notExpression();
				}
				}
				setState(106);
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
			setState(110);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(107);
				match(NOT);
				setState(108);
				atomExpression();
				}
				break;
			case T__3:
			case NodeId:
				enterOuterAlt(_localctx, 2);
				{
				setState(109);
				atomExpression();
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
		public EventContext event() {
			return getRuleContext(EventContext.class,0);
		}
		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class,0);
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
			setState(117);
			switch (_input.LA(1)) {
			case NodeId:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				event();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(113);
				match(T__3);
				setState(114);
				orExpression();
				setState(115);
				match(T__4);
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
		public StableEventContext stableEvent() {
			return getRuleContext(StableEventContext.class,0);
		}
		public TransientEventContext transientEvent() {
			return getRuleContext(TransientEventContext.class,0);
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
			setState(121);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(119);
				stableEvent();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(120);
				transientEvent();
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
		public StringComparisonContext stringComparison() {
			return getRuleContext(StringComparisonContext.class,0);
		}
		public NumberComparisonContext numberComparison() {
			return getRuleContext(NumberComparisonContext.class,0);
		}
		public BooleanComparisonContext booleanComparison() {
			return getRuleContext(BooleanComparisonContext.class,0);
		}
		public UnknownComparisonContext unknownComparison() {
			return getRuleContext(UnknownComparisonContext.class,0);
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
			setState(127);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(123);
				stringComparison();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(124);
				numberComparison();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(125);
				booleanComparison();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(126);
				unknownComparison();
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
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class,0);
		}
		public TerminalNode StringLiteral() { return getToken(SferaScriptGrammarParser.StringLiteral, 0); }
		public TerminalNode ET() { return getToken(SferaScriptGrammarParser.ET, 0); }
		public TerminalNode NE() { return getToken(SferaScriptGrammarParser.NE, 0); }
		public TerminalNode GT() { return getToken(SferaScriptGrammarParser.GT, 0); }
		public TerminalNode LT() { return getToken(SferaScriptGrammarParser.LT, 0); }
		public TerminalNode GE() { return getToken(SferaScriptGrammarParser.GE, 0); }
		public TerminalNode LE() { return getToken(SferaScriptGrammarParser.LE, 0); }
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
			setState(129);
			terminalNode();
			setState(130);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(131);
			match(StringLiteral);
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
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class,0);
		}
		public TerminalNode NumberLiteral() { return getToken(SferaScriptGrammarParser.NumberLiteral, 0); }
		public TerminalNode ET() { return getToken(SferaScriptGrammarParser.ET, 0); }
		public TerminalNode NE() { return getToken(SferaScriptGrammarParser.NE, 0); }
		public TerminalNode GT() { return getToken(SferaScriptGrammarParser.GT, 0); }
		public TerminalNode LT() { return getToken(SferaScriptGrammarParser.LT, 0); }
		public TerminalNode GE() { return getToken(SferaScriptGrammarParser.GE, 0); }
		public TerminalNode LE() { return getToken(SferaScriptGrammarParser.LE, 0); }
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
			setState(133);
			terminalNode();
			setState(134);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(135);
			match(NumberLiteral);
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
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class,0);
		}
		public TerminalNode BooleanLiteral() { return getToken(SferaScriptGrammarParser.BooleanLiteral, 0); }
		public TerminalNode ET() { return getToken(SferaScriptGrammarParser.ET, 0); }
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
			setState(137);
			terminalNode();
			setState(138);
			_la = _input.LA(1);
			if ( !(_la==ET || _la==NE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(139);
			match(BooleanLiteral);
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
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class,0);
		}
		public TerminalNode Unknown() { return getToken(SferaScriptGrammarParser.Unknown, 0); }
		public TerminalNode ET() { return getToken(SferaScriptGrammarParser.ET, 0); }
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
			setState(141);
			terminalNode();
			setState(142);
			_la = _input.LA(1);
			if ( !(_la==ET || _la==NE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(143);
			match(Unknown);
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
		public TerminalNode NodeId() { return getToken(SferaScriptGrammarParser.NodeId, 0); }
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class,0);
		}
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
		try {
			setState(147);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(145);
				match(NodeId);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(146);
				terminalNode();
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

	public static class TerminalNodeContext extends ParserRuleContext {
		public TerminalNode NodeId() { return getToken(SferaScriptGrammarParser.NodeId, 0); }
		public List<SubNodeContext> subNode() {
			return getRuleContexts(SubNodeContext.class);
		}
		public SubNodeContext subNode(int i) {
			return getRuleContext(SubNodeContext.class,i);
		}
		public TerminalNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terminalNode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterTerminalNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitTerminalNode(this);
		}
	}

	public final TerminalNodeContext terminalNode() throws RecognitionException {
		TerminalNodeContext _localctx = new TerminalNodeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_terminalNode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(NodeId);
			setState(152); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(150);
				match(T__5);
				setState(151);
				subNode();
				}
				}
				setState(154); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__5 );
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

	public static class SubNodeContext extends ParserRuleContext {
		public TerminalNode NodeId() { return getToken(SferaScriptGrammarParser.NodeId, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public SubNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subNode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterSubNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitSubNode(this);
		}
	}

	public final SubNodeContext subNode() throws RecognitionException {
		SubNodeContext _localctx = new SubNodeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_subNode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(NodeId);
			setState(158);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(157);
				parameters();
				}
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

	public static class ParametersContext extends ParserRuleContext {
		public ParamsListContext paramsList() {
			return getRuleContext(ParamsListContext.class,0);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitParameters(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_parameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			match(T__3);
			setState(162);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << BooleanLiteral) | (1L << NumberLiteral) | (1L << StringLiteral))) != 0)) {
				{
				setState(161);
				paramsList();
				}
			}

			setState(164);
			match(T__4);
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

	public static class ParamsListContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public ParamsListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramsList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterParamsList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitParamsList(this);
		}
	}

	public final ParamsListContext paramsList() throws RecognitionException {
		ParamsListContext _localctx = new ParamsListContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_paramsList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			parameter();
			setState(171);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(167);
				match(T__6);
				setState(168);
				parameter();
				}
				}
				setState(173);
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

	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode NumberLiteral() { return getToken(SferaScriptGrammarParser.NumberLiteral, 0); }
		public TerminalNode StringLiteral() { return getToken(SferaScriptGrammarParser.StringLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(SferaScriptGrammarParser.BooleanLiteral, 0); }
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitParameter(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_parameter);
		try {
			setState(178);
			switch (_input.LA(1)) {
			case NumberLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(174);
				match(NumberLiteral);
				}
				break;
			case StringLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(175);
				match(StringLiteral);
				}
				break;
			case BooleanLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(176);
				match(BooleanLiteral);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 4);
				{
				setState(177);
				array();
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

	public static class ArrayContext extends ParserRuleContext {
		public ParamsListContext paramsList() {
			return getRuleContext(ParamsListContext.class,0);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SferaScriptGrammarListener ) ((SferaScriptGrammarListener)listener).exitArray(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_array);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			match(T__7);
			setState(181);
			paramsList();
			setState(182);
			match(T__8);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\35\u00bb\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\3\2\3\2\5\2\67\n\2\3\2\3\2\3\3\5\3<\n\3\3\3\5\3?\n\3\3\3\5"+
		"\3B\n\3\3\3\5\3E\n\3\5\3G\n\3\3\4\5\4J\n\4\3\4\3\4\3\5\3\5\3\5\3\6\6\6"+
		"R\n\6\r\6\16\6S\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\7\na\n\n\f"+
		"\n\16\nd\13\n\3\13\3\13\3\13\7\13i\n\13\f\13\16\13l\13\13\3\f\3\f\3\f"+
		"\5\fq\n\f\3\r\3\r\3\r\3\r\3\r\5\rx\n\r\3\16\3\16\5\16|\n\16\3\17\3\17"+
		"\3\17\3\17\5\17\u0082\n\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22"+
		"\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\5\24\u0096\n\24\3\25\3\25"+
		"\3\25\6\25\u009b\n\25\r\25\16\25\u009c\3\26\3\26\5\26\u00a1\n\26\3\27"+
		"\3\27\5\27\u00a5\n\27\3\27\3\27\3\30\3\30\3\30\7\30\u00ac\n\30\f\30\16"+
		"\30\u00af\13\30\3\31\3\31\3\31\3\31\5\31\u00b5\n\31\3\32\3\32\3\32\3\32"+
		"\3\32\2\2\33\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\2\4"+
		"\3\2\22\27\3\2\22\23\u00b9\2\64\3\2\2\2\4F\3\2\2\2\6I\3\2\2\2\bM\3\2\2"+
		"\2\nQ\3\2\2\2\fU\3\2\2\2\16Y\3\2\2\2\20[\3\2\2\2\22]\3\2\2\2\24e\3\2\2"+
		"\2\26p\3\2\2\2\30w\3\2\2\2\32{\3\2\2\2\34\u0081\3\2\2\2\36\u0083\3\2\2"+
		"\2 \u0087\3\2\2\2\"\u008b\3\2\2\2$\u008f\3\2\2\2&\u0095\3\2\2\2(\u0097"+
		"\3\2\2\2*\u009e\3\2\2\2,\u00a2\3\2\2\2.\u00a8\3\2\2\2\60\u00b4\3\2\2\2"+
		"\62\u00b6\3\2\2\2\64\66\5\4\3\2\65\67\5\n\6\2\66\65\3\2\2\2\66\67\3\2"+
		"\2\2\678\3\2\2\289\7\2\2\39\3\3\2\2\2:<\5\6\4\2;:\3\2\2\2;<\3\2\2\2<>"+
		"\3\2\2\2=?\5\b\5\2>=\3\2\2\2>?\3\2\2\2?G\3\2\2\2@B\5\b\5\2A@\3\2\2\2A"+
		"B\3\2\2\2BD\3\2\2\2CE\5\6\4\2DC\3\2\2\2DE\3\2\2\2EG\3\2\2\2F;\3\2\2\2"+
		"FA\3\2\2\2G\5\3\2\2\2HJ\7\3\2\2IH\3\2\2\2IJ\3\2\2\2JK\3\2\2\2KL\7\f\2"+
		"\2L\7\3\2\2\2MN\7\4\2\2NO\7\f\2\2O\t\3\2\2\2PR\5\f\7\2QP\3\2\2\2RS\3\2"+
		"\2\2SQ\3\2\2\2ST\3\2\2\2T\13\3\2\2\2UV\5\20\t\2VW\7\5\2\2WX\5\16\b\2X"+
		"\r\3\2\2\2YZ\7\f\2\2Z\17\3\2\2\2[\\\5\22\n\2\\\21\3\2\2\2]b\5\24\13\2"+
		"^_\7\30\2\2_a\5\24\13\2`^\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2c\23\3"+
		"\2\2\2db\3\2\2\2ej\5\26\f\2fg\7\31\2\2gi\5\26\f\2hf\3\2\2\2il\3\2\2\2"+
		"jh\3\2\2\2jk\3\2\2\2k\25\3\2\2\2lj\3\2\2\2mn\7\32\2\2nq\5\30\r\2oq\5\30"+
		"\r\2pm\3\2\2\2po\3\2\2\2q\27\3\2\2\2rx\5\32\16\2st\7\6\2\2tu\5\22\n\2"+
		"uv\7\7\2\2vx\3\2\2\2wr\3\2\2\2ws\3\2\2\2x\31\3\2\2\2y|\5\34\17\2z|\5&"+
		"\24\2{y\3\2\2\2{z\3\2\2\2|\33\3\2\2\2}\u0082\5\36\20\2~\u0082\5 \21\2"+
		"\177\u0082\5\"\22\2\u0080\u0082\5$\23\2\u0081}\3\2\2\2\u0081~\3\2\2\2"+
		"\u0081\177\3\2\2\2\u0081\u0080\3\2\2\2\u0082\35\3\2\2\2\u0083\u0084\5"+
		"(\25\2\u0084\u0085\t\2\2\2\u0085\u0086\7\21\2\2\u0086\37\3\2\2\2\u0087"+
		"\u0088\5(\25\2\u0088\u0089\t\2\2\2\u0089\u008a\7\20\2\2\u008a!\3\2\2\2"+
		"\u008b\u008c\5(\25\2\u008c\u008d\t\3\2\2\u008d\u008e\7\r\2\2\u008e#\3"+
		"\2\2\2\u008f\u0090\5(\25\2\u0090\u0091\t\3\2\2\u0091\u0092\7\16\2\2\u0092"+
		"%\3\2\2\2\u0093\u0096\7\17\2\2\u0094\u0096\5(\25\2\u0095\u0093\3\2\2\2"+
		"\u0095\u0094\3\2\2\2\u0096\'\3\2\2\2\u0097\u009a\7\17\2\2\u0098\u0099"+
		"\7\b\2\2\u0099\u009b\5*\26\2\u009a\u0098\3\2\2\2\u009b\u009c\3\2\2\2\u009c"+
		"\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d)\3\2\2\2\u009e\u00a0\7\17\2\2"+
		"\u009f\u00a1\5,\27\2\u00a0\u009f\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1+\3"+
		"\2\2\2\u00a2\u00a4\7\6\2\2\u00a3\u00a5\5.\30\2\u00a4\u00a3\3\2\2\2\u00a4"+
		"\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\7\7\2\2\u00a7-\3\2\2\2"+
		"\u00a8\u00ad\5\60\31\2\u00a9\u00aa\7\t\2\2\u00aa\u00ac\5\60\31\2\u00ab"+
		"\u00a9\3\2\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad\u00ae\3\2"+
		"\2\2\u00ae/\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0\u00b5\7\20\2\2\u00b1\u00b5"+
		"\7\21\2\2\u00b2\u00b5\7\r\2\2\u00b3\u00b5\5\62\32\2\u00b4\u00b0\3\2\2"+
		"\2\u00b4\u00b1\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4\u00b3\3\2\2\2\u00b5\61"+
		"\3\2\2\2\u00b6\u00b7\7\n\2\2\u00b7\u00b8\5.\30\2\u00b8\u00b9\7\13\2\2"+
		"\u00b9\63\3\2\2\2\26\66;>ADFISbjpw{\u0081\u0095\u009c\u00a0\u00a4\u00ad"+
		"\u00b4";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}