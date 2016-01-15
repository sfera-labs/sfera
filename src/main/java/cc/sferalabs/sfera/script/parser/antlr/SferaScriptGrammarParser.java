// Generated from SferaScriptGrammar.g4 by ANTLR 4.5.1
package cc.sferalabs.sfera.script.parser.antlr;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class SferaScriptGrammarParser extends Parser {
	static {
		RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7,
			T__7 = 8, ImportLine = 9, Script = 10, BooleanLiteral = 11, Unknown = 12, NodeId = 13,
			NumberLiteral = 14, StringLiteral = 15, ET = 16, NE = 17, GT = 18, LT = 19, GE = 20,
			LE = 21, OR = 22, AND = 23, NOT = 24, WS = 25, COMMENT = 26, LINE_COMMENT = 27;
	public static final int RULE_parse = 0, RULE_imports = 1, RULE_importLine = 2, RULE_init = 3,
			RULE_rules = 4, RULE_ruleLine = 5, RULE_action = 6, RULE_trigger = 7,
			RULE_orExpression = 8, RULE_andExpression = 9, RULE_notExpression = 10,
			RULE_atomExpression = 11, RULE_event = 12, RULE_stableEvent = 13,
			RULE_stringComparison = 14, RULE_numberComparison = 15, RULE_booleanComparison = 16,
			RULE_unknownComparison = 17, RULE_transientEvent = 18, RULE_terminalNode = 19,
			RULE_subNode = 20, RULE_parameters = 21, RULE_paramsList = 22, RULE_parameter = 23,
			RULE_array = 24;
	public static final String[] ruleNames = { "parse", "imports", "importLine", "init", "rules",
			"ruleLine", "action", "trigger", "orExpression", "andExpression", "notExpression",
			"atomExpression", "event", "stableEvent", "stringComparison", "numberComparison",
			"booleanComparison", "unknownComparison", "transientEvent", "terminalNode", "subNode",
			"parameters", "paramsList", "parameter", "array" };

	private static final String[] _LITERAL_NAMES = { null, "'init'", "':'", "'('", "')'", "'.'",
			"','", "'['", "']'", null, null, null, "'unknown'", null, null, null, "'=='", "'!='",
			"'>'", "'<'", "'>='", "'<='", "'||'", "'&&'", "'!'" };
	private static final String[] _SYMBOLIC_NAMES = { null, null, null, null, null, null, null,
			null, null, "ImportLine", "Script", "BooleanLiteral", "Unknown", "NodeId",
			"NumberLiteral", "StringLiteral", "ET", "NE", "GT", "LT", "GE", "LE", "OR", "AND",
			"NOT", "WS", "COMMENT", "LINE_COMMENT" };
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
	public String getGrammarFileName() {
		return "SferaScriptGrammar.g4";
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return _serializedATN;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public SferaScriptGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	public static class ParseContext extends ParserRuleContext {
		public TerminalNode EOF() {
			return getToken(SferaScriptGrammarParser.EOF, 0);
		}

		public ImportsContext imports() {
			return getRuleContext(ImportsContext.class, 0);
		}

		public InitContext init() {
			return getRuleContext(InitContext.class, 0);
		}

		public RulesContext rules() {
			return getRuleContext(RulesContext.class, 0);
		}

		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_parse;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterParse(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitParse(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(51);
				_la = _input.LA(1);
				if (_la == ImportLine) {
					{
						setState(50);
						imports();
					}
				}

				setState(54);
				_la = _input.LA(1);
				if (_la == T__0 || _la == Script) {
					{
						setState(53);
						init();
					}
				}

				setState(57);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0
						&& ((1L << _la) & ((1L << T__2) | (1L << NodeId) | (1L << NOT))) != 0)) {
					{
						setState(56);
						rules();
					}
				}

				setState(59);
				match(EOF);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportsContext extends ParserRuleContext {
		public List<ImportLineContext> importLine() {
			return getRuleContexts(ImportLineContext.class);
		}

		public ImportLineContext importLine(int i) {
			return getRuleContext(ImportLineContext.class, i);
		}

		public ImportsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_imports;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterImports(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitImports(this);
		}
	}

	public final ImportsContext imports() throws RecognitionException {
		ImportsContext _localctx = new ImportsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_imports);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
						{
							setState(61);
							importLine();
						}
					}
					setState(64);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while (_la == ImportLine);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportLineContext extends ParserRuleContext {
		public TerminalNode ImportLine() {
			return getToken(SferaScriptGrammarParser.ImportLine, 0);
		}

		public ImportLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_importLine;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterImportLine(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitImportLine(this);
		}
	}

	public final ImportLineContext importLine() throws RecognitionException {
		ImportLineContext _localctx = new ImportLineContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_importLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(66);
				match(ImportLine);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InitContext extends ParserRuleContext {
		public TerminalNode Script() {
			return getToken(SferaScriptGrammarParser.Script, 0);
		}

		public InitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_init;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterInit(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitInit(this);
		}
	}

	public final InitContext init() throws RecognitionException {
		InitContext _localctx = new InitContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_init);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(69);
				_la = _input.LA(1);
				if (_la == T__0) {
					{
						setState(68);
						match(T__0);
					}
				}

				setState(71);
				match(Script);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RulesContext extends ParserRuleContext {
		public List<RuleLineContext> ruleLine() {
			return getRuleContexts(RuleLineContext.class);
		}

		public RuleLineContext ruleLine(int i) {
			return getRuleContext(RuleLineContext.class, i);
		}

		public RulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_rules;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterRules(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitRules(this);
		}
	}

	public final RulesContext rules() throws RecognitionException {
		RulesContext _localctx = new RulesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_rules);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
						{
							setState(73);
							ruleLine();
						}
					}
					setState(76);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ((((_la) & ~0x3f) == 0
						&& ((1L << _la) & ((1L << T__2) | (1L << NodeId) | (1L << NOT))) != 0));
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RuleLineContext extends ParserRuleContext {
		public TriggerContext trigger() {
			return getRuleContext(TriggerContext.class, 0);
		}

		public ActionContext action() {
			return getRuleContext(ActionContext.class, 0);
		}

		public RuleLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_ruleLine;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterRuleLine(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitRuleLine(this);
		}
	}

	public final RuleLineContext ruleLine() throws RecognitionException {
		RuleLineContext _localctx = new RuleLineContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_ruleLine);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(78);
				trigger();
				setState(79);
				match(T__1);
				setState(80);
				action();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionContext extends ParserRuleContext {
		public TerminalNode Script() {
			return getToken(SferaScriptGrammarParser.Script, 0);
		}

		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_action;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterAction(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitAction(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(82);
				match(Script);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriggerContext extends ParserRuleContext {
		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class, 0);
		}

		public TriggerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_trigger;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterTrigger(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitTrigger(this);
		}
	}

	public final TriggerContext trigger() throws RecognitionException {
		TriggerContext _localctx = new TriggerContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_trigger);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(84);
				orExpression();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrExpressionContext extends ParserRuleContext {
		public List<AndExpressionContext> andExpression() {
			return getRuleContexts(AndExpressionContext.class);
		}

		public AndExpressionContext andExpression(int i) {
			return getRuleContext(AndExpressionContext.class, i);
		}

		public List<TerminalNode> OR() {
			return getTokens(SferaScriptGrammarParser.OR);
		}

		public TerminalNode OR(int i) {
			return getToken(SferaScriptGrammarParser.OR, i);
		}

		public OrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterOrExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitOrExpression(this);
		}
	}

	public final OrExpressionContext orExpression() throws RecognitionException {
		OrExpressionContext _localctx = new OrExpressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_orExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(86);
				andExpression();
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == OR) {
					{
						{
							setState(87);
							match(OR);
							setState(88);
							andExpression();
						}
					}
					setState(93);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndExpressionContext extends ParserRuleContext {
		public List<NotExpressionContext> notExpression() {
			return getRuleContexts(NotExpressionContext.class);
		}

		public NotExpressionContext notExpression(int i) {
			return getRuleContext(NotExpressionContext.class, i);
		}

		public List<TerminalNode> AND() {
			return getTokens(SferaScriptGrammarParser.AND);
		}

		public TerminalNode AND(int i) {
			return getToken(SferaScriptGrammarParser.AND, i);
		}

		public AndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_andExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterAndExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitAndExpression(this);
		}
	}

	public final AndExpressionContext andExpression() throws RecognitionException {
		AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_andExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(94);
				notExpression();
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == AND) {
					{
						{
							setState(95);
							match(AND);
							setState(96);
							notExpression();
						}
					}
					setState(101);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NotExpressionContext extends ParserRuleContext {
		public TerminalNode NOT() {
			return getToken(SferaScriptGrammarParser.NOT, 0);
		}

		public AtomExpressionContext atomExpression() {
			return getRuleContext(AtomExpressionContext.class, 0);
		}

		public NotExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_notExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterNotExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitNotExpression(this);
		}
	}

	public final NotExpressionContext notExpression() throws RecognitionException {
		NotExpressionContext _localctx = new NotExpressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_notExpression);
		try {
			setState(105);
			switch (_input.LA(1)) {
			case NOT:
				enterOuterAlt(_localctx, 1); {
				setState(102);
				match(NOT);
				setState(103);
				atomExpression();
			}
				break;
			case T__2:
			case NodeId:
				enterOuterAlt(_localctx, 2); {
				setState(104);
				atomExpression();
			}
				break;
			default:
				throw new NoViableAltException(this);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomExpressionContext extends ParserRuleContext {
		public EventContext event() {
			return getRuleContext(EventContext.class, 0);
		}

		public OrExpressionContext orExpression() {
			return getRuleContext(OrExpressionContext.class, 0);
		}

		public AtomExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_atomExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterAtomExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitAtomExpression(this);
		}
	}

	public final AtomExpressionContext atomExpression() throws RecognitionException {
		AtomExpressionContext _localctx = new AtomExpressionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_atomExpression);
		try {
			setState(112);
			switch (_input.LA(1)) {
			case NodeId:
				enterOuterAlt(_localctx, 1); {
				setState(107);
				event();
			}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2); {
				setState(108);
				match(T__2);
				setState(109);
				orExpression();
				setState(110);
				match(T__3);
			}
				break;
			default:
				throw new NoViableAltException(this);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventContext extends ParserRuleContext {
		public StableEventContext stableEvent() {
			return getRuleContext(StableEventContext.class, 0);
		}

		public TransientEventContext transientEvent() {
			return getRuleContext(TransientEventContext.class, 0);
		}

		public EventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_event;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterEvent(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitEvent(this);
		}
	}

	public final EventContext event() throws RecognitionException {
		EventContext _localctx = new EventContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_event);
		try {
			setState(116);
			switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
			case 1:
				enterOuterAlt(_localctx, 1); {
				setState(114);
				stableEvent();
			}
				break;
			case 2:
				enterOuterAlt(_localctx, 2); {
				setState(115);
				transientEvent();
			}
				break;
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StableEventContext extends ParserRuleContext {
		public StringComparisonContext stringComparison() {
			return getRuleContext(StringComparisonContext.class, 0);
		}

		public NumberComparisonContext numberComparison() {
			return getRuleContext(NumberComparisonContext.class, 0);
		}

		public BooleanComparisonContext booleanComparison() {
			return getRuleContext(BooleanComparisonContext.class, 0);
		}

		public UnknownComparisonContext unknownComparison() {
			return getRuleContext(UnknownComparisonContext.class, 0);
		}

		public StableEventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_stableEvent;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterStableEvent(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitStableEvent(this);
		}
	}

	public final StableEventContext stableEvent() throws RecognitionException {
		StableEventContext _localctx = new StableEventContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_stableEvent);
		try {
			setState(122);
			switch (getInterpreter().adaptivePredict(_input, 11, _ctx)) {
			case 1:
				enterOuterAlt(_localctx, 1); {
				setState(118);
				stringComparison();
			}
				break;
			case 2:
				enterOuterAlt(_localctx, 2); {
				setState(119);
				numberComparison();
			}
				break;
			case 3:
				enterOuterAlt(_localctx, 3); {
				setState(120);
				booleanComparison();
			}
				break;
			case 4:
				enterOuterAlt(_localctx, 4); {
				setState(121);
				unknownComparison();
			}
				break;
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringComparisonContext extends ParserRuleContext {
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class, 0);
		}

		public TerminalNode StringLiteral() {
			return getToken(SferaScriptGrammarParser.StringLiteral, 0);
		}

		public TerminalNode ET() {
			return getToken(SferaScriptGrammarParser.ET, 0);
		}

		public TerminalNode NE() {
			return getToken(SferaScriptGrammarParser.NE, 0);
		}

		public TerminalNode GT() {
			return getToken(SferaScriptGrammarParser.GT, 0);
		}

		public TerminalNode LT() {
			return getToken(SferaScriptGrammarParser.LT, 0);
		}

		public TerminalNode GE() {
			return getToken(SferaScriptGrammarParser.GE, 0);
		}

		public TerminalNode LE() {
			return getToken(SferaScriptGrammarParser.LE, 0);
		}

		public StringComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_stringComparison;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterStringComparison(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitStringComparison(this);
		}
	}

	public final StringComparisonContext stringComparison() throws RecognitionException {
		StringComparisonContext _localctx = new StringComparisonContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_stringComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(124);
				terminalNode();
				setState(125);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT)
						| (1L << LT) | (1L << GE) | (1L << LE))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(126);
				match(StringLiteral);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberComparisonContext extends ParserRuleContext {
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class, 0);
		}

		public TerminalNode NumberLiteral() {
			return getToken(SferaScriptGrammarParser.NumberLiteral, 0);
		}

		public TerminalNode ET() {
			return getToken(SferaScriptGrammarParser.ET, 0);
		}

		public TerminalNode NE() {
			return getToken(SferaScriptGrammarParser.NE, 0);
		}

		public TerminalNode GT() {
			return getToken(SferaScriptGrammarParser.GT, 0);
		}

		public TerminalNode LT() {
			return getToken(SferaScriptGrammarParser.LT, 0);
		}

		public TerminalNode GE() {
			return getToken(SferaScriptGrammarParser.GE, 0);
		}

		public TerminalNode LE() {
			return getToken(SferaScriptGrammarParser.LE, 0);
		}

		public NumberComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_numberComparison;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterNumberComparison(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitNumberComparison(this);
		}
	}

	public final NumberComparisonContext numberComparison() throws RecognitionException {
		NumberComparisonContext _localctx = new NumberComparisonContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_numberComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(128);
				terminalNode();
				setState(129);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ET) | (1L << NE) | (1L << GT)
						| (1L << LT) | (1L << GE) | (1L << LE))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(130);
				match(NumberLiteral);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanComparisonContext extends ParserRuleContext {
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class, 0);
		}

		public TerminalNode BooleanLiteral() {
			return getToken(SferaScriptGrammarParser.BooleanLiteral, 0);
		}

		public TerminalNode ET() {
			return getToken(SferaScriptGrammarParser.ET, 0);
		}

		public TerminalNode NE() {
			return getToken(SferaScriptGrammarParser.NE, 0);
		}

		public BooleanComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_booleanComparison;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterBooleanComparison(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitBooleanComparison(this);
		}
	}

	public final BooleanComparisonContext booleanComparison() throws RecognitionException {
		BooleanComparisonContext _localctx = new BooleanComparisonContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_booleanComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(132);
				terminalNode();
				setState(133);
				_la = _input.LA(1);
				if (!(_la == ET || _la == NE)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(134);
				match(BooleanLiteral);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnknownComparisonContext extends ParserRuleContext {
		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class, 0);
		}

		public TerminalNode Unknown() {
			return getToken(SferaScriptGrammarParser.Unknown, 0);
		}

		public TerminalNode ET() {
			return getToken(SferaScriptGrammarParser.ET, 0);
		}

		public TerminalNode NE() {
			return getToken(SferaScriptGrammarParser.NE, 0);
		}

		public UnknownComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_unknownComparison;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterUnknownComparison(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitUnknownComparison(this);
		}
	}

	public final UnknownComparisonContext unknownComparison() throws RecognitionException {
		UnknownComparisonContext _localctx = new UnknownComparisonContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_unknownComparison);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(136);
				terminalNode();
				setState(137);
				_la = _input.LA(1);
				if (!(_la == ET || _la == NE)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(138);
				match(Unknown);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TransientEventContext extends ParserRuleContext {
		public TerminalNode NodeId() {
			return getToken(SferaScriptGrammarParser.NodeId, 0);
		}

		public TerminalNodeContext terminalNode() {
			return getRuleContext(TerminalNodeContext.class, 0);
		}

		public TransientEventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_transientEvent;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterTransientEvent(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitTransientEvent(this);
		}
	}

	public final TransientEventContext transientEvent() throws RecognitionException {
		TransientEventContext _localctx = new TransientEventContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_transientEvent);
		try {
			setState(142);
			switch (getInterpreter().adaptivePredict(_input, 12, _ctx)) {
			case 1:
				enterOuterAlt(_localctx, 1); {
				setState(140);
				match(NodeId);
			}
				break;
			case 2:
				enterOuterAlt(_localctx, 2); {
				setState(141);
				terminalNode();
			}
				break;
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TerminalNodeContext extends ParserRuleContext {
		public TerminalNode NodeId() {
			return getToken(SferaScriptGrammarParser.NodeId, 0);
		}

		public List<SubNodeContext> subNode() {
			return getRuleContexts(SubNodeContext.class);
		}

		public SubNodeContext subNode(int i) {
			return getRuleContext(SubNodeContext.class, i);
		}

		public TerminalNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_terminalNode;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterTerminalNode(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitTerminalNode(this);
		}
	}

	public final TerminalNodeContext terminalNode() throws RecognitionException {
		TerminalNodeContext _localctx = new TerminalNodeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_terminalNode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(144);
				match(NodeId);
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
						{
							setState(145);
							match(T__4);
							setState(146);
							subNode();
						}
					}
					setState(149);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while (_la == T__4);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubNodeContext extends ParserRuleContext {
		public TerminalNode NodeId() {
			return getToken(SferaScriptGrammarParser.NodeId, 0);
		}

		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class, 0);
		}

		public SubNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_subNode;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterSubNode(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitSubNode(this);
		}
	}

	public final SubNodeContext subNode() throws RecognitionException {
		SubNodeContext _localctx = new SubNodeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_subNode);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(151);
				match(NodeId);
				setState(153);
				_la = _input.LA(1);
				if (_la == T__2) {
					{
						setState(152);
						parameters();
					}
				}

			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParametersContext extends ParserRuleContext {
		public ParamsListContext paramsList() {
			return getRuleContext(ParamsListContext.class, 0);
		}

		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_parameters;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterParameters(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitParameters(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_parameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(155);
				match(T__2);
				setState(157);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << BooleanLiteral)
						| (1L << NodeId) | (1L << NumberLiteral) | (1L << StringLiteral))) != 0)) {
					{
						setState(156);
						paramsList();
					}
				}

				setState(159);
				match(T__3);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamsListContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}

		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class, i);
		}

		public ParamsListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_paramsList;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterParamsList(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitParamsList(this);
		}
	}

	public final ParamsListContext paramsList() throws RecognitionException {
		ParamsListContext _localctx = new ParamsListContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_paramsList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(161);
				parameter();
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__5) {
					{
						{
							setState(162);
							match(T__5);
							setState(163);
							parameter();
						}
					}
					setState(168);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode NumberLiteral() {
			return getToken(SferaScriptGrammarParser.NumberLiteral, 0);
		}

		public TerminalNode StringLiteral() {
			return getToken(SferaScriptGrammarParser.StringLiteral, 0);
		}

		public TerminalNode BooleanLiteral() {
			return getToken(SferaScriptGrammarParser.BooleanLiteral, 0);
		}

		public TerminalNode NodeId() {
			return getToken(SferaScriptGrammarParser.NodeId, 0);
		}

		public ArrayContext array() {
			return getRuleContext(ArrayContext.class, 0);
		}

		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_parameter;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterParameter(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitParameter(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_parameter);
		try {
			setState(174);
			switch (_input.LA(1)) {
			case NumberLiteral:
				enterOuterAlt(_localctx, 1); {
				setState(169);
				match(NumberLiteral);
			}
				break;
			case StringLiteral:
				enterOuterAlt(_localctx, 2); {
				setState(170);
				match(StringLiteral);
			}
				break;
			case BooleanLiteral:
				enterOuterAlt(_localctx, 3); {
				setState(171);
				match(BooleanLiteral);
			}
				break;
			case NodeId:
				enterOuterAlt(_localctx, 4); {
				setState(172);
				match(NodeId);
			}
				break;
			case T__6:
				enterOuterAlt(_localctx, 5); {
				setState(173);
				array();
			}
				break;
			default:
				throw new NoViableAltException(this);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayContext extends ParserRuleContext {
		public ParamsListContext paramsList() {
			return getRuleContext(ParamsListContext.class, 0);
		}

		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_array;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).enterArray(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof SferaScriptGrammarListener)
				((SferaScriptGrammarListener) listener).exitArray(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_array);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(176);
				match(T__6);
				setState(177);
				paramsList();
				setState(178);
				match(T__7);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\35\u00b7\4\2\t\2"
			+ "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
			+ "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
			+ "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
			+ "\4\32\t\32\3\2\5\2\66\n\2\3\2\5\29\n\2\3\2\5\2<\n\2\3\2\3\2\3\3\6\3A\n"
			+ "\3\r\3\16\3B\3\4\3\4\3\5\5\5H\n\5\3\5\3\5\3\6\6\6M\n\6\r\6\16\6N\3\7\3"
			+ "\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\7\n\\\n\n\f\n\16\n_\13\n\3\13\3"
			+ "\13\3\13\7\13d\n\13\f\13\16\13g\13\13\3\f\3\f\3\f\5\fl\n\f\3\r\3\r\3\r"
			+ "\3\r\3\r\5\rs\n\r\3\16\3\16\5\16w\n\16\3\17\3\17\3\17\3\17\5\17}\n\17"
			+ "\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23"
			+ "\3\23\3\23\3\24\3\24\5\24\u0091\n\24\3\25\3\25\3\25\6\25\u0096\n\25\r"
			+ "\25\16\25\u0097\3\26\3\26\5\26\u009c\n\26\3\27\3\27\5\27\u00a0\n\27\3"
			+ "\27\3\27\3\30\3\30\3\30\7\30\u00a7\n\30\f\30\16\30\u00aa\13\30\3\31\3"
			+ "\31\3\31\3\31\3\31\5\31\u00b1\n\31\3\32\3\32\3\32\3\32\3\32\2\2\33\2\4"
			+ "\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\2\4\3\2\22\27\3\2\22"
			+ "\23\u00b4\2\65\3\2\2\2\4@\3\2\2\2\6D\3\2\2\2\bG\3\2\2\2\nL\3\2\2\2\fP"
			+ "\3\2\2\2\16T\3\2\2\2\20V\3\2\2\2\22X\3\2\2\2\24`\3\2\2\2\26k\3\2\2\2\30"
			+ "r\3\2\2\2\32v\3\2\2\2\34|\3\2\2\2\36~\3\2\2\2 \u0082\3\2\2\2\"\u0086\3"
			+ "\2\2\2$\u008a\3\2\2\2&\u0090\3\2\2\2(\u0092\3\2\2\2*\u0099\3\2\2\2,\u009d"
			+ "\3\2\2\2.\u00a3\3\2\2\2\60\u00b0\3\2\2\2\62\u00b2\3\2\2\2\64\66\5\4\3"
			+ "\2\65\64\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\679\5\b\5\28\67\3\2\2\289\3"
			+ "\2\2\29;\3\2\2\2:<\5\n\6\2;:\3\2\2\2;<\3\2\2\2<=\3\2\2\2=>\7\2\2\3>\3"
			+ "\3\2\2\2?A\5\6\4\2@?\3\2\2\2AB\3\2\2\2B@\3\2\2\2BC\3\2\2\2C\5\3\2\2\2"
			+ "DE\7\13\2\2E\7\3\2\2\2FH\7\3\2\2GF\3\2\2\2GH\3\2\2\2HI\3\2\2\2IJ\7\f\2"
			+ "\2J\t\3\2\2\2KM\5\f\7\2LK\3\2\2\2MN\3\2\2\2NL\3\2\2\2NO\3\2\2\2O\13\3"
			+ "\2\2\2PQ\5\20\t\2QR\7\4\2\2RS\5\16\b\2S\r\3\2\2\2TU\7\f\2\2U\17\3\2\2"
			+ "\2VW\5\22\n\2W\21\3\2\2\2X]\5\24\13\2YZ\7\30\2\2Z\\\5\24\13\2[Y\3\2\2"
			+ "\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^\23\3\2\2\2_]\3\2\2\2`e\5\26\f\2ab\7"
			+ "\31\2\2bd\5\26\f\2ca\3\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2f\25\3\2\2\2"
			+ "ge\3\2\2\2hi\7\32\2\2il\5\30\r\2jl\5\30\r\2kh\3\2\2\2kj\3\2\2\2l\27\3"
			+ "\2\2\2ms\5\32\16\2no\7\5\2\2op\5\22\n\2pq\7\6\2\2qs\3\2\2\2rm\3\2\2\2"
			+ "rn\3\2\2\2s\31\3\2\2\2tw\5\34\17\2uw\5&\24\2vt\3\2\2\2vu\3\2\2\2w\33\3"
			+ "\2\2\2x}\5\36\20\2y}\5 \21\2z}\5\"\22\2{}\5$\23\2|x\3\2\2\2|y\3\2\2\2"
			+ "|z\3\2\2\2|{\3\2\2\2}\35\3\2\2\2~\177\5(\25\2\177\u0080\t\2\2\2\u0080"
			+ "\u0081\7\21\2\2\u0081\37\3\2\2\2\u0082\u0083\5(\25\2\u0083\u0084\t\2\2"
			+ "\2\u0084\u0085\7\20\2\2\u0085!\3\2\2\2\u0086\u0087\5(\25\2\u0087\u0088"
			+ "\t\3\2\2\u0088\u0089\7\r\2\2\u0089#\3\2\2\2\u008a\u008b\5(\25\2\u008b"
			+ "\u008c\t\3\2\2\u008c\u008d\7\16\2\2\u008d%\3\2\2\2\u008e\u0091\7\17\2"
			+ "\2\u008f\u0091\5(\25\2\u0090\u008e\3\2\2\2\u0090\u008f\3\2\2\2\u0091\'"
			+ "\3\2\2\2\u0092\u0095\7\17\2\2\u0093\u0094\7\7\2\2\u0094\u0096\5*\26\2"
			+ "\u0095\u0093\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0095\3\2\2\2\u0097\u0098"
			+ "\3\2\2\2\u0098)\3\2\2\2\u0099\u009b\7\17\2\2\u009a\u009c\5,\27\2\u009b"
			+ "\u009a\3\2\2\2\u009b\u009c\3\2\2\2\u009c+\3\2\2\2\u009d\u009f\7\5\2\2"
			+ "\u009e\u00a0\5.\30\2\u009f\u009e\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a1"
			+ "\3\2\2\2\u00a1\u00a2\7\6\2\2\u00a2-\3\2\2\2\u00a3\u00a8\5\60\31\2\u00a4"
			+ "\u00a5\7\b\2\2\u00a5\u00a7\5\60\31\2\u00a6\u00a4\3\2\2\2\u00a7\u00aa\3"
			+ "\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9/\3\2\2\2\u00aa\u00a8"
			+ "\3\2\2\2\u00ab\u00b1\7\20\2\2\u00ac\u00b1\7\21\2\2\u00ad\u00b1\7\r\2\2"
			+ "\u00ae\u00b1\7\17\2\2\u00af\u00b1\5\62\32\2\u00b0\u00ab\3\2\2\2\u00b0"
			+ "\u00ac\3\2\2\2\u00b0\u00ad\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00af\3\2"
			+ "\2\2\u00b1\61\3\2\2\2\u00b2\u00b3\7\t\2\2\u00b3\u00b4\5.\30\2\u00b4\u00b5"
			+ "\7\n\2\2\u00b5\63\3\2\2\2\24\658;BGN]ekrv|\u0090\u0097\u009b\u009f\u00a8" + "\u00b0";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}