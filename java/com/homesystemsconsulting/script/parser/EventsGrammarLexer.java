// Generated from EventsGrammar.g4 by ANTLR 4.3
package com.homesystemsconsulting.script.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class EventsGrammarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Script=1, FinalNodeId=2, BooleanLiteral=3, Unknown=4, NodeId=5, NumberLiteral=6, 
		StringLiteral=7, LPAREN=8, RPAREN=9, DOT=10, ET=11, NE=12, GT=13, LT=14, 
		GE=15, LE=16, OR=17, AND=18, NOT=19, COLON=20, WS=21, COMMENT=22, LINE_COMMENT=23;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'"
	};
	public static final String[] ruleNames = {
		"Script", "FinalNodeId", "BooleanLiteral", "Unknown", "NodeId", "NodeFirstLetter", 
		"LetterOrDigit", "NumberLiteral", "StringLiteral", "DoubleQuotesStringCharacters", 
		"SingleQuotesStringCharacters", "DoubleQuotesStringCharacter", "SingleQuotesStringCharacter", 
		"EscapeSequence", "OctalEscape", "UnicodeEscape", "HexDigit", "OctalDigit", 
		"ZeroToThree", "LPAREN", "RPAREN", "DOT", "ET", "NE", "GT", "LT", "GE", 
		"LE", "OR", "AND", "NOT", "COLON", "WS", "COMMENT", "LINE_COMMENT"
	};


	public EventsGrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "EventsGrammar.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\31\u010a\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\3\2\3\2\3\2\3\2\3\2\3\2\7\2P\n\2\f\2\16\2"+
		"S\13\2\3\2\3\2\3\3\3\3\3\3\3\3\6\3[\n\3\r\3\16\3\\\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\5\4h\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\7\6"+
		"t\n\6\f\6\16\6w\13\6\3\7\3\7\3\b\3\b\3\t\5\t~\n\t\3\t\6\t\u0081\n\t\r"+
		"\t\16\t\u0082\3\t\3\t\6\t\u0087\n\t\r\t\16\t\u0088\5\t\u008b\n\t\3\n\3"+
		"\n\5\n\u008f\n\n\3\n\3\n\3\n\5\n\u0094\n\n\3\n\5\n\u0097\n\n\3\13\6\13"+
		"\u009a\n\13\r\13\16\13\u009b\3\f\6\f\u009f\n\f\r\f\16\f\u00a0\3\r\3\r"+
		"\5\r\u00a5\n\r\3\16\3\16\5\16\u00a9\n\16\3\17\3\17\3\17\3\17\5\17\u00af"+
		"\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00bc"+
		"\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24"+
		"\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32"+
		"\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3 \3 \3!\3!\3\"\6\"\u00ec\n\"\r\"\16\"\u00ed\3\"\3\"\3#\3#\3#\3#\7#\u00f6"+
		"\n#\f#\16#\u00f9\13#\3#\3#\3#\3#\3#\3$\3$\3$\3$\7$\u0104\n$\f$\16$\u0107"+
		"\13$\3$\3$\4Q\u00f7\2%\3\3\5\4\7\5\t\6\13\7\r\2\17\2\21\b\23\t\25\2\27"+
		"\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\n+\13-\f/\r\61\16\63\17\65\20\67"+
		"\219\22;\23=\24?\25A\26C\27E\30G\31\3\2\17\5\2))}}\177\177\4\2C\\c|\7"+
		"\2//\62;C\\aac|\4\2--//\3\2\62;\4\2$$^^\4\2))^^\n\2$$))^^ddhhppttvv\5"+
		"\2\62;CHch\3\2\629\3\2\62\65\5\2\13\f\16\17\"\"\4\2\f\f\17\17\u0117\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"+
		"\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2"+
		"\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\3I\3\2\2\2"+
		"\5V\3\2\2\2\7g\3\2\2\2\ti\3\2\2\2\13q\3\2\2\2\rx\3\2\2\2\17z\3\2\2\2\21"+
		"}\3\2\2\2\23\u0096\3\2\2\2\25\u0099\3\2\2\2\27\u009e\3\2\2\2\31\u00a4"+
		"\3\2\2\2\33\u00a8\3\2\2\2\35\u00ae\3\2\2\2\37\u00bb\3\2\2\2!\u00bd\3\2"+
		"\2\2#\u00c4\3\2\2\2%\u00c6\3\2\2\2\'\u00c8\3\2\2\2)\u00ca\3\2\2\2+\u00cc"+
		"\3\2\2\2-\u00ce\3\2\2\2/\u00d0\3\2\2\2\61\u00d3\3\2\2\2\63\u00d6\3\2\2"+
		"\2\65\u00d8\3\2\2\2\67\u00da\3\2\2\29\u00dd\3\2\2\2;\u00e0\3\2\2\2=\u00e3"+
		"\3\2\2\2?\u00e6\3\2\2\2A\u00e8\3\2\2\2C\u00eb\3\2\2\2E\u00f1\3\2\2\2G"+
		"\u00ff\3\2\2\2IQ\7}\2\2JP\5\23\n\2KP\5E#\2LP\5G$\2MP\n\2\2\2NP\5\3\2\2"+
		"OJ\3\2\2\2OK\3\2\2\2OL\3\2\2\2OM\3\2\2\2ON\3\2\2\2PS\3\2\2\2QR\3\2\2\2"+
		"QO\3\2\2\2RT\3\2\2\2SQ\3\2\2\2TU\7\177\2\2U\4\3\2\2\2VZ\5\13\6\2WX\5-"+
		"\27\2XY\5\13\6\2Y[\3\2\2\2ZW\3\2\2\2[\\\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2"+
		"]\6\3\2\2\2^_\7v\2\2_`\7t\2\2`a\7w\2\2ah\7g\2\2bc\7h\2\2cd\7c\2\2de\7"+
		"n\2\2ef\7u\2\2fh\7g\2\2g^\3\2\2\2gb\3\2\2\2h\b\3\2\2\2ij\7w\2\2jk\7p\2"+
		"\2kl\7m\2\2lm\7p\2\2mn\7q\2\2no\7y\2\2op\7p\2\2p\n\3\2\2\2qu\5\r\7\2r"+
		"t\5\17\b\2sr\3\2\2\2tw\3\2\2\2us\3\2\2\2uv\3\2\2\2v\f\3\2\2\2wu\3\2\2"+
		"\2xy\t\3\2\2y\16\3\2\2\2z{\t\4\2\2{\20\3\2\2\2|~\t\5\2\2}|\3\2\2\2}~\3"+
		"\2\2\2~\u0080\3\2\2\2\177\u0081\t\6\2\2\u0080\177\3\2\2\2\u0081\u0082"+
		"\3\2\2\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u008a\3\2\2\2\u0084"+
		"\u0086\7\60\2\2\u0085\u0087\t\6\2\2\u0086\u0085\3\2\2\2\u0087\u0088\3"+
		"\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008b\3\2\2\2\u008a"+
		"\u0084\3\2\2\2\u008a\u008b\3\2\2\2\u008b\22\3\2\2\2\u008c\u008e\7$\2\2"+
		"\u008d\u008f\5\25\13\2\u008e\u008d\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0090"+
		"\3\2\2\2\u0090\u0097\7$\2\2\u0091\u0093\7)\2\2\u0092\u0094\5\27\f\2\u0093"+
		"\u0092\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0097\7)"+
		"\2\2\u0096\u008c\3\2\2\2\u0096\u0091\3\2\2\2\u0097\24\3\2\2\2\u0098\u009a"+
		"\5\31\r\2\u0099\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u0099\3\2\2\2"+
		"\u009b\u009c\3\2\2\2\u009c\26\3\2\2\2\u009d\u009f\5\33\16\2\u009e\u009d"+
		"\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1"+
		"\30\3\2\2\2\u00a2\u00a5\n\7\2\2\u00a3\u00a5\5\35\17\2\u00a4\u00a2\3\2"+
		"\2\2\u00a4\u00a3\3\2\2\2\u00a5\32\3\2\2\2\u00a6\u00a9\n\b\2\2\u00a7\u00a9"+
		"\5\35\17\2\u00a8\u00a6\3\2\2\2\u00a8\u00a7\3\2\2\2\u00a9\34\3\2\2\2\u00aa"+
		"\u00ab\7^\2\2\u00ab\u00af\t\t\2\2\u00ac\u00af\5\37\20\2\u00ad\u00af\5"+
		"!\21\2\u00ae\u00aa\3\2\2\2\u00ae\u00ac\3\2\2\2\u00ae\u00ad\3\2\2\2\u00af"+
		"\36\3\2\2\2\u00b0\u00b1\7^\2\2\u00b1\u00bc\5%\23\2\u00b2\u00b3\7^\2\2"+
		"\u00b3\u00b4\5%\23\2\u00b4\u00b5\5%\23\2\u00b5\u00bc\3\2\2\2\u00b6\u00b7"+
		"\7^\2\2\u00b7\u00b8\5\'\24\2\u00b8\u00b9\5%\23\2\u00b9\u00ba\5%\23\2\u00ba"+
		"\u00bc\3\2\2\2\u00bb\u00b0\3\2\2\2\u00bb\u00b2\3\2\2\2\u00bb\u00b6\3\2"+
		"\2\2\u00bc \3\2\2\2\u00bd\u00be\7^\2\2\u00be\u00bf\7w\2\2\u00bf\u00c0"+
		"\5#\22\2\u00c0\u00c1\5#\22\2\u00c1\u00c2\5#\22\2\u00c2\u00c3\5#\22\2\u00c3"+
		"\"\3\2\2\2\u00c4\u00c5\t\n\2\2\u00c5$\3\2\2\2\u00c6\u00c7\t\13\2\2\u00c7"+
		"&\3\2\2\2\u00c8\u00c9\t\f\2\2\u00c9(\3\2\2\2\u00ca\u00cb\7*\2\2\u00cb"+
		"*\3\2\2\2\u00cc\u00cd\7+\2\2\u00cd,\3\2\2\2\u00ce\u00cf\7\60\2\2\u00cf"+
		".\3\2\2\2\u00d0\u00d1\7?\2\2\u00d1\u00d2\7?\2\2\u00d2\60\3\2\2\2\u00d3"+
		"\u00d4\7#\2\2\u00d4\u00d5\7?\2\2\u00d5\62\3\2\2\2\u00d6\u00d7\7@\2\2\u00d7"+
		"\64\3\2\2\2\u00d8\u00d9\7>\2\2\u00d9\66\3\2\2\2\u00da\u00db\7@\2\2\u00db"+
		"\u00dc\7?\2\2\u00dc8\3\2\2\2\u00dd\u00de\7>\2\2\u00de\u00df\7?\2\2\u00df"+
		":\3\2\2\2\u00e0\u00e1\7~\2\2\u00e1\u00e2\7~\2\2\u00e2<\3\2\2\2\u00e3\u00e4"+
		"\7(\2\2\u00e4\u00e5\7(\2\2\u00e5>\3\2\2\2\u00e6\u00e7\7#\2\2\u00e7@\3"+
		"\2\2\2\u00e8\u00e9\7<\2\2\u00e9B\3\2\2\2\u00ea\u00ec\t\r\2\2\u00eb\u00ea"+
		"\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee"+
		"\u00ef\3\2\2\2\u00ef\u00f0\b\"\2\2\u00f0D\3\2\2\2\u00f1\u00f2\7\61\2\2"+
		"\u00f2\u00f3\7,\2\2\u00f3\u00f7\3\2\2\2\u00f4\u00f6\13\2\2\2\u00f5\u00f4"+
		"\3\2\2\2\u00f6\u00f9\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f8"+
		"\u00fa\3\2\2\2\u00f9\u00f7\3\2\2\2\u00fa\u00fb\7,\2\2\u00fb\u00fc\7\61"+
		"\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00fe\b#\2\2\u00feF\3\2\2\2\u00ff\u0100"+
		"\7\61\2\2\u0100\u0101\7\61\2\2\u0101\u0105\3\2\2\2\u0102\u0104\n\16\2"+
		"\2\u0103\u0102\3\2\2\2\u0104\u0107\3\2\2\2\u0105\u0103\3\2\2\2\u0105\u0106"+
		"\3\2\2\2\u0106\u0108\3\2\2\2\u0107\u0105\3\2\2\2\u0108\u0109\b$\2\2\u0109"+
		"H\3\2\2\2\30\2OQ\\gu}\u0082\u0088\u008a\u008e\u0093\u0096\u009b\u00a0"+
		"\u00a4\u00a8\u00ae\u00bb\u00ed\u00f7\u0105\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}