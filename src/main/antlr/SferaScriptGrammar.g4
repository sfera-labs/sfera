/*
 * 
 */

grammar SferaScriptGrammar;

parse
    : imports? init? rules? EOF
    ;
    
imports
	: importLine+
	;
	
importLine
	: ImportLine
	;
	
ImportLine
	: 'import' Path ';'
	;

fragment
Path
	: .+?
	;
	
init
	: 'init'? Script
	;

rules
    : ruleLine+
    ;

ruleLine
    : trigger ':' action
    ;
    
action
	: Script 
	;

Script
	: '{' (StringLiteral | COMMENT | LINE_COMMENT | ~['{''}'] | Script)*? '}'
	;

trigger
    : orExpression
    ;

orExpression
    : andExpression (OR andExpression)*
    ;

andExpression
    : notExpression (AND notExpression)*
    ;

notExpression
    : NOT atomExpression
    | atomExpression
    ;

atomExpression
    : event
    | '(' orExpression ')'
    ;

event
    : stableEvent
    | transientEvent
    ;

stableEvent
	: stringComparison
	| numberComparison
	| booleanComparison
	| unknownComparison
    ;
    
stringComparison
    : terminalNode (ET|NE|GT|LT|GE|LE) StringLiteral
    ;
    
numberComparison
    : terminalNode (ET|NE|GT|LT|GE|LE) NumberLiteral
    ;
    
booleanComparison
    : terminalNode (ET|NE) BooleanLiteral
    ;
    
unknownComparison
    : terminalNode (ET|NE) Unknown
    ;

transientEvent
    : NodeId
    | terminalNode
    ;

terminalNode
    : NodeId ('.' subNode)+
    ;
    
BooleanLiteral
    : 'true'
    | 'false'
    ;
    
Unknown
    : 'unknown'
    ;

subNode
	: NodeId parameters?
	;
	
parameters
	: '(' paramsList? ')'
	;
	
paramsList
	: parameter (',' parameter)*
	;
	
parameter
	: NumberLiteral
	| StringLiteral
	| BooleanLiteral
	| array
	;
	
array
	: '[' paramsList ']'
	;
    
NodeId
    : NodeFirstLetter LetterOrDigit*
    ;

fragment
NodeFirstLetter
    : [a-zA-Z]
    ;

fragment
LetterOrDigit
    : [a-zA-Z0-9_-]
    ;

NumberLiteral
    : [-+]?[0-9]+('.'[0-9]+)?
    ;

StringLiteral
    : '"' DoubleQuotesStringCharacters? '"'
	| '\'' SingleQuotesStringCharacters? '\''
    ;

fragment
DoubleQuotesStringCharacters
    :   DoubleQuotesStringCharacter+
    ;
    
fragment
SingleQuotesStringCharacters
    :   SingleQuotesStringCharacter+
    ;

fragment
DoubleQuotesStringCharacter
    :   ~["\\]
    |   EscapeSequence
    ;
    
fragment
SingleQuotesStringCharacter
    :   ~['\\]
    |   EscapeSequence
    ;

fragment
EscapeSequence
    :   '\\' [btnfr"'\\]
    |   OctalEscape
    |   UnicodeEscape
    ;

fragment
OctalEscape
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' ZeroToThree OctalDigit OctalDigit
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
HexDigit
    :   [0-9a-fA-F]
    ;

fragment
OctalDigit
    :   [0-7]
    ;

fragment
ZeroToThree
    :   [0-3]
    ;

ET      : '==';
NE      : '!=';
GT      : '>';
LT      : '<';
GE      : '>=';
LE      : '<=';

OR      : '||';
AND      : '&&';
NOT      : '!';

//
// Whitespace and comments
//

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;