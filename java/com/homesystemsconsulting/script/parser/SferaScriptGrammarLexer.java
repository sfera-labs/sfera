// Generated from SferaScriptGrammar.g4 by ANTLR 4.3
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
public class SferaScriptGrammarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, Script=3, FinalNodeId=4, BooleanLiteral=5, Unknown=6, 
		NodeId=7, NumberLiteral=8, StringLiteral=9, LPAREN=10, RPAREN=11, DOT=12, 
		ET=13, NE=14, GT=15, LT=16, GE=17, LE=18, OR=19, AND=20, NOT=21, COLON=22, 
		WS=23, COMMENT=24, LINE_COMMENT=25;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'"
	};
	public static final String[] ruleNames = {
		"T__1", "T__0", "Script", "FinalNodeId", "BooleanLiteral", "Unknown", 
		"NodeId", "NodeFirstLetter", "LetterOrDigit", "NumberLiteral", "StringLiteral", 
		"DoubleQuotesStringCharacters", "SingleQuotesStringCharacters", "DoubleQuotesStringCharacter", 
		"SingleQuotesStringCharacter", "EscapeSequence", "OctalEscape", "UnicodeEscape", 
		"HexDigit", "OctalDigit", "ZeroToThree", "LPAREN", "RPAREN", "DOT", "ET", 
		"NE", "GT", "LT", "GE", "LE", "OR", "AND", "NOT", "COLON", "WS", "COMMENT", 
		"LINE_COMMENT"
	};


	public SferaScriptGrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SferaScriptGrammar.g4"; }

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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\33\u0125\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4a\n\4\f\4\16\4d\13\4"+
		"\3\4\3\4\3\5\3\5\3\5\3\5\6\5l\n\5\r\5\16\5m\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\5\6y\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\7\b\u0085"+
		"\n\b\f\b\16\b\u0088\13\b\3\b\3\b\6\b\u008c\n\b\r\b\16\b\u008d\3\b\3\b"+
		"\5\b\u0092\n\b\3\t\3\t\3\n\3\n\3\13\5\13\u0099\n\13\3\13\6\13\u009c\n"+
		"\13\r\13\16\13\u009d\3\13\3\13\6\13\u00a2\n\13\r\13\16\13\u00a3\5\13\u00a6"+
		"\n\13\3\f\3\f\5\f\u00aa\n\f\3\f\3\f\3\f\5\f\u00af\n\f\3\f\5\f\u00b2\n"+
		"\f\3\r\6\r\u00b5\n\r\r\r\16\r\u00b6\3\16\6\16\u00ba\n\16\r\16\16\16\u00bb"+
		"\3\17\3\17\5\17\u00c0\n\17\3\20\3\20\5\20\u00c4\n\20\3\21\3\21\3\21\3"+
		"\21\5\21\u00ca\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\5\22\u00d7\n\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\25"+
		"\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\33\3\33"+
		"\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3"+
		"!\3!\3\"\3\"\3#\3#\3$\6$\u0107\n$\r$\16$\u0108\3$\3$\3%\3%\3%\3%\7%\u0111"+
		"\n%\f%\16%\u0114\13%\3%\3%\3%\3%\3%\3&\3&\3&\3&\7&\u011f\n&\f&\16&\u0122"+
		"\13&\3&\3&\4b\u0112\2\'\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\2\23\2\25\n\27"+
		"\13\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\f/\r\61\16\63\17\65\20\67"+
		"\219\22;\23=\24?\25A\26C\27E\30G\31I\32K\33\3\2\17\5\2))}}\177\177\3\2"+
		"\62;\4\2C\\c|\7\2//\62;C\\aac|\4\2--//\4\2$$^^\4\2))^^\n\2$$))^^ddhhp"+
		"pttvv\5\2\62;CHch\3\2\629\3\2\62\65\5\2\13\f\16\17\"\"\4\2\f\f\17\17\u0134"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2"+
		"\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I"+
		"\3\2\2\2\2K\3\2\2\2\3M\3\2\2\2\5S\3\2\2\2\7Z\3\2\2\2\tg\3\2\2\2\13x\3"+
		"\2\2\2\rz\3\2\2\2\17\u0082\3\2\2\2\21\u0093\3\2\2\2\23\u0095\3\2\2\2\25"+
		"\u0098\3\2\2\2\27\u00b1\3\2\2\2\31\u00b4\3\2\2\2\33\u00b9\3\2\2\2\35\u00bf"+
		"\3\2\2\2\37\u00c3\3\2\2\2!\u00c9\3\2\2\2#\u00d6\3\2\2\2%\u00d8\3\2\2\2"+
		"\'\u00df\3\2\2\2)\u00e1\3\2\2\2+\u00e3\3\2\2\2-\u00e5\3\2\2\2/\u00e7\3"+
		"\2\2\2\61\u00e9\3\2\2\2\63\u00eb\3\2\2\2\65\u00ee\3\2\2\2\67\u00f1\3\2"+
		"\2\29\u00f3\3\2\2\2;\u00f5\3\2\2\2=\u00f8\3\2\2\2?\u00fb\3\2\2\2A\u00fe"+
		"\3\2\2\2C\u0101\3\2\2\2E\u0103\3\2\2\2G\u0106\3\2\2\2I\u010c\3\2\2\2K"+
		"\u011a\3\2\2\2MN\7n\2\2NO\7q\2\2OP\7e\2\2PQ\7c\2\2QR\7n\2\2R\4\3\2\2\2"+
		"ST\7i\2\2TU\7n\2\2UV\7q\2\2VW\7d\2\2WX\7c\2\2XY\7n\2\2Y\6\3\2\2\2Zb\7"+
		"}\2\2[a\5\27\f\2\\a\5I%\2]a\5K&\2^a\n\2\2\2_a\5\7\4\2`[\3\2\2\2`\\\3\2"+
		"\2\2`]\3\2\2\2`^\3\2\2\2`_\3\2\2\2ad\3\2\2\2bc\3\2\2\2b`\3\2\2\2ce\3\2"+
		"\2\2db\3\2\2\2ef\7\177\2\2f\b\3\2\2\2gk\5\17\b\2hi\5\61\31\2ij\5\17\b"+
		"\2jl\3\2\2\2kh\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2\2n\n\3\2\2\2op\7v\2"+
		"\2pq\7t\2\2qr\7w\2\2ry\7g\2\2st\7h\2\2tu\7c\2\2uv\7n\2\2vw\7u\2\2wy\7"+
		"g\2\2xo\3\2\2\2xs\3\2\2\2y\f\3\2\2\2z{\7w\2\2{|\7p\2\2|}\7m\2\2}~\7p\2"+
		"\2~\177\7q\2\2\177\u0080\7y\2\2\u0080\u0081\7p\2\2\u0081\16\3\2\2\2\u0082"+
		"\u0086\5\21\t\2\u0083\u0085\5\23\n\2\u0084\u0083\3\2\2\2\u0085\u0088\3"+
		"\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0091\3\2\2\2\u0088"+
		"\u0086\3\2\2\2\u0089\u008b\5-\27\2\u008a\u008c\t\3\2\2\u008b\u008a\3\2"+
		"\2\2\u008c\u008d\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e"+
		"\u008f\3\2\2\2\u008f\u0090\5/\30\2\u0090\u0092\3\2\2\2\u0091\u0089\3\2"+
		"\2\2\u0091\u0092\3\2\2\2\u0092\20\3\2\2\2\u0093\u0094\t\4\2\2\u0094\22"+
		"\3\2\2\2\u0095\u0096\t\5\2\2\u0096\24\3\2\2\2\u0097\u0099\t\6\2\2\u0098"+
		"\u0097\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009b\3\2\2\2\u009a\u009c\t\3"+
		"\2\2\u009b\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d"+
		"\u009e\3\2\2\2\u009e\u00a5\3\2\2\2\u009f\u00a1\7\60\2\2\u00a0\u00a2\t"+
		"\3\2\2\u00a1\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3"+
		"\u00a4\3\2\2\2\u00a4\u00a6\3\2\2\2\u00a5\u009f\3\2\2\2\u00a5\u00a6\3\2"+
		"\2\2\u00a6\26\3\2\2\2\u00a7\u00a9\7$\2\2\u00a8\u00aa\5\31\r\2\u00a9\u00a8"+
		"\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00b2\7$\2\2\u00ac"+
		"\u00ae\7)\2\2\u00ad\u00af\5\33\16\2\u00ae\u00ad\3\2\2\2\u00ae\u00af\3"+
		"\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\7)\2\2\u00b1\u00a7\3\2\2\2\u00b1"+
		"\u00ac\3\2\2\2\u00b2\30\3\2\2\2\u00b3\u00b5\5\35\17\2\u00b4\u00b3\3\2"+
		"\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7"+
		"\32\3\2\2\2\u00b8\u00ba\5\37\20\2\u00b9\u00b8\3\2\2\2\u00ba\u00bb\3\2"+
		"\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\34\3\2\2\2\u00bd\u00c0"+
		"\n\7\2\2\u00be\u00c0\5!\21\2\u00bf\u00bd\3\2\2\2\u00bf\u00be\3\2\2\2\u00c0"+
		"\36\3\2\2\2\u00c1\u00c4\n\b\2\2\u00c2\u00c4\5!\21\2\u00c3\u00c1\3\2\2"+
		"\2\u00c3\u00c2\3\2\2\2\u00c4 \3\2\2\2\u00c5\u00c6\7^\2\2\u00c6\u00ca\t"+
		"\t\2\2\u00c7\u00ca\5#\22\2\u00c8\u00ca\5%\23\2\u00c9\u00c5\3\2\2\2\u00c9"+
		"\u00c7\3\2\2\2\u00c9\u00c8\3\2\2\2\u00ca\"\3\2\2\2\u00cb\u00cc\7^\2\2"+
		"\u00cc\u00d7\5)\25\2\u00cd\u00ce\7^\2\2\u00ce\u00cf\5)\25\2\u00cf\u00d0"+
		"\5)\25\2\u00d0\u00d7\3\2\2\2\u00d1\u00d2\7^\2\2\u00d2\u00d3\5+\26\2\u00d3"+
		"\u00d4\5)\25\2\u00d4\u00d5\5)\25\2\u00d5\u00d7\3\2\2\2\u00d6\u00cb\3\2"+
		"\2\2\u00d6\u00cd\3\2\2\2\u00d6\u00d1\3\2\2\2\u00d7$\3\2\2\2\u00d8\u00d9"+
		"\7^\2\2\u00d9\u00da\7w\2\2\u00da\u00db\5\'\24\2\u00db\u00dc\5\'\24\2\u00dc"+
		"\u00dd\5\'\24\2\u00dd\u00de\5\'\24\2\u00de&\3\2\2\2\u00df\u00e0\t\n\2"+
		"\2\u00e0(\3\2\2\2\u00e1\u00e2\t\13\2\2\u00e2*\3\2\2\2\u00e3\u00e4\t\f"+
		"\2\2\u00e4,\3\2\2\2\u00e5\u00e6\7*\2\2\u00e6.\3\2\2\2\u00e7\u00e8\7+\2"+
		"\2\u00e8\60\3\2\2\2\u00e9\u00ea\7\60\2\2\u00ea\62\3\2\2\2\u00eb\u00ec"+
		"\7?\2\2\u00ec\u00ed\7?\2\2\u00ed\64\3\2\2\2\u00ee\u00ef\7#\2\2\u00ef\u00f0"+
		"\7?\2\2\u00f0\66\3\2\2\2\u00f1\u00f2\7@\2\2\u00f28\3\2\2\2\u00f3\u00f4"+
		"\7>\2\2\u00f4:\3\2\2\2\u00f5\u00f6\7@\2\2\u00f6\u00f7\7?\2\2\u00f7<\3"+
		"\2\2\2\u00f8\u00f9\7>\2\2\u00f9\u00fa\7?\2\2\u00fa>\3\2\2\2\u00fb\u00fc"+
		"\7~\2\2\u00fc\u00fd\7~\2\2\u00fd@\3\2\2\2\u00fe\u00ff\7(\2\2\u00ff\u0100"+
		"\7(\2\2\u0100B\3\2\2\2\u0101\u0102\7#\2\2\u0102D\3\2\2\2\u0103\u0104\7"+
		"<\2\2\u0104F\3\2\2\2\u0105\u0107\t\r\2\2\u0106\u0105\3\2\2\2\u0107\u0108"+
		"\3\2\2\2\u0108\u0106\3\2\2\2\u0108\u0109\3\2\2\2\u0109\u010a\3\2\2\2\u010a"+
		"\u010b\b$\2\2\u010bH\3\2\2\2\u010c\u010d\7\61\2\2\u010d\u010e\7,\2\2\u010e"+
		"\u0112\3\2\2\2\u010f\u0111\13\2\2\2\u0110\u010f\3\2\2\2\u0111\u0114\3"+
		"\2\2\2\u0112\u0113\3\2\2\2\u0112\u0110\3\2\2\2\u0113\u0115\3\2\2\2\u0114"+
		"\u0112\3\2\2\2\u0115\u0116\7,\2\2\u0116\u0117\7\61\2\2\u0117\u0118\3\2"+
		"\2\2\u0118\u0119\b%\2\2\u0119J\3\2\2\2\u011a\u011b\7\61\2\2\u011b\u011c"+
		"\7\61\2\2\u011c\u0120\3\2\2\2\u011d\u011f\n\16\2\2\u011e\u011d\3\2\2\2"+
		"\u011f\u0122\3\2\2\2\u0120\u011e\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u0123"+
		"\3\2\2\2\u0122\u0120\3\2\2\2\u0123\u0124\b&\2\2\u0124L\3\2\2\2\32\2`b"+
		"mx\u0086\u008d\u0091\u0098\u009d\u00a3\u00a5\u00a9\u00ae\u00b1\u00b6\u00bb"+
		"\u00bf\u00c3\u00c9\u00d6\u0108\u0112\u0120\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}