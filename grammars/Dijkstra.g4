grammar Dijkstra;

@header {
package dijkstra.lexparse;
}

// Parser rules
dijkstraText : 			program EOF ;
program :				PROGRAM ID (declaration | statement)+ ;
 
declaration :			t=(BOOLEAN|INT|FLOAT) idList SEMICOLON? ;
//type :					BOOLEAN | INT ;
 
statement :				assignStatement
	 				 	| inputStatement 
	 				 	| outputStatement
	 				 	| iterativeStatement
	 				 	| compoundStatement
	 				 	| alternativeStatement ;
assignStatement :		idList ASSIGN expressionList SEMICOLON? ;
inputStatement :			INPUT idList SEMICOLON? ;
outputStatement :		PRINT expression SEMICOLON? ;
iterativeStatement :		DO guard+ OD ;
compoundStatement :		LBRACE (declaration | statement)+ RBRACE ;
alternativeStatement :	IF guard+ FI ;
 
idList :					ID (COMMA ID)* ;
expressionList : 		expression (COMMA expression)* ;
guard :					expression GUARD statement ;
 
/* Note that the equality operators are right-associative. This allows for a = b ~= c to be
 * interpreted as a = (b ~= c). Semantic analysis would guarantee that a is a boolean.
 * NOTE also that the placement of the <assoc=right> is different from the description in
 * TDAR. The new way of doing this is described at 
 * https://github.com/antlr/antlr4/blob/master/doc/left-recursion.md
 */
expression :				LPAR expression RPAR
						| (TILDE | MINUS) expression
						| expression (STAR | SLASH) expression
						| expression (PLUS | MINUS) expression
						| expression (LT | GT | LE | GE) expression
						| <assoc=right> expression (EQ | NEQ) expression
						| expression AND expression
						| expression OR expression
						| FLOATCONST
						| ID 
						| INTEGER
						| (TRUE | FALSE)
						;
    

// Lexical Analyzer rules
// Reserved words
AND:        	'&' ;
COMMA :     	',' ;
BOOLEAN 			: 'boolean' ;
DIV:	            	'div';
FALSE			: 'false' ;
FLOAT:          	'float';
FI				: 'fi' ;
IF				: 'if' ;
INPUT			: 'input' ;
INT				: 'int' ;
DO:             	'do';
OD:             	'od';
OR: 			'|' ;
MOD:            	'mod';
PRINT			: 'print' ;
PROGRAM			: 'program' ;
TRUE				: 'true' ;

// Operators
ASSIGN		: '<-' ;
SLASH			: '/' ;
EQ 			: '=' ;
GE:       	'>=' ;
GT 			: '>' ;
LE :        	'<=' ;
LT			: '<' ;
MINUS		: '-' ;
STAR			: '*' ;
NEQ			: '~=' ;
TILDE			: '~' ;
PLUS			: '+' ;

// Separators
GUARD		: '::' ;
LBRACE		: '{' ;
LPAR			: '(' ;
RBRACE		: '}' ;
RPAR			: ')' ;
SEMICOLON	: ';' ;

 // The rest
 ID : 			LETTER (LETTER|DIGIT|'_'|'?')* ;
 
 FLOATCONST:		DIGIT+'.'DIGIT+;
 INTEGER : 		DIGIT+ ;
 
 WS :			[ \t\r\n]+ -> skip ;
 COMMENT :		'#' .*? ('\n'|EOF);
 
 fragment
 LETTER :		[A-Za-z] ;
 
 fragment
 DIGIT :			[0-9] ;
