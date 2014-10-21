/*
 * 
 */

grammar SferaScriptGrammar;

parse
    : init? rules? EOF
    ;
    
init
	: Script
	;

rules
    : ruleLine+
    ;

ruleLine
    : trigger COLON action
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
    | LPAREN orExpression RPAREN
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
    : FinalNodeId (ET|NE|GT|LT|GE|LE) StringLiteral
    ;
    
numberComparison
    : FinalNodeId (ET|NE|GT|LT|GE|LE) NumberLiteral
    ;
    
booleanComparison
    : FinalNodeId (ET|NE) BooleanLiteral
    ;
    
unknownComparison
    : FinalNodeId (ET|NE) Unknown
    ;

transientEvent
    : NodeId
    | FinalNodeId
    ;

FinalNodeId
    : NodeId (DOT NodeId)+
    ;
    
BooleanLiteral
    :   'true'
    |   'false'
    ;
    
Unknown
    :   'unknown'
    ;
    
NodeId
    : NodeFirstLetter LetterOrDigit* (LPAREN [0-9]+ RPAREN)?
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
      
LPAREN  : '(';
RPAREN  : ')';

DOT : '.';

ET      : '==';
NE      : '!=';
GT      : '>';
LT      : '<';
GE      : '>=';
LE      : '<=';

OR      : '||';
AND      : '&&';
NOT      : '!';

COLON   : ':';

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