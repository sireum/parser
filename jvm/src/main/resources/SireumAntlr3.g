/*
 [The "BSD licence"]
 Copyright (c) 2005-2007 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/** ANTLR v3 grammar written in ANTLR v3 with AST construction */
grammar SireumAntlr3;

options {
	output=AST;
	ASTLabelType=Object;
	k=2;
}

@header { package org.sireum.parser; }

@lexer::header { package org.sireum.parser; }

grammarDef: 'grammar' id ';' optionsSpec? PHEADER? LHEADER? parserRule* lexerRule* EOF ;

optionsSpec: 'options' '{' option+ '}' ;

option: id '=' optionValue ';' ;
 	
optionValue: id | INT ;

parserRule: PID ':' alt ( '|' alt )* ';' ;

lexerRule: 'fragment'? LID ':' alt 
           ( 
             ( '|' alt )+
           | channel ( '|' alt channel )*
           )?
           ';'
           ;

block: '(' alt ( '|' alt )* ')' ;

alt: element+ ;

element: ( atom | block ) ( '?' | '*' | '+' )? ;

atom: range | terminal | not | PID ;

not: '~' ( CHAR | STRING | block ) ;

range: CHAR '..' CHAR ;

terminal: LID | CHAR | STRING | '.' ;
	
id: LID | PID ;

channel: '{' '$channel' '=' LID ';' '}' ;

// L E X I C A L   R U L E S

CHAR: '\'' C '\'' ;

STRING: '\'' C C+ '\'' ;

INT: '1'..'9' ('0'..'9')* ;

LID: 'A'..'Z' ( 'a'..'z' | 'A'..'Z' | '_' | '0'..'9' )* ;

PID: 'a'..'z' ( 'a'..'z' | 'A'..'Z' | '_' | '0'..'9' )* ;

PHEADER: '@' WSC* 'header' WSC* '{' ( ~'}' )* '}' ;

LHEADER: '@' WSC* 'lexer' WSC* ':' WSC* ':' WSC* 'header' WSC* '{' ( ~'}' )* '}' ;

COMMENT
  : '//' ~( '\n' | '\r' )* '\r'? '\n' {$channel=HIDDEN;}
  | '/*' ( ~'*' | '*' ~'/' )* ( '*/' | '**/' ) {$channel=HIDDEN;}
  ;

WS: WSC+ {$channel=HIDDEN;} ;

fragment
C: ESC | ~( '\'' | '\\' ) ;

fragment
ESC: '\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\'' | '\"' | '\\' | 'u' XDIGIT XDIGIT XDIGIT XDIGIT ) ;

fragment
XDIGIT: '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ;

fragment
WSC: ' ' | '\t' | '\r' | '\n' ;
