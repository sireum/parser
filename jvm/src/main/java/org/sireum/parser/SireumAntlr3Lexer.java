package org.sireum.parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class SireumAntlr3Lexer extends Lexer {
	public static final int EOF=-1;
	public static final int T__17=17;
	public static final int T__18=18;
	public static final int T__19=19;
	public static final int T__20=20;
	public static final int T__21=21;
	public static final int T__22=22;
	public static final int T__23=23;
	public static final int T__24=24;
	public static final int T__25=25;
	public static final int T__26=26;
	public static final int T__27=27;
	public static final int T__28=28;
	public static final int T__29=29;
	public static final int T__30=30;
	public static final int T__31=31;
	public static final int T__32=32;
	public static final int T__33=33;
	public static final int T__34=34;
	public static final int C=4;
	public static final int CHAR=5;
	public static final int COMMENT=6;
	public static final int ESC=7;
	public static final int INT=8;
	public static final int LHEADER=9;
	public static final int LID=10;
	public static final int PHEADER=11;
	public static final int PID=12;
	public static final int STRING=13;
	public static final int WS=14;
	public static final int WSC=15;
	public static final int XDIGIT=16;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public SireumAntlr3Lexer() {} 
	public SireumAntlr3Lexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public SireumAntlr3Lexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "parser/jvm/src/main/resources/SireumAntlr3.g"; }

	// $ANTLR start "T__17"
	public final void mT__17() throws RecognitionException {
		try {
			int _type = T__17;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:4:7: ( '$channel' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:4:9: '$channel'
			{
			match("$channel"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__17"

	// $ANTLR start "T__18"
	public final void mT__18() throws RecognitionException {
		try {
			int _type = T__18;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:5:7: ( '(' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:5:9: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__18"

	// $ANTLR start "T__19"
	public final void mT__19() throws RecognitionException {
		try {
			int _type = T__19;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:6:7: ( ')' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:6:9: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__19"

	// $ANTLR start "T__20"
	public final void mT__20() throws RecognitionException {
		try {
			int _type = T__20;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:7:7: ( '*' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:7:9: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__20"

	// $ANTLR start "T__21"
	public final void mT__21() throws RecognitionException {
		try {
			int _type = T__21;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:8:7: ( '+' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:8:9: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__21"

	// $ANTLR start "T__22"
	public final void mT__22() throws RecognitionException {
		try {
			int _type = T__22;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:9:7: ( '.' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:9:9: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__22"

	// $ANTLR start "T__23"
	public final void mT__23() throws RecognitionException {
		try {
			int _type = T__23;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:10:7: ( '..' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:10:9: '..'
			{
			match(".."); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__23"

	// $ANTLR start "T__24"
	public final void mT__24() throws RecognitionException {
		try {
			int _type = T__24;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:11:7: ( ':' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:11:9: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__24"

	// $ANTLR start "T__25"
	public final void mT__25() throws RecognitionException {
		try {
			int _type = T__25;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:12:7: ( ';' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:12:9: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__25"

	// $ANTLR start "T__26"
	public final void mT__26() throws RecognitionException {
		try {
			int _type = T__26;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:13:7: ( '=' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:13:9: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__26"

	// $ANTLR start "T__27"
	public final void mT__27() throws RecognitionException {
		try {
			int _type = T__27;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:14:7: ( '?' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:14:9: '?'
			{
			match('?'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__27"

	// $ANTLR start "T__28"
	public final void mT__28() throws RecognitionException {
		try {
			int _type = T__28;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:15:7: ( 'fragment' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:15:9: 'fragment'
			{
			match("fragment"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__28"

	// $ANTLR start "T__29"
	public final void mT__29() throws RecognitionException {
		try {
			int _type = T__29;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:16:7: ( 'grammar' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:16:9: 'grammar'
			{
			match("grammar"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__29"

	// $ANTLR start "T__30"
	public final void mT__30() throws RecognitionException {
		try {
			int _type = T__30;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:17:7: ( 'options' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:17:9: 'options'
			{
			match("options"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__30"

	// $ANTLR start "T__31"
	public final void mT__31() throws RecognitionException {
		try {
			int _type = T__31;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:18:7: ( '{' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:18:9: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__31"

	// $ANTLR start "T__32"
	public final void mT__32() throws RecognitionException {
		try {
			int _type = T__32;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:19:7: ( '|' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:19:9: '|'
			{
			match('|'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__32"

	// $ANTLR start "T__33"
	public final void mT__33() throws RecognitionException {
		try {
			int _type = T__33;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:20:7: ( '}' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:20:9: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__33"

	// $ANTLR start "T__34"
	public final void mT__34() throws RecognitionException {
		try {
			int _type = T__34;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:21:7: ( '~' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:21:9: '~'
			{
			match('~'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__34"

	// $ANTLR start "CHAR"
	public final void mCHAR() throws RecognitionException {
		try {
			int _type = CHAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:80:5: ( '\\'' C '\\'' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:80:7: '\\'' C '\\''
			{
			match('\''); 
			mC(); 

			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CHAR"

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException {
		try {
			int _type = STRING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:82:7: ( '\\'' C ( C )+ '\\'' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:82:9: '\\'' C ( C )+ '\\''
			{
			match('\''); 
			mC(); 

			// parser/jvm/src/main/resources/SireumAntlr3.g:82:16: ( C )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '\u0000' && LA1_0 <= '&')||(LA1_0 >= '(' && LA1_0 <= '\uFFFF')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:82:16: C
					{
					mC(); 

					}
					break;

				default :
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
			}

			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:84:4: ( '1' .. '9' ( '0' .. '9' )* )
			// parser/jvm/src/main/resources/SireumAntlr3.g:84:6: '1' .. '9' ( '0' .. '9' )*
			{
			matchRange('1','9'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:84:15: ( '0' .. '9' )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop2;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "LID"
	public final void mLID() throws RecognitionException {
		try {
			int _type = LID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:86:4: ( 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
			// parser/jvm/src/main/resources/SireumAntlr3.g:86:6: 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			{
			matchRange('A','Z'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:86:15: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '0' && LA3_0 <= '9')||(LA3_0 >= 'A' && LA3_0 <= 'Z')||LA3_0=='_'||(LA3_0 >= 'a' && LA3_0 <= 'z')) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop3;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LID"

	// $ANTLR start "PID"
	public final void mPID() throws RecognitionException {
		try {
			int _type = PID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:88:4: ( 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
			// parser/jvm/src/main/resources/SireumAntlr3.g:88:6: 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			{
			matchRange('a','z'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:88:15: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '0' && LA4_0 <= '9')||(LA4_0 >= 'A' && LA4_0 <= 'Z')||LA4_0=='_'||(LA4_0 >= 'a' && LA4_0 <= 'z')) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop4;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PID"

	// $ANTLR start "PHEADER"
	public final void mPHEADER() throws RecognitionException {
		try {
			int _type = PHEADER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:90:8: ( '@' ( WSC )* 'header' ( WSC )* '{' (~ '}' )* '}' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:90:10: '@' ( WSC )* 'header' ( WSC )* '{' (~ '}' )* '}'
			{
			match('@'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:90:14: ( WSC )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( ((LA5_0 >= '\t' && LA5_0 <= '\n')||LA5_0=='\r'||LA5_0==' ') ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop5;
				}
			}

			match("header"); 

			// parser/jvm/src/main/resources/SireumAntlr3.g:90:28: ( WSC )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= '\t' && LA6_0 <= '\n')||LA6_0=='\r'||LA6_0==' ') ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop6;
				}
			}

			match('{'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:90:37: (~ '}' )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( ((LA7_0 >= '\u0000' && LA7_0 <= '|')||(LA7_0 >= '~' && LA7_0 <= '\uFFFF')) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '|')||(input.LA(1) >= '~' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop7;
				}
			}

			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PHEADER"

	// $ANTLR start "LHEADER"
	public final void mLHEADER() throws RecognitionException {
		try {
			int _type = LHEADER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:92:8: ( '@' ( WSC )* 'lexer' ( WSC )* ':' ( WSC )* ':' ( WSC )* 'header' ( WSC )* '{' (~ '}' )* '}' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:92:10: '@' ( WSC )* 'lexer' ( WSC )* ':' ( WSC )* ':' ( WSC )* 'header' ( WSC )* '{' (~ '}' )* '}'
			{
			match('@'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:92:14: ( WSC )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( ((LA8_0 >= '\t' && LA8_0 <= '\n')||LA8_0=='\r'||LA8_0==' ') ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop8;
				}
			}

			match("lexer"); 

			// parser/jvm/src/main/resources/SireumAntlr3.g:92:27: ( WSC )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( ((LA9_0 >= '\t' && LA9_0 <= '\n')||LA9_0=='\r'||LA9_0==' ') ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop9;
				}
			}

			match(':'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:92:36: ( WSC )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( ((LA10_0 >= '\t' && LA10_0 <= '\n')||LA10_0=='\r'||LA10_0==' ') ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop10;
				}
			}

			match(':'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:92:45: ( WSC )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( ((LA11_0 >= '\t' && LA11_0 <= '\n')||LA11_0=='\r'||LA11_0==' ') ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop11;
				}
			}

			match("header"); 

			// parser/jvm/src/main/resources/SireumAntlr3.g:92:59: ( WSC )*
			loop12:
			while (true) {
				int alt12=2;
				int LA12_0 = input.LA(1);
				if ( ((LA12_0 >= '\t' && LA12_0 <= '\n')||LA12_0=='\r'||LA12_0==' ') ) {
					alt12=1;
				}

				switch (alt12) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop12;
				}
			}

			match('{'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:92:68: (~ '}' )*
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( ((LA13_0 >= '\u0000' && LA13_0 <= '|')||(LA13_0 >= '~' && LA13_0 <= '\uFFFF')) ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '|')||(input.LA(1) >= '~' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop13;
				}
			}

			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LHEADER"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:95:3: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' (~ '*' | '*' ~ '/' )* ( '*/' | '**/' ) )
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0=='/') ) {
				int LA18_1 = input.LA(2);
				if ( (LA18_1=='/') ) {
					alt18=1;
				}
				else if ( (LA18_1=='*') ) {
					alt18=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 18, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 18, 0, input);
				throw nvae;
			}

			switch (alt18) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:95:5: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
					{
					match("//"); 

					// parser/jvm/src/main/resources/SireumAntlr3.g:95:10: (~ ( '\\n' | '\\r' ) )*
					loop14:
					while (true) {
						int alt14=2;
						int LA14_0 = input.LA(1);
						if ( ((LA14_0 >= '\u0000' && LA14_0 <= '\t')||(LA14_0 >= '\u000B' && LA14_0 <= '\f')||(LA14_0 >= '\u000E' && LA14_0 <= '\uFFFF')) ) {
							alt14=1;
						}

						switch (alt14) {
						case 1 :
							// parser/jvm/src/main/resources/SireumAntlr3.g:
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop14;
						}
					}

					// parser/jvm/src/main/resources/SireumAntlr3.g:95:28: ( '\\r' )?
					int alt15=2;
					int LA15_0 = input.LA(1);
					if ( (LA15_0=='\r') ) {
						alt15=1;
					}
					switch (alt15) {
						case 1 :
							// parser/jvm/src/main/resources/SireumAntlr3.g:95:28: '\\r'
							{
							match('\r'); 
							}
							break;

					}

					match('\n'); 
					_channel=HIDDEN;
					}
					break;
				case 2 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:96:5: '/*' (~ '*' | '*' ~ '/' )* ( '*/' | '**/' )
					{
					match("/*"); 

					// parser/jvm/src/main/resources/SireumAntlr3.g:96:10: (~ '*' | '*' ~ '/' )*
					loop16:
					while (true) {
						int alt16=3;
						int LA16_0 = input.LA(1);
						if ( (LA16_0=='*') ) {
							int LA16_1 = input.LA(2);
							if ( (LA16_1=='*') ) {
								int LA16_4 = input.LA(3);
								if ( (LA16_4=='/') ) {
									int LA16_6 = input.LA(4);
									if ( ((LA16_6 >= '\u0000' && LA16_6 <= '\uFFFF')) ) {
										alt16=2;
									}

								}
								else if ( ((LA16_4 >= '\u0000' && LA16_4 <= '.')||(LA16_4 >= '0' && LA16_4 <= '\uFFFF')) ) {
									alt16=2;
								}

							}
							else if ( ((LA16_1 >= '\u0000' && LA16_1 <= ')')||(LA16_1 >= '+' && LA16_1 <= '.')||(LA16_1 >= '0' && LA16_1 <= '\uFFFF')) ) {
								alt16=2;
							}

						}
						else if ( ((LA16_0 >= '\u0000' && LA16_0 <= ')')||(LA16_0 >= '+' && LA16_0 <= '\uFFFF')) ) {
							alt16=1;
						}

						switch (alt16) {
						case 1 :
							// parser/jvm/src/main/resources/SireumAntlr3.g:96:12: ~ '*'
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= ')')||(input.LA(1) >= '+' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;
						case 2 :
							// parser/jvm/src/main/resources/SireumAntlr3.g:96:19: '*' ~ '/'
							{
							match('*'); 
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop16;
						}
					}

					// parser/jvm/src/main/resources/SireumAntlr3.g:96:31: ( '*/' | '**/' )
					int alt17=2;
					int LA17_0 = input.LA(1);
					if ( (LA17_0=='*') ) {
						int LA17_1 = input.LA(2);
						if ( (LA17_1=='/') ) {
							alt17=1;
						}
						else if ( (LA17_1=='*') ) {
							alt17=2;
						}

						else {
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 17, 1, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 17, 0, input);
						throw nvae;
					}

					switch (alt17) {
						case 1 :
							// parser/jvm/src/main/resources/SireumAntlr3.g:96:33: '*/'
							{
							match("*/"); 

							}
							break;
						case 2 :
							// parser/jvm/src/main/resources/SireumAntlr3.g:96:40: '**/'
							{
							match("**/"); 

							}
							break;

					}

					_channel=HIDDEN;
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// parser/jvm/src/main/resources/SireumAntlr3.g:99:3: ( ( WSC )+ )
			// parser/jvm/src/main/resources/SireumAntlr3.g:99:5: ( WSC )+
			{
			// parser/jvm/src/main/resources/SireumAntlr3.g:99:5: ( WSC )+
			int cnt19=0;
			loop19:
			while (true) {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( ((LA19_0 >= '\t' && LA19_0 <= '\n')||LA19_0=='\r'||LA19_0==' ') ) {
					alt19=1;
				}

				switch (alt19) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt19 >= 1 ) break loop19;
					EarlyExitException eee = new EarlyExitException(19, input);
					throw eee;
				}
				cnt19++;
			}

			_channel=HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "C"
	public final void mC() throws RecognitionException {
		try {
			// parser/jvm/src/main/resources/SireumAntlr3.g:103:2: ( ESC |~ ( '\\'' | '\\\\' ) )
			int alt20=2;
			int LA20_0 = input.LA(1);
			if ( (LA20_0=='\\') ) {
				alt20=1;
			}
			else if ( ((LA20_0 >= '\u0000' && LA20_0 <= '&')||(LA20_0 >= '(' && LA20_0 <= '[')||(LA20_0 >= ']' && LA20_0 <= '\uFFFF')) ) {
				alt20=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 20, 0, input);
				throw nvae;
			}

			switch (alt20) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:103:4: ESC
					{
					mESC(); 

					}
					break;
				case 2 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:103:10: ~ ( '\\'' | '\\\\' )
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "C"

	// $ANTLR start "ESC"
	public final void mESC() throws RecognitionException {
		try {
			// parser/jvm/src/main/resources/SireumAntlr3.g:106:4: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\\'' | '\\\"' | '\\\\' | 'u' XDIGIT XDIGIT XDIGIT XDIGIT ) )
			// parser/jvm/src/main/resources/SireumAntlr3.g:106:6: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\\'' | '\\\"' | '\\\\' | 'u' XDIGIT XDIGIT XDIGIT XDIGIT )
			{
			match('\\'); 
			// parser/jvm/src/main/resources/SireumAntlr3.g:106:11: ( 'n' | 'r' | 't' | 'b' | 'f' | '\\'' | '\\\"' | '\\\\' | 'u' XDIGIT XDIGIT XDIGIT XDIGIT )
			int alt21=9;
			switch ( input.LA(1) ) {
			case 'n':
				{
				alt21=1;
				}
				break;
			case 'r':
				{
				alt21=2;
				}
				break;
			case 't':
				{
				alt21=3;
				}
				break;
			case 'b':
				{
				alt21=4;
				}
				break;
			case 'f':
				{
				alt21=5;
				}
				break;
			case '\'':
				{
				alt21=6;
				}
				break;
			case '\"':
				{
				alt21=7;
				}
				break;
			case '\\':
				{
				alt21=8;
				}
				break;
			case 'u':
				{
				alt21=9;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 21, 0, input);
				throw nvae;
			}
			switch (alt21) {
				case 1 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:13: 'n'
					{
					match('n'); 
					}
					break;
				case 2 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:19: 'r'
					{
					match('r'); 
					}
					break;
				case 3 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:25: 't'
					{
					match('t'); 
					}
					break;
				case 4 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:31: 'b'
					{
					match('b'); 
					}
					break;
				case 5 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:37: 'f'
					{
					match('f'); 
					}
					break;
				case 6 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:43: '\\''
					{
					match('\''); 
					}
					break;
				case 7 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:50: '\\\"'
					{
					match('\"'); 
					}
					break;
				case 8 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:57: '\\\\'
					{
					match('\\'); 
					}
					break;
				case 9 :
					// parser/jvm/src/main/resources/SireumAntlr3.g:106:64: 'u' XDIGIT XDIGIT XDIGIT XDIGIT
					{
					match('u'); 
					mXDIGIT(); 

					mXDIGIT(); 

					mXDIGIT(); 

					mXDIGIT(); 

					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ESC"

	// $ANTLR start "XDIGIT"
	public final void mXDIGIT() throws RecognitionException {
		try {
			// parser/jvm/src/main/resources/SireumAntlr3.g:109:7: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "XDIGIT"

	// $ANTLR start "WSC"
	public final void mWSC() throws RecognitionException {
		try {
			// parser/jvm/src/main/resources/SireumAntlr3.g:112:4: ( ' ' | '\\t' | '\\r' | '\\n' )
			// parser/jvm/src/main/resources/SireumAntlr3.g:
			{
			if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WSC"

	@Override
	public void mTokens() throws RecognitionException {
		// parser/jvm/src/main/resources/SireumAntlr3.g:1:8: ( T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | CHAR | STRING | INT | LID | PID | PHEADER | LHEADER | COMMENT | WS )
		int alt22=27;
		alt22 = dfa22.predict(input);
		switch (alt22) {
			case 1 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:10: T__17
				{
				mT__17(); 

				}
				break;
			case 2 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:16: T__18
				{
				mT__18(); 

				}
				break;
			case 3 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:22: T__19
				{
				mT__19(); 

				}
				break;
			case 4 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:28: T__20
				{
				mT__20(); 

				}
				break;
			case 5 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:34: T__21
				{
				mT__21(); 

				}
				break;
			case 6 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:40: T__22
				{
				mT__22(); 

				}
				break;
			case 7 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:46: T__23
				{
				mT__23(); 

				}
				break;
			case 8 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:52: T__24
				{
				mT__24(); 

				}
				break;
			case 9 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:58: T__25
				{
				mT__25(); 

				}
				break;
			case 10 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:64: T__26
				{
				mT__26(); 

				}
				break;
			case 11 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:70: T__27
				{
				mT__27(); 

				}
				break;
			case 12 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:76: T__28
				{
				mT__28(); 

				}
				break;
			case 13 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:82: T__29
				{
				mT__29(); 

				}
				break;
			case 14 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:88: T__30
				{
				mT__30(); 

				}
				break;
			case 15 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:94: T__31
				{
				mT__31(); 

				}
				break;
			case 16 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:100: T__32
				{
				mT__32(); 

				}
				break;
			case 17 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:106: T__33
				{
				mT__33(); 

				}
				break;
			case 18 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:112: T__34
				{
				mT__34(); 

				}
				break;
			case 19 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:118: CHAR
				{
				mCHAR(); 

				}
				break;
			case 20 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:123: STRING
				{
				mSTRING(); 

				}
				break;
			case 21 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:130: INT
				{
				mINT(); 

				}
				break;
			case 22 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:134: LID
				{
				mLID(); 

				}
				break;
			case 23 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:138: PID
				{
				mPID(); 

				}
				break;
			case 24 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:142: PHEADER
				{
				mPHEADER(); 

				}
				break;
			case 25 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:150: LHEADER
				{
				mLHEADER(); 

				}
				break;
			case 26 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:158: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 27 :
				// parser/jvm/src/main/resources/SireumAntlr3.g:1:166: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA22 dfa22 = new DFA22(this);
	static final String DFA22_eotS =
		"\6\uffff\1\32\4\uffff\3\25\15\uffff\3\25\5\uffff\3\25\13\uffff\3\25\1"+
		"\uffff\3\25\1\uffff\3\25\1\uffff\1\25\1\102\1\103\1\uffff\1\104\3\uffff";
	static final String DFA22_eofS =
		"\105\uffff";
	static final String DFA22_minS =
		"\1\11\5\uffff\1\56\4\uffff\2\162\1\160\4\uffff\1\0\3\uffff\1\11\4\uffff"+
		"\2\141\1\164\1\42\1\0\1\11\2\uffff\1\147\1\155\1\151\10\0\1\60\2\uffff"+
		"\2\155\1\157\1\60\1\145\1\141\1\156\1\60\1\156\1\162\1\163\1\60\1\164"+
		"\2\60\1\0\1\60\3\uffff";
	static final String DFA22_maxS =
		"\1\176\5\uffff\1\56\4\uffff\2\162\1\160\4\uffff\1\uffff\3\uffff\1\154"+
		"\4\uffff\2\141\1\164\1\165\1\uffff\1\154\2\uffff\1\147\1\155\1\151\10"+
		"\uffff\1\146\2\uffff\2\155\1\157\1\146\1\145\1\141\1\156\1\146\1\156\1"+
		"\162\1\163\1\146\1\164\2\172\1\uffff\1\172\3\uffff";
	static final String DFA22_acceptS =
		"\1\uffff\1\1\1\2\1\3\1\4\1\5\1\uffff\1\10\1\11\1\12\1\13\3\uffff\1\17"+
		"\1\20\1\21\1\22\1\uffff\1\25\1\26\1\27\1\uffff\1\32\1\33\1\7\1\6\6\uffff"+
		"\1\30\1\31\14\uffff\1\23\1\24\21\uffff\1\15\1\16\1\14";
	static final String DFA22_specialS =
		"\22\uffff\1\10\14\uffff\1\4\6\uffff\1\5\1\7\1\11\1\12\1\0\1\1\1\2\1\3"+
		"\22\uffff\1\6\4\uffff}>";
	static final String[] DFA22_transitionS = {
			"\2\30\2\uffff\1\30\22\uffff\1\30\3\uffff\1\1\2\uffff\1\22\1\2\1\3\1\4"+
			"\1\5\2\uffff\1\6\1\27\1\uffff\11\23\1\7\1\10\1\uffff\1\11\1\uffff\1\12"+
			"\1\26\32\24\6\uffff\5\25\1\13\1\14\7\25\1\15\13\25\1\16\1\17\1\20\1\21",
			"",
			"",
			"",
			"",
			"",
			"\1\31",
			"",
			"",
			"",
			"",
			"\1\33",
			"\1\34",
			"\1\35",
			"",
			"",
			"",
			"",
			"\47\37\1\uffff\64\37\1\36\uffa3\37",
			"",
			"",
			"",
			"\2\40\2\uffff\1\40\22\uffff\1\40\107\uffff\1\41\3\uffff\1\42",
			"",
			"",
			"",
			"",
			"\1\43",
			"\1\44",
			"\1\45",
			"\1\54\4\uffff\1\53\64\uffff\1\55\5\uffff\1\51\3\uffff\1\52\7\uffff\1"+
			"\46\3\uffff\1\47\1\uffff\1\50\1\56",
			"\47\60\1\57\uffd8\60",
			"\2\40\2\uffff\1\40\22\uffff\1\40\107\uffff\1\41\3\uffff\1\42",
			"",
			"",
			"\1\61",
			"\1\62",
			"\1\63",
			"\47\60\1\57\uffd8\60",
			"\47\60\1\57\uffd8\60",
			"\47\60\1\57\uffd8\60",
			"\47\60\1\57\uffd8\60",
			"\47\60\1\57\uffd8\60",
			"\47\60\1\57\uffd8\60",
			"\47\60\1\57\uffd8\60",
			"\47\60\1\57\uffd8\60",
			"\12\64\7\uffff\6\64\32\uffff\6\64",
			"",
			"",
			"\1\65",
			"\1\66",
			"\1\67",
			"\12\70\7\uffff\6\70\32\uffff\6\70",
			"\1\71",
			"\1\72",
			"\1\73",
			"\12\74\7\uffff\6\74\32\uffff\6\74",
			"\1\75",
			"\1\76",
			"\1\77",
			"\12\100\7\uffff\6\100\32\uffff\6\100",
			"\1\101",
			"\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
			"\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
			"\47\60\1\57\uffd8\60",
			"\12\25\7\uffff\32\25\4\uffff\1\25\1\uffff\32\25",
			"",
			"",
			""
	};

	static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
	static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
	static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
	static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
	static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
	static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
	static final short[][] DFA22_transition;

	static {
		int numStates = DFA22_transitionS.length;
		DFA22_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
		}
	}

	protected class DFA22 extends DFA {

		public DFA22(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 22;
			this.eot = DFA22_eot;
			this.eof = DFA22_eof;
			this.min = DFA22_min;
			this.max = DFA22_max;
			this.accept = DFA22_accept;
			this.special = DFA22_special;
			this.transition = DFA22_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | CHAR | STRING | INT | LID | PID | PHEADER | LHEADER | COMMENT | WS );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA22_42 = input.LA(1);
						s = -1;
						if ( (LA22_42=='\'') ) {s = 47;}
						else if ( ((LA22_42 >= '\u0000' && LA22_42 <= '&')||(LA22_42 >= '(' && LA22_42 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA22_43 = input.LA(1);
						s = -1;
						if ( (LA22_43=='\'') ) {s = 47;}
						else if ( ((LA22_43 >= '\u0000' && LA22_43 <= '&')||(LA22_43 >= '(' && LA22_43 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA22_44 = input.LA(1);
						s = -1;
						if ( (LA22_44=='\'') ) {s = 47;}
						else if ( ((LA22_44 >= '\u0000' && LA22_44 <= '&')||(LA22_44 >= '(' && LA22_44 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 3 : 
						int LA22_45 = input.LA(1);
						s = -1;
						if ( (LA22_45=='\'') ) {s = 47;}
						else if ( ((LA22_45 >= '\u0000' && LA22_45 <= '&')||(LA22_45 >= '(' && LA22_45 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 4 : 
						int LA22_31 = input.LA(1);
						s = -1;
						if ( (LA22_31=='\'') ) {s = 47;}
						else if ( ((LA22_31 >= '\u0000' && LA22_31 <= '&')||(LA22_31 >= '(' && LA22_31 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 5 : 
						int LA22_38 = input.LA(1);
						s = -1;
						if ( (LA22_38=='\'') ) {s = 47;}
						else if ( ((LA22_38 >= '\u0000' && LA22_38 <= '&')||(LA22_38 >= '(' && LA22_38 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 6 : 
						int LA22_64 = input.LA(1);
						s = -1;
						if ( (LA22_64=='\'') ) {s = 47;}
						else if ( ((LA22_64 >= '\u0000' && LA22_64 <= '&')||(LA22_64 >= '(' && LA22_64 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 7 : 
						int LA22_39 = input.LA(1);
						s = -1;
						if ( (LA22_39=='\'') ) {s = 47;}
						else if ( ((LA22_39 >= '\u0000' && LA22_39 <= '&')||(LA22_39 >= '(' && LA22_39 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 8 : 
						int LA22_18 = input.LA(1);
						s = -1;
						if ( (LA22_18=='\\') ) {s = 30;}
						else if ( ((LA22_18 >= '\u0000' && LA22_18 <= '&')||(LA22_18 >= '(' && LA22_18 <= '[')||(LA22_18 >= ']' && LA22_18 <= '\uFFFF')) ) {s = 31;}
						if ( s>=0 ) return s;
						break;

					case 9 : 
						int LA22_40 = input.LA(1);
						s = -1;
						if ( (LA22_40=='\'') ) {s = 47;}
						else if ( ((LA22_40 >= '\u0000' && LA22_40 <= '&')||(LA22_40 >= '(' && LA22_40 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;

					case 10 : 
						int LA22_41 = input.LA(1);
						s = -1;
						if ( (LA22_41=='\'') ) {s = 47;}
						else if ( ((LA22_41 >= '\u0000' && LA22_41 <= '&')||(LA22_41 >= '(' && LA22_41 <= '\uFFFF')) ) {s = 48;}
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 22, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
