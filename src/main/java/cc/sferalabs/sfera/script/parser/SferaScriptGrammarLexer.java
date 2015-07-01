// Generated from SferaScriptGrammar.g4 by ANTLR 4.5
package cc.sferalabs.sfera.script.parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class SferaScriptGrammarLexer extends Lexer {
	static {
		RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5,
			T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, Script = 10,
			BooleanLiteral = 11, Unknown = 12, NodeId = 13, NumberLiteral = 14,
			StringLiteral = 15, ET = 16, NE = 17, GT = 18, LT = 19, GE = 20,
			LE = 21, OR = 22, AND = 23, NOT = 24, WS = 25, COMMENT = 26,
			LINE_COMMENT = 27;
	public static String[] modeNames = { "DEFAULT_MODE" };

	public static final String[] ruleNames = { "T__0", "T__1", "T__2", "T__3",
			"T__4", "T__5", "T__6", "T__7", "T__8", "Script", "BooleanLiteral",
			"Unknown", "NodeId", "NodeFirstLetter", "LetterOrDigit",
			"NumberLiteral", "StringLiteral", "DoubleQuotesStringCharacters",
			"SingleQuotesStringCharacters", "DoubleQuotesStringCharacter",
			"SingleQuotesStringCharacter", "EscapeSequence", "OctalEscape",
			"UnicodeEscape", "HexDigit", "OctalDigit", "ZeroToThree", "ET",
			"NE", "GT", "LT", "GE", "LE", "OR", "AND", "NOT", "WS", "COMMENT",
			"LINE_COMMENT" };

	private static final String[] _LITERAL_NAMES = { null, "'local'",
			"'global'", "':'", "'('", "')'", "'.'", "','", "'['", "']'", null,
			null, "'unknown'", null, null, null, "'=='", "'!='", "'>'", "'<'",
			"'>='", "'<='", "'||'", "'&&'", "'!'" };
	private static final String[] _SYMBOLIC_NAMES = { null, null, null, null,
			null, null, null, null, null, null, "Script", "BooleanLiteral",
			"Unknown", "NodeId", "NumberLiteral", "StringLiteral", "ET", "NE",
			"GT", "LT", "GE", "LE", "OR", "AND", "NOT", "WS", "COMMENT",
			"LINE_COMMENT" };
	public static final Vocabulary VOCABULARY = new VocabularyImpl(
			_LITERAL_NAMES, _SYMBOLIC_NAMES);

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

	public SferaScriptGrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this, _ATN, _decisionToDFA,
				_sharedContextCache);
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
	public String[] getModeNames() {
		return modeNames;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\35\u011d\b\1\4\2"
			+ "\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"
			+ "\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"
			+ "\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"
			+ "\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"
			+ " \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\3\2\3\2\3\2\3\2"
			+ "\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3"
			+ "\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\7\13s\n\13\f\13\16"
			+ "\13v\13\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u0083\n\f"
			+ "\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\7\16\u008f\n\16\f\16\16\16"
			+ "\u0092\13\16\3\17\3\17\3\20\3\20\3\21\5\21\u0099\n\21\3\21\6\21\u009c"
			+ "\n\21\r\21\16\21\u009d\3\21\3\21\6\21\u00a2\n\21\r\21\16\21\u00a3\5\21"
			+ "\u00a6\n\21\3\22\3\22\5\22\u00aa\n\22\3\22\3\22\3\22\5\22\u00af\n\22\3"
			+ "\22\5\22\u00b2\n\22\3\23\6\23\u00b5\n\23\r\23\16\23\u00b6\3\24\6\24\u00ba"
			+ "\n\24\r\24\16\24\u00bb\3\25\3\25\5\25\u00c0\n\25\3\26\3\26\5\26\u00c4"
			+ "\n\26\3\27\3\27\3\27\3\27\5\27\u00ca\n\27\3\30\3\30\3\30\3\30\3\30\3\30"
			+ "\3\30\3\30\3\30\3\30\3\30\5\30\u00d7\n\30\3\31\3\31\3\31\3\31\3\31\3\31"
			+ "\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37"
			+ "\3\37\3 \3 \3!\3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3&\6&\u00ff\n"
			+ "&\r&\16&\u0100\3&\3&\3\'\3\'\3\'\3\'\7\'\u0109\n\'\f\'\16\'\u010c\13\'"
			+ "\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\7(\u0117\n(\f(\16(\u011a\13(\3(\3(\4"
			+ "t\u010a\2)\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"
			+ "\17\35\2\37\2!\20#\21%\2\'\2)\2+\2-\2/\2\61\2\63\2\65\2\67\29\22;\23="
			+ "\24?\25A\26C\27E\30G\31I\32K\33M\34O\35\3\2\17\5\2))}}\177\177\4\2C\\"
			+ "c|\7\2//\62;C\\aac|\4\2--//\3\2\62;\4\2$$^^\4\2))^^\n\2$$))^^ddhhpptt"
			+ "vv\5\2\62;CHch\3\2\629\3\2\62\65\5\2\13\f\16\17\"\"\4\2\f\f\17\17\u0129"
			+ "\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"
			+ "\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"
			+ "\2\31\3\2\2\2\2\33\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\29\3\2\2\2\2;\3\2\2\2"
			+ "\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I"
			+ "\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\3Q\3\2\2\2\5W\3\2\2\2\7^\3\2"
			+ "\2\2\t`\3\2\2\2\13b\3\2\2\2\rd\3\2\2\2\17f\3\2\2\2\21h\3\2\2\2\23j\3\2"
			+ "\2\2\25l\3\2\2\2\27\u0082\3\2\2\2\31\u0084\3\2\2\2\33\u008c\3\2\2\2\35"
			+ "\u0093\3\2\2\2\37\u0095\3\2\2\2!\u0098\3\2\2\2#\u00b1\3\2\2\2%\u00b4\3"
			+ "\2\2\2\'\u00b9\3\2\2\2)\u00bf\3\2\2\2+\u00c3\3\2\2\2-\u00c9\3\2\2\2/\u00d6"
			+ "\3\2\2\2\61\u00d8\3\2\2\2\63\u00df\3\2\2\2\65\u00e1\3\2\2\2\67\u00e3\3"
			+ "\2\2\29\u00e5\3\2\2\2;\u00e8\3\2\2\2=\u00eb\3\2\2\2?\u00ed\3\2\2\2A\u00ef"
			+ "\3\2\2\2C\u00f2\3\2\2\2E\u00f5\3\2\2\2G\u00f8\3\2\2\2I\u00fb\3\2\2\2K"
			+ "\u00fe\3\2\2\2M\u0104\3\2\2\2O\u0112\3\2\2\2QR\7n\2\2RS\7q\2\2ST\7e\2"
			+ "\2TU\7c\2\2UV\7n\2\2V\4\3\2\2\2WX\7i\2\2XY\7n\2\2YZ\7q\2\2Z[\7d\2\2[\\"
			+ "\7c\2\2\\]\7n\2\2]\6\3\2\2\2^_\7<\2\2_\b\3\2\2\2`a\7*\2\2a\n\3\2\2\2b"
			+ "c\7+\2\2c\f\3\2\2\2de\7\60\2\2e\16\3\2\2\2fg\7.\2\2g\20\3\2\2\2hi\7]\2"
			+ "\2i\22\3\2\2\2jk\7_\2\2k\24\3\2\2\2lt\7}\2\2ms\5#\22\2ns\5M\'\2os\5O("
			+ "\2ps\n\2\2\2qs\5\25\13\2rm\3\2\2\2rn\3\2\2\2ro\3\2\2\2rp\3\2\2\2rq\3\2"
			+ "\2\2sv\3\2\2\2tu\3\2\2\2tr\3\2\2\2uw\3\2\2\2vt\3\2\2\2wx\7\177\2\2x\26"
			+ "\3\2\2\2yz\7v\2\2z{\7t\2\2{|\7w\2\2|\u0083\7g\2\2}~\7h\2\2~\177\7c\2\2"
			+ "\177\u0080\7n\2\2\u0080\u0081\7u\2\2\u0081\u0083\7g\2\2\u0082y\3\2\2\2"
			+ "\u0082}\3\2\2\2\u0083\30\3\2\2\2\u0084\u0085\7w\2\2\u0085\u0086\7p\2\2"
			+ "\u0086\u0087\7m\2\2\u0087\u0088\7p\2\2\u0088\u0089\7q\2\2\u0089\u008a"
			+ "\7y\2\2\u008a\u008b\7p\2\2\u008b\32\3\2\2\2\u008c\u0090\5\35\17\2\u008d"
			+ "\u008f\5\37\20\2\u008e\u008d\3\2\2\2\u008f\u0092\3\2\2\2\u0090\u008e\3"
			+ "\2\2\2\u0090\u0091\3\2\2\2\u0091\34\3\2\2\2\u0092\u0090\3\2\2\2\u0093"
			+ "\u0094\t\3\2\2\u0094\36\3\2\2\2\u0095\u0096\t\4\2\2\u0096 \3\2\2\2\u0097"
			+ "\u0099\t\5\2\2\u0098\u0097\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009b\3\2"
			+ "\2\2\u009a\u009c\t\6\2\2\u009b\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d"
			+ "\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u00a5\3\2\2\2\u009f\u00a1\7\60"
			+ "\2\2\u00a0\u00a2\t\6\2\2\u00a1\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3"
			+ "\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a6\3\2\2\2\u00a5\u009f\3\2"
			+ "\2\2\u00a5\u00a6\3\2\2\2\u00a6\"\3\2\2\2\u00a7\u00a9\7$\2\2\u00a8\u00aa"
			+ "\5%\23\2\u00a9\u00a8\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab"
			+ "\u00b2\7$\2\2\u00ac\u00ae\7)\2\2\u00ad\u00af\5\'\24\2\u00ae\u00ad\3\2"
			+ "\2\2\u00ae\u00af\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\7)\2\2\u00b1"
			+ "\u00a7\3\2\2\2\u00b1\u00ac\3\2\2\2\u00b2$\3\2\2\2\u00b3\u00b5\5)\25\2"
			+ "\u00b4\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7"
			+ "\3\2\2\2\u00b7&\3\2\2\2\u00b8\u00ba\5+\26\2\u00b9\u00b8\3\2\2\2\u00ba"
			+ "\u00bb\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc(\3\2\2\2"
			+ "\u00bd\u00c0\n\7\2\2\u00be\u00c0\5-\27\2\u00bf\u00bd\3\2\2\2\u00bf\u00be"
			+ "\3\2\2\2\u00c0*\3\2\2\2\u00c1\u00c4\n\b\2\2\u00c2\u00c4\5-\27\2\u00c3"
			+ "\u00c1\3\2\2\2\u00c3\u00c2\3\2\2\2\u00c4,\3\2\2\2\u00c5\u00c6\7^\2\2\u00c6"
			+ "\u00ca\t\t\2\2\u00c7\u00ca\5/\30\2\u00c8\u00ca\5\61\31\2\u00c9\u00c5\3"
			+ "\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00c8\3\2\2\2\u00ca.\3\2\2\2\u00cb\u00cc"
			+ "\7^\2\2\u00cc\u00d7\5\65\33\2\u00cd\u00ce\7^\2\2\u00ce\u00cf\5\65\33\2"
			+ "\u00cf\u00d0\5\65\33\2\u00d0\u00d7\3\2\2\2\u00d1\u00d2\7^\2\2\u00d2\u00d3"
			+ "\5\67\34\2\u00d3\u00d4\5\65\33\2\u00d4\u00d5\5\65\33\2\u00d5\u00d7\3\2"
			+ "\2\2\u00d6\u00cb\3\2\2\2\u00d6\u00cd\3\2\2\2\u00d6\u00d1\3\2\2\2\u00d7"
			+ "\60\3\2\2\2\u00d8\u00d9\7^\2\2\u00d9\u00da\7w\2\2\u00da\u00db\5\63\32"
			+ "\2\u00db\u00dc\5\63\32\2\u00dc\u00dd\5\63\32\2\u00dd\u00de\5\63\32\2\u00de"
			+ "\62\3\2\2\2\u00df\u00e0\t\n\2\2\u00e0\64\3\2\2\2\u00e1\u00e2\t\13\2\2"
			+ "\u00e2\66\3\2\2\2\u00e3\u00e4\t\f\2\2\u00e48\3\2\2\2\u00e5\u00e6\7?\2"
			+ "\2\u00e6\u00e7\7?\2\2\u00e7:\3\2\2\2\u00e8\u00e9\7#\2\2\u00e9\u00ea\7"
			+ "?\2\2\u00ea<\3\2\2\2\u00eb\u00ec\7@\2\2\u00ec>\3\2\2\2\u00ed\u00ee\7>"
			+ "\2\2\u00ee@\3\2\2\2\u00ef\u00f0\7@\2\2\u00f0\u00f1\7?\2\2\u00f1B\3\2\2"
			+ "\2\u00f2\u00f3\7>\2\2\u00f3\u00f4\7?\2\2\u00f4D\3\2\2\2\u00f5\u00f6\7"
			+ "~\2\2\u00f6\u00f7\7~\2\2\u00f7F\3\2\2\2\u00f8\u00f9\7(\2\2\u00f9\u00fa"
			+ "\7(\2\2\u00faH\3\2\2\2\u00fb\u00fc\7#\2\2\u00fcJ\3\2\2\2\u00fd\u00ff\t"
			+ "\r\2\2\u00fe\u00fd\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\u00fe\3\2\2\2\u0100"
			+ "\u0101\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0103\b&\2\2\u0103L\3\2\2\2\u0104"
			+ "\u0105\7\61\2\2\u0105\u0106\7,\2\2\u0106\u010a\3\2\2\2\u0107\u0109\13"
			+ "\2\2\2\u0108\u0107\3\2\2\2\u0109\u010c\3\2\2\2\u010a\u010b\3\2\2\2\u010a"
			+ "\u0108\3\2\2\2\u010b\u010d\3\2\2\2\u010c\u010a\3\2\2\2\u010d\u010e\7,"
			+ "\2\2\u010e\u010f\7\61\2\2\u010f\u0110\3\2\2\2\u0110\u0111\b\'\2\2\u0111"
			+ "N\3\2\2\2\u0112\u0113\7\61\2\2\u0113\u0114\7\61\2\2\u0114\u0118\3\2\2"
			+ "\2\u0115\u0117\n\16\2\2\u0116\u0115\3\2\2\2\u0117\u011a\3\2\2\2\u0118"
			+ "\u0116\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011b\3\2\2\2\u011a\u0118\3\2"
			+ "\2\2\u011b\u011c\b(\2\2\u011cP\3\2\2\2\27\2rt\u0082\u0090\u0098\u009d"
			+ "\u00a3\u00a5\u00a9\u00ae\u00b1\u00b6\u00bb\u00bf\u00c3\u00c9\u00d6\u0100"
			+ "\u010a\u0118\3\b\2\2";
	public static final ATN _ATN = new ATNDeserializer()
			.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}