 package org.sireum.parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


/** ANTLR v3 grammar written in ANTLR v3 with AST construction */
@SuppressWarnings("all")
public class SireumAntlr3Parser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "C", "CHAR", "COMMENT", "ESC", 
		"INT", "LHEADER", "LID", "PHEADER", "PID", "STRING", "WS", "WSC", "XDIGIT", 
		"'$channel'", "'('", "')'", "'*'", "'+'", "'.'", "'..'", "':'", "';'", 
		"'='", "'?'", "'fragment'", "'grammar'", "'options'", "'{'", "'|'", "'}'", 
		"'~'"
	};
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
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public SireumAntlr3Parser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public SireumAntlr3Parser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return SireumAntlr3Parser.tokenNames; }
	@Override public String getGrammarFileName() { return "/Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g"; }


	public static class grammarDef_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "grammarDef"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:1: grammarDef : 'grammar' id ';' ( optionsSpec )? ( PHEADER )? ( LHEADER )? ( parserRule )* ( lexerRule )* EOF ;
	public final SireumAntlr3Parser.grammarDef_return grammarDef() throws RecognitionException {
		SireumAntlr3Parser.grammarDef_return retval = new SireumAntlr3Parser.grammarDef_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal1=null;
		Token char_literal3=null;
		Token PHEADER5=null;
		Token LHEADER6=null;
		Token EOF9=null;
		ParserRuleReturnScope id2 =null;
		ParserRuleReturnScope optionsSpec4 =null;
		ParserRuleReturnScope parserRule7 =null;
		ParserRuleReturnScope lexerRule8 =null;

		Object string_literal1_tree=null;
		Object char_literal3_tree=null;
		Object PHEADER5_tree=null;
		Object LHEADER6_tree=null;
		Object EOF9_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:11: ( 'grammar' id ';' ( optionsSpec )? ( PHEADER )? ( LHEADER )? ( parserRule )* ( lexerRule )* EOF )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:13: 'grammar' id ';' ( optionsSpec )? ( PHEADER )? ( LHEADER )? ( parserRule )* ( lexerRule )* EOF
			{
			root_0 = (Object)adaptor.nil();


			string_literal1=(Token)match(input,29,FOLLOW_29_in_grammarDef52); 
			string_literal1_tree = (Object)adaptor.create(string_literal1);
			adaptor.addChild(root_0, string_literal1_tree);

			pushFollow(FOLLOW_id_in_grammarDef54);
			id2=id();
			state._fsp--;

			adaptor.addChild(root_0, id2.getTree());

			char_literal3=(Token)match(input,25,FOLLOW_25_in_grammarDef56); 
			char_literal3_tree = (Object)adaptor.create(char_literal3);
			adaptor.addChild(root_0, char_literal3_tree);

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:30: ( optionsSpec )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==30) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:30: optionsSpec
					{
					pushFollow(FOLLOW_optionsSpec_in_grammarDef58);
					optionsSpec4=optionsSpec();
					state._fsp--;

					adaptor.addChild(root_0, optionsSpec4.getTree());

					}
					break;

			}

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:43: ( PHEADER )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==PHEADER) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:43: PHEADER
					{
					PHEADER5=(Token)match(input,PHEADER,FOLLOW_PHEADER_in_grammarDef61); 
					PHEADER5_tree = (Object)adaptor.create(PHEADER5);
					adaptor.addChild(root_0, PHEADER5_tree);

					}
					break;

			}

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:52: ( LHEADER )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==LHEADER) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:52: LHEADER
					{
					LHEADER6=(Token)match(input,LHEADER,FOLLOW_LHEADER_in_grammarDef64); 
					LHEADER6_tree = (Object)adaptor.create(LHEADER6);
					adaptor.addChild(root_0, LHEADER6_tree);

					}
					break;

			}

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:61: ( parserRule )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0==PID) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:61: parserRule
					{
					pushFollow(FOLLOW_parserRule_in_grammarDef67);
					parserRule7=parserRule();
					state._fsp--;

					adaptor.addChild(root_0, parserRule7.getTree());

					}
					break;

				default :
					break loop4;
				}
			}

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:73: ( lexerRule )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==LID||LA5_0==28) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:42:73: lexerRule
					{
					pushFollow(FOLLOW_lexerRule_in_grammarDef70);
					lexerRule8=lexerRule();
					state._fsp--;

					adaptor.addChild(root_0, lexerRule8.getTree());

					}
					break;

				default :
					break loop5;
				}
			}

			EOF9=(Token)match(input,EOF,FOLLOW_EOF_in_grammarDef73); 
			EOF9_tree = (Object)adaptor.create(EOF9);
			adaptor.addChild(root_0, EOF9_tree);

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "grammarDef"


	public static class optionsSpec_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "optionsSpec"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:44:1: optionsSpec : 'options' '{' ( option )+ '}' ;
	public final SireumAntlr3Parser.optionsSpec_return optionsSpec() throws RecognitionException {
		SireumAntlr3Parser.optionsSpec_return retval = new SireumAntlr3Parser.optionsSpec_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal10=null;
		Token char_literal11=null;
		Token char_literal13=null;
		ParserRuleReturnScope option12 =null;

		Object string_literal10_tree=null;
		Object char_literal11_tree=null;
		Object char_literal13_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:44:12: ( 'options' '{' ( option )+ '}' )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:44:14: 'options' '{' ( option )+ '}'
			{
			root_0 = (Object)adaptor.nil();


			string_literal10=(Token)match(input,30,FOLLOW_30_in_optionsSpec81); 
			string_literal10_tree = (Object)adaptor.create(string_literal10);
			adaptor.addChild(root_0, string_literal10_tree);

			char_literal11=(Token)match(input,31,FOLLOW_31_in_optionsSpec83); 
			char_literal11_tree = (Object)adaptor.create(char_literal11);
			adaptor.addChild(root_0, char_literal11_tree);

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:44:28: ( option )+
			int cnt6=0;
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==LID||LA6_0==PID) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:44:28: option
					{
					pushFollow(FOLLOW_option_in_optionsSpec85);
					option12=option();
					state._fsp--;

					adaptor.addChild(root_0, option12.getTree());

					}
					break;

				default :
					if ( cnt6 >= 1 ) break loop6;
					EarlyExitException eee = new EarlyExitException(6, input);
					throw eee;
				}
				cnt6++;
			}

			char_literal13=(Token)match(input,33,FOLLOW_33_in_optionsSpec88); 
			char_literal13_tree = (Object)adaptor.create(char_literal13);
			adaptor.addChild(root_0, char_literal13_tree);

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "optionsSpec"


	public static class option_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "option"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:46:1: option : id '=' optionValue ';' ;
	public final SireumAntlr3Parser.option_return option() throws RecognitionException {
		SireumAntlr3Parser.option_return retval = new SireumAntlr3Parser.option_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal15=null;
		Token char_literal17=null;
		ParserRuleReturnScope id14 =null;
		ParserRuleReturnScope optionValue16 =null;

		Object char_literal15_tree=null;
		Object char_literal17_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:46:7: ( id '=' optionValue ';' )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:46:9: id '=' optionValue ';'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_id_in_option96);
			id14=id();
			state._fsp--;

			adaptor.addChild(root_0, id14.getTree());

			char_literal15=(Token)match(input,26,FOLLOW_26_in_option98); 
			char_literal15_tree = (Object)adaptor.create(char_literal15);
			adaptor.addChild(root_0, char_literal15_tree);

			pushFollow(FOLLOW_optionValue_in_option100);
			optionValue16=optionValue();
			state._fsp--;

			adaptor.addChild(root_0, optionValue16.getTree());

			char_literal17=(Token)match(input,25,FOLLOW_25_in_option102); 
			char_literal17_tree = (Object)adaptor.create(char_literal17);
			adaptor.addChild(root_0, char_literal17_tree);

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "option"


	public static class optionValue_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "optionValue"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:48:1: optionValue : ( id | INT );
	public final SireumAntlr3Parser.optionValue_return optionValue() throws RecognitionException {
		SireumAntlr3Parser.optionValue_return retval = new SireumAntlr3Parser.optionValue_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token INT19=null;
		ParserRuleReturnScope id18 =null;

		Object INT19_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:48:12: ( id | INT )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==LID||LA7_0==PID) ) {
				alt7=1;
			}
			else if ( (LA7_0==INT) ) {
				alt7=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:48:14: id
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_id_in_optionValue112);
					id18=id();
					state._fsp--;

					adaptor.addChild(root_0, id18.getTree());

					}
					break;
				case 2 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:48:19: INT
					{
					root_0 = (Object)adaptor.nil();


					INT19=(Token)match(input,INT,FOLLOW_INT_in_optionValue116); 
					INT19_tree = (Object)adaptor.create(INT19);
					adaptor.addChild(root_0, INT19_tree);

					}
					break;

			}
			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "optionValue"


	public static class parserRule_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "parserRule"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:50:1: parserRule : PID ':' alt ( '|' alt )* ';' ;
	public final SireumAntlr3Parser.parserRule_return parserRule() throws RecognitionException {
		SireumAntlr3Parser.parserRule_return retval = new SireumAntlr3Parser.parserRule_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PID20=null;
		Token char_literal21=null;
		Token char_literal23=null;
		Token char_literal25=null;
		ParserRuleReturnScope alt22 =null;
		ParserRuleReturnScope alt24 =null;

		Object PID20_tree=null;
		Object char_literal21_tree=null;
		Object char_literal23_tree=null;
		Object char_literal25_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:50:11: ( PID ':' alt ( '|' alt )* ';' )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:50:13: PID ':' alt ( '|' alt )* ';'
			{
			root_0 = (Object)adaptor.nil();


			PID20=(Token)match(input,PID,FOLLOW_PID_in_parserRule124); 
			PID20_tree = (Object)adaptor.create(PID20);
			adaptor.addChild(root_0, PID20_tree);

			char_literal21=(Token)match(input,24,FOLLOW_24_in_parserRule126); 
			char_literal21_tree = (Object)adaptor.create(char_literal21);
			adaptor.addChild(root_0, char_literal21_tree);

			pushFollow(FOLLOW_alt_in_parserRule128);
			alt22=alt();
			state._fsp--;

			adaptor.addChild(root_0, alt22.getTree());

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:50:25: ( '|' alt )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==32) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:50:27: '|' alt
					{
					char_literal23=(Token)match(input,32,FOLLOW_32_in_parserRule132); 
					char_literal23_tree = (Object)adaptor.create(char_literal23);
					adaptor.addChild(root_0, char_literal23_tree);

					pushFollow(FOLLOW_alt_in_parserRule134);
					alt24=alt();
					state._fsp--;

					adaptor.addChild(root_0, alt24.getTree());

					}
					break;

				default :
					break loop8;
				}
			}

			char_literal25=(Token)match(input,25,FOLLOW_25_in_parserRule139); 
			char_literal25_tree = (Object)adaptor.create(char_literal25);
			adaptor.addChild(root_0, char_literal25_tree);

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "parserRule"


	public static class lexerRule_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "lexerRule"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:52:1: lexerRule : ( 'fragment' )? LID ':' alt ( ( '|' alt )+ | channel ( '|' alt channel )* )? ';' ;
	public final SireumAntlr3Parser.lexerRule_return lexerRule() throws RecognitionException {
		SireumAntlr3Parser.lexerRule_return retval = new SireumAntlr3Parser.lexerRule_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal26=null;
		Token LID27=null;
		Token char_literal28=null;
		Token char_literal30=null;
		Token char_literal33=null;
		Token char_literal36=null;
		ParserRuleReturnScope alt29 =null;
		ParserRuleReturnScope alt31 =null;
		ParserRuleReturnScope channel32 =null;
		ParserRuleReturnScope alt34 =null;
		ParserRuleReturnScope channel35 =null;

		Object string_literal26_tree=null;
		Object LID27_tree=null;
		Object char_literal28_tree=null;
		Object char_literal30_tree=null;
		Object char_literal33_tree=null;
		Object char_literal36_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:52:10: ( ( 'fragment' )? LID ':' alt ( ( '|' alt )+ | channel ( '|' alt channel )* )? ';' )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:52:12: ( 'fragment' )? LID ':' alt ( ( '|' alt )+ | channel ( '|' alt channel )* )? ';'
			{
			root_0 = (Object)adaptor.nil();


			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:52:12: ( 'fragment' )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0==28) ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:52:12: 'fragment'
					{
					string_literal26=(Token)match(input,28,FOLLOW_28_in_lexerRule147); 
					string_literal26_tree = (Object)adaptor.create(string_literal26);
					adaptor.addChild(root_0, string_literal26_tree);

					}
					break;

			}

			LID27=(Token)match(input,LID,FOLLOW_LID_in_lexerRule150); 
			LID27_tree = (Object)adaptor.create(LID27);
			adaptor.addChild(root_0, LID27_tree);

			char_literal28=(Token)match(input,24,FOLLOW_24_in_lexerRule152); 
			char_literal28_tree = (Object)adaptor.create(char_literal28);
			adaptor.addChild(root_0, char_literal28_tree);

			pushFollow(FOLLOW_alt_in_lexerRule154);
			alt29=alt();
			state._fsp--;

			adaptor.addChild(root_0, alt29.getTree());

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:53:12: ( ( '|' alt )+ | channel ( '|' alt channel )* )?
			int alt12=3;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==32) ) {
				alt12=1;
			}
			else if ( (LA12_0==31) ) {
				alt12=2;
			}
			switch (alt12) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:54:14: ( '|' alt )+
					{
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:54:14: ( '|' alt )+
					int cnt10=0;
					loop10:
					while (true) {
						int alt10=2;
						int LA10_0 = input.LA(1);
						if ( (LA10_0==32) ) {
							alt10=1;
						}

						switch (alt10) {
						case 1 :
							// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:54:16: '|' alt
							{
							char_literal30=(Token)match(input,32,FOLLOW_32_in_lexerRule186); 
							char_literal30_tree = (Object)adaptor.create(char_literal30);
							adaptor.addChild(root_0, char_literal30_tree);

							pushFollow(FOLLOW_alt_in_lexerRule188);
							alt31=alt();
							state._fsp--;

							adaptor.addChild(root_0, alt31.getTree());

							}
							break;

						default :
							if ( cnt10 >= 1 ) break loop10;
							EarlyExitException eee = new EarlyExitException(10, input);
							throw eee;
						}
						cnt10++;
					}

					}
					break;
				case 2 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:55:14: channel ( '|' alt channel )*
					{
					pushFollow(FOLLOW_channel_in_lexerRule206);
					channel32=channel();
					state._fsp--;

					adaptor.addChild(root_0, channel32.getTree());

					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:55:22: ( '|' alt channel )*
					loop11:
					while (true) {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( (LA11_0==32) ) {
							alt11=1;
						}

						switch (alt11) {
						case 1 :
							// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:55:24: '|' alt channel
							{
							char_literal33=(Token)match(input,32,FOLLOW_32_in_lexerRule210); 
							char_literal33_tree = (Object)adaptor.create(char_literal33);
							adaptor.addChild(root_0, char_literal33_tree);

							pushFollow(FOLLOW_alt_in_lexerRule212);
							alt34=alt();
							state._fsp--;

							adaptor.addChild(root_0, alt34.getTree());

							pushFollow(FOLLOW_channel_in_lexerRule214);
							channel35=channel();
							state._fsp--;

							adaptor.addChild(root_0, channel35.getTree());

							}
							break;

						default :
							break loop11;
						}
					}

					}
					break;

			}

			char_literal36=(Token)match(input,25,FOLLOW_25_in_lexerRule244); 
			char_literal36_tree = (Object)adaptor.create(char_literal36);
			adaptor.addChild(root_0, char_literal36_tree);

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "lexerRule"


	public static class block_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "block"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:60:1: block : '(' alt ( '|' alt )* ')' ;
	public final SireumAntlr3Parser.block_return block() throws RecognitionException {
		SireumAntlr3Parser.block_return retval = new SireumAntlr3Parser.block_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal37=null;
		Token char_literal39=null;
		Token char_literal41=null;
		ParserRuleReturnScope alt38 =null;
		ParserRuleReturnScope alt40 =null;

		Object char_literal37_tree=null;
		Object char_literal39_tree=null;
		Object char_literal41_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:60:6: ( '(' alt ( '|' alt )* ')' )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:60:8: '(' alt ( '|' alt )* ')'
			{
			root_0 = (Object)adaptor.nil();


			char_literal37=(Token)match(input,18,FOLLOW_18_in_block263); 
			char_literal37_tree = (Object)adaptor.create(char_literal37);
			adaptor.addChild(root_0, char_literal37_tree);

			pushFollow(FOLLOW_alt_in_block265);
			alt38=alt();
			state._fsp--;

			adaptor.addChild(root_0, alt38.getTree());

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:60:16: ( '|' alt )*
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( (LA13_0==32) ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:60:18: '|' alt
					{
					char_literal39=(Token)match(input,32,FOLLOW_32_in_block269); 
					char_literal39_tree = (Object)adaptor.create(char_literal39);
					adaptor.addChild(root_0, char_literal39_tree);

					pushFollow(FOLLOW_alt_in_block271);
					alt40=alt();
					state._fsp--;

					adaptor.addChild(root_0, alt40.getTree());

					}
					break;

				default :
					break loop13;
				}
			}

			char_literal41=(Token)match(input,19,FOLLOW_19_in_block276); 
			char_literal41_tree = (Object)adaptor.create(char_literal41);
			adaptor.addChild(root_0, char_literal41_tree);

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "block"


	public static class alt_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "alt"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:62:1: alt : ( element )+ ;
	public final SireumAntlr3Parser.alt_return alt() throws RecognitionException {
		SireumAntlr3Parser.alt_return retval = new SireumAntlr3Parser.alt_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope element42 =null;


		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:62:4: ( ( element )+ )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:62:6: ( element )+
			{
			root_0 = (Object)adaptor.nil();


			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:62:6: ( element )+
			int cnt14=0;
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==CHAR||LA14_0==LID||(LA14_0 >= PID && LA14_0 <= STRING)||LA14_0==18||LA14_0==22||LA14_0==34) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:62:6: element
					{
					pushFollow(FOLLOW_element_in_alt284);
					element42=element();
					state._fsp--;

					adaptor.addChild(root_0, element42.getTree());

					}
					break;

				default :
					if ( cnt14 >= 1 ) break loop14;
					EarlyExitException eee = new EarlyExitException(14, input);
					throw eee;
				}
				cnt14++;
			}

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "alt"


	public static class element_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "element"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:64:1: element : ( atom | block ) ( '?' | '*' | '+' )? ;
	public final SireumAntlr3Parser.element_return element() throws RecognitionException {
		SireumAntlr3Parser.element_return retval = new SireumAntlr3Parser.element_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set45=null;
		ParserRuleReturnScope atom43 =null;
		ParserRuleReturnScope block44 =null;

		Object set45_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:64:8: ( ( atom | block ) ( '?' | '*' | '+' )? )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:64:10: ( atom | block ) ( '?' | '*' | '+' )?
			{
			root_0 = (Object)adaptor.nil();


			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:64:10: ( atom | block )
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==CHAR||LA15_0==LID||(LA15_0 >= PID && LA15_0 <= STRING)||LA15_0==22||LA15_0==34) ) {
				alt15=1;
			}
			else if ( (LA15_0==18) ) {
				alt15=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}

			switch (alt15) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:64:12: atom
					{
					pushFollow(FOLLOW_atom_in_element295);
					atom43=atom();
					state._fsp--;

					adaptor.addChild(root_0, atom43.getTree());

					}
					break;
				case 2 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:64:19: block
					{
					pushFollow(FOLLOW_block_in_element299);
					block44=block();
					state._fsp--;

					adaptor.addChild(root_0, block44.getTree());

					}
					break;

			}

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:64:27: ( '?' | '*' | '+' )?
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( ((LA16_0 >= 20 && LA16_0 <= 21)||LA16_0==27) ) {
				alt16=1;
			}
			switch (alt16) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:
					{
					set45=input.LT(1);
					if ( (input.LA(1) >= 20 && input.LA(1) <= 21)||input.LA(1)==27 ) {
						input.consume();
						adaptor.addChild(root_0, (Object)adaptor.create(set45));
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "element"


	public static class atom_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "atom"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:66:1: atom : ( range | terminal | not | PID );
	public final SireumAntlr3Parser.atom_return atom() throws RecognitionException {
		SireumAntlr3Parser.atom_return retval = new SireumAntlr3Parser.atom_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token PID49=null;
		ParserRuleReturnScope range46 =null;
		ParserRuleReturnScope terminal47 =null;
		ParserRuleReturnScope not48 =null;

		Object PID49_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:66:5: ( range | terminal | not | PID )
			int alt17=4;
			switch ( input.LA(1) ) {
			case CHAR:
				{
				int LA17_1 = input.LA(2);
				if ( (LA17_1==23) ) {
					alt17=1;
				}
				else if ( (LA17_1==CHAR||LA17_1==LID||(LA17_1 >= PID && LA17_1 <= STRING)||(LA17_1 >= 18 && LA17_1 <= 22)||LA17_1==25||LA17_1==27||(LA17_1 >= 31 && LA17_1 <= 32)||LA17_1==34) ) {
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
				break;
			case LID:
			case STRING:
			case 22:
				{
				alt17=2;
				}
				break;
			case 34:
				{
				alt17=3;
				}
				break;
			case PID:
				{
				alt17=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 17, 0, input);
				throw nvae;
			}
			switch (alt17) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:66:7: range
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_range_in_atom324);
					range46=range();
					state._fsp--;

					adaptor.addChild(root_0, range46.getTree());

					}
					break;
				case 2 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:66:15: terminal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_terminal_in_atom328);
					terminal47=terminal();
					state._fsp--;

					adaptor.addChild(root_0, terminal47.getTree());

					}
					break;
				case 3 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:66:26: not
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_not_in_atom332);
					not48=not();
					state._fsp--;

					adaptor.addChild(root_0, not48.getTree());

					}
					break;
				case 4 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:66:32: PID
					{
					root_0 = (Object)adaptor.nil();


					PID49=(Token)match(input,PID,FOLLOW_PID_in_atom336); 
					PID49_tree = (Object)adaptor.create(PID49);
					adaptor.addChild(root_0, PID49_tree);

					}
					break;

			}
			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "atom"


	public static class not_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "not"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:68:1: not : '~' ( CHAR | STRING | block ) ;
	public final SireumAntlr3Parser.not_return not() throws RecognitionException {
		SireumAntlr3Parser.not_return retval = new SireumAntlr3Parser.not_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal50=null;
		Token CHAR51=null;
		Token STRING52=null;
		ParserRuleReturnScope block53 =null;

		Object char_literal50_tree=null;
		Object CHAR51_tree=null;
		Object STRING52_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:68:4: ( '~' ( CHAR | STRING | block ) )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:68:6: '~' ( CHAR | STRING | block )
			{
			root_0 = (Object)adaptor.nil();


			char_literal50=(Token)match(input,34,FOLLOW_34_in_not344); 
			char_literal50_tree = (Object)adaptor.create(char_literal50);
			adaptor.addChild(root_0, char_literal50_tree);

			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:68:10: ( CHAR | STRING | block )
			int alt18=3;
			switch ( input.LA(1) ) {
			case CHAR:
				{
				alt18=1;
				}
				break;
			case STRING:
				{
				alt18=2;
				}
				break;
			case 18:
				{
				alt18=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 18, 0, input);
				throw nvae;
			}
			switch (alt18) {
				case 1 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:68:12: CHAR
					{
					CHAR51=(Token)match(input,CHAR,FOLLOW_CHAR_in_not348); 
					CHAR51_tree = (Object)adaptor.create(CHAR51);
					adaptor.addChild(root_0, CHAR51_tree);

					}
					break;
				case 2 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:68:19: STRING
					{
					STRING52=(Token)match(input,STRING,FOLLOW_STRING_in_not352); 
					STRING52_tree = (Object)adaptor.create(STRING52);
					adaptor.addChild(root_0, STRING52_tree);

					}
					break;
				case 3 :
					// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:68:28: block
					{
					pushFollow(FOLLOW_block_in_not356);
					block53=block();
					state._fsp--;

					adaptor.addChild(root_0, block53.getTree());

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "not"


	public static class range_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "range"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:70:1: range : CHAR '..' CHAR ;
	public final SireumAntlr3Parser.range_return range() throws RecognitionException {
		SireumAntlr3Parser.range_return retval = new SireumAntlr3Parser.range_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token CHAR54=null;
		Token string_literal55=null;
		Token CHAR56=null;

		Object CHAR54_tree=null;
		Object string_literal55_tree=null;
		Object CHAR56_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:70:6: ( CHAR '..' CHAR )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:70:8: CHAR '..' CHAR
			{
			root_0 = (Object)adaptor.nil();


			CHAR54=(Token)match(input,CHAR,FOLLOW_CHAR_in_range366); 
			CHAR54_tree = (Object)adaptor.create(CHAR54);
			adaptor.addChild(root_0, CHAR54_tree);

			string_literal55=(Token)match(input,23,FOLLOW_23_in_range368); 
			string_literal55_tree = (Object)adaptor.create(string_literal55);
			adaptor.addChild(root_0, string_literal55_tree);

			CHAR56=(Token)match(input,CHAR,FOLLOW_CHAR_in_range370); 
			CHAR56_tree = (Object)adaptor.create(CHAR56);
			adaptor.addChild(root_0, CHAR56_tree);

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "range"


	public static class terminal_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "terminal"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:72:1: terminal : ( LID | CHAR | STRING | '.' );
	public final SireumAntlr3Parser.terminal_return terminal() throws RecognitionException {
		SireumAntlr3Parser.terminal_return retval = new SireumAntlr3Parser.terminal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set57=null;

		Object set57_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:72:9: ( LID | CHAR | STRING | '.' )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:
			{
			root_0 = (Object)adaptor.nil();


			set57=input.LT(1);
			if ( input.LA(1)==CHAR||input.LA(1)==LID||input.LA(1)==STRING||input.LA(1)==22 ) {
				input.consume();
				adaptor.addChild(root_0, (Object)adaptor.create(set57));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "terminal"


	public static class id_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "id"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:74:1: id : ( LID | PID );
	public final SireumAntlr3Parser.id_return id() throws RecognitionException {
		SireumAntlr3Parser.id_return retval = new SireumAntlr3Parser.id_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set58=null;

		Object set58_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:74:3: ( LID | PID )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:
			{
			root_0 = (Object)adaptor.nil();


			set58=input.LT(1);
			if ( input.LA(1)==LID||input.LA(1)==PID ) {
				input.consume();
				adaptor.addChild(root_0, (Object)adaptor.create(set58));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "id"


	public static class channel_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "channel"
	// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:76:1: channel : '{' '$channel' '=' LID ';' '}' ;
	public final SireumAntlr3Parser.channel_return channel() throws RecognitionException {
		SireumAntlr3Parser.channel_return retval = new SireumAntlr3Parser.channel_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal59=null;
		Token string_literal60=null;
		Token char_literal61=null;
		Token LID62=null;
		Token char_literal63=null;
		Token char_literal64=null;

		Object char_literal59_tree=null;
		Object string_literal60_tree=null;
		Object char_literal61_tree=null;
		Object LID62_tree=null;
		Object char_literal63_tree=null;
		Object char_literal64_tree=null;

		try {
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:76:8: ( '{' '$channel' '=' LID ';' '}' )
			// /Users/robby/Repositories/Robby/slang-ll1/jvm/src/main/resources/SireumAntlr3.g:76:10: '{' '$channel' '=' LID ';' '}'
			{
			root_0 = (Object)adaptor.nil();


			char_literal59=(Token)match(input,31,FOLLOW_31_in_channel411); 
			char_literal59_tree = (Object)adaptor.create(char_literal59);
			adaptor.addChild(root_0, char_literal59_tree);

			string_literal60=(Token)match(input,17,FOLLOW_17_in_channel413); 
			string_literal60_tree = (Object)adaptor.create(string_literal60);
			adaptor.addChild(root_0, string_literal60_tree);

			char_literal61=(Token)match(input,26,FOLLOW_26_in_channel415); 
			char_literal61_tree = (Object)adaptor.create(char_literal61);
			adaptor.addChild(root_0, char_literal61_tree);

			LID62=(Token)match(input,LID,FOLLOW_LID_in_channel417); 
			LID62_tree = (Object)adaptor.create(LID62);
			adaptor.addChild(root_0, LID62_tree);

			char_literal63=(Token)match(input,25,FOLLOW_25_in_channel419); 
			char_literal63_tree = (Object)adaptor.create(char_literal63);
			adaptor.addChild(root_0, char_literal63_tree);

			char_literal64=(Token)match(input,33,FOLLOW_33_in_channel421); 
			char_literal64_tree = (Object)adaptor.create(char_literal64);
			adaptor.addChild(root_0, char_literal64_tree);

			}

			retval.stop = input.LT(-1);

			adaptor.setTokenBoundaries(root_0, retval.start, retval.stop);
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "channel"

	// Delegated rules



	public static final BitSet FOLLOW_29_in_grammarDef52 = new BitSet(new long[]{0x0000000000001400L});
	public static final BitSet FOLLOW_id_in_grammarDef54 = new BitSet(new long[]{0x0000000002000000L});
	public static final BitSet FOLLOW_25_in_grammarDef56 = new BitSet(new long[]{0x0000000050001E00L});
	public static final BitSet FOLLOW_optionsSpec_in_grammarDef58 = new BitSet(new long[]{0x0000000010001E00L});
	public static final BitSet FOLLOW_PHEADER_in_grammarDef61 = new BitSet(new long[]{0x0000000010001600L});
	public static final BitSet FOLLOW_LHEADER_in_grammarDef64 = new BitSet(new long[]{0x0000000010001400L});
	public static final BitSet FOLLOW_parserRule_in_grammarDef67 = new BitSet(new long[]{0x0000000010001400L});
	public static final BitSet FOLLOW_lexerRule_in_grammarDef70 = new BitSet(new long[]{0x0000000010000400L});
	public static final BitSet FOLLOW_EOF_in_grammarDef73 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_30_in_optionsSpec81 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_31_in_optionsSpec83 = new BitSet(new long[]{0x0000000000001400L});
	public static final BitSet FOLLOW_option_in_optionsSpec85 = new BitSet(new long[]{0x0000000200001400L});
	public static final BitSet FOLLOW_33_in_optionsSpec88 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_id_in_option96 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_26_in_option98 = new BitSet(new long[]{0x0000000000001500L});
	public static final BitSet FOLLOW_optionValue_in_option100 = new BitSet(new long[]{0x0000000002000000L});
	public static final BitSet FOLLOW_25_in_option102 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_id_in_optionValue112 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_optionValue116 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PID_in_parserRule124 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_24_in_parserRule126 = new BitSet(new long[]{0x0000000400443420L});
	public static final BitSet FOLLOW_alt_in_parserRule128 = new BitSet(new long[]{0x0000000102000000L});
	public static final BitSet FOLLOW_32_in_parserRule132 = new BitSet(new long[]{0x0000000400443420L});
	public static final BitSet FOLLOW_alt_in_parserRule134 = new BitSet(new long[]{0x0000000102000000L});
	public static final BitSet FOLLOW_25_in_parserRule139 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_28_in_lexerRule147 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_LID_in_lexerRule150 = new BitSet(new long[]{0x0000000001000000L});
	public static final BitSet FOLLOW_24_in_lexerRule152 = new BitSet(new long[]{0x0000000400443420L});
	public static final BitSet FOLLOW_alt_in_lexerRule154 = new BitSet(new long[]{0x0000000182000000L});
	public static final BitSet FOLLOW_32_in_lexerRule186 = new BitSet(new long[]{0x0000000400443420L});
	public static final BitSet FOLLOW_alt_in_lexerRule188 = new BitSet(new long[]{0x0000000102000000L});
	public static final BitSet FOLLOW_channel_in_lexerRule206 = new BitSet(new long[]{0x0000000102000000L});
	public static final BitSet FOLLOW_32_in_lexerRule210 = new BitSet(new long[]{0x0000000400443420L});
	public static final BitSet FOLLOW_alt_in_lexerRule212 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_channel_in_lexerRule214 = new BitSet(new long[]{0x0000000102000000L});
	public static final BitSet FOLLOW_25_in_lexerRule244 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_18_in_block263 = new BitSet(new long[]{0x0000000400443420L});
	public static final BitSet FOLLOW_alt_in_block265 = new BitSet(new long[]{0x0000000100080000L});
	public static final BitSet FOLLOW_32_in_block269 = new BitSet(new long[]{0x0000000400443420L});
	public static final BitSet FOLLOW_alt_in_block271 = new BitSet(new long[]{0x0000000100080000L});
	public static final BitSet FOLLOW_19_in_block276 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_element_in_alt284 = new BitSet(new long[]{0x0000000400443422L});
	public static final BitSet FOLLOW_atom_in_element295 = new BitSet(new long[]{0x0000000008300002L});
	public static final BitSet FOLLOW_block_in_element299 = new BitSet(new long[]{0x0000000008300002L});
	public static final BitSet FOLLOW_range_in_atom324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_terminal_in_atom328 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_not_in_atom332 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PID_in_atom336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_34_in_not344 = new BitSet(new long[]{0x0000000000042020L});
	public static final BitSet FOLLOW_CHAR_in_not348 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_not352 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_block_in_not356 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_CHAR_in_range366 = new BitSet(new long[]{0x0000000000800000L});
	public static final BitSet FOLLOW_23_in_range368 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_CHAR_in_range370 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_31_in_channel411 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_17_in_channel413 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_26_in_channel415 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_LID_in_channel417 = new BitSet(new long[]{0x0000000002000000L});
	public static final BitSet FOLLOW_25_in_channel419 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_33_in_channel421 = new BitSet(new long[]{0x0000000000000002L});
}
