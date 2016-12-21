grammar Micro;

KEYWORD: 'PROGRAM' | 'BEGIN' | 'END' | 'FUNCTION' | 'READ' | 'WRITE' | 'IF' | 'ELSIF' | 'ENDIF' | 'DO' | 'WHILE' | 'CONTINUE' | 'BREAK' | 'RETURN' | 'INT' | 'VOID' | 'STRING' | 'FLOAT' | 'TRUE' | 'FALSE';


IDENTIFIER: [a-zA-Z][a-zA-Z0-9]*;

INTLITERAL: [0-9]+;

FLOATLITERAL: [0-9]*'.'[0-9]+;

STRINGLITERAL: '"'(~'"')*'"';

COMMENT: '--' ~('\n' | '\r')+ -> skip;

WHITESPACE: ('\t' | '\n' | ' ' | '\r' )+ -> skip;

OPERATOR: ':=' | '+' | '-' | '*' | '/' | '=' | '!=' | '<' | '>' | '(' | ')' | ';' | ',' | '<=' | '>=' ;

dummy: 'xiaoxiao';
