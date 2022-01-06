// #Sireum
/*
 Copyright (c) 2017-2022, Robby, Kansas State University
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

// This file is auto-generated from SireumAntlr3.g

package org.sireum.parser


import org.sireum._
import org.sireum.U32._
import org.sireum.U64._
import org.sireum.conversions.U32.toC

object SireumGrammar {
  @range(min = 0, max = 16) class State
}

import SireumGrammar.State
import SireumGrammar.State._

object SireumGrammarParser {

  @datatype class Result(val kind: Result.Kind.Type, val tree: ParseTree, val newIndex: Z) {
    def leaf: ParseTree.Leaf = {
      return tree.asInstanceOf[ParseTree.Leaf]
    }
  }

  object Result {

    @enum object Kind {
      'Normal
      'LexicalError
      'GrammaticalError
    }

    @strictpure def create(tree: ParseTree, newIndex: Z): Result =
      Result(Result.Kind.Normal, tree, newIndex)

    @strictpure def error(isLexical: B, index: Z): Result =
      Result(if (isLexical) Result.Kind.LexicalError else Result.Kind.GrammaticalError, errorLeaf, index)

  }

  @record class Context(val ruleName: String,
                        val ruleType: U32,
                        val accepting: IS[State, B],
                        var state: State,
                        var resOpt: Option[Result],
                        var j: Z,
                        var max: Z,
                        var initial: B,
                        var trees: ISZ[ParseTree],
                        var found: B,
                        var failIndex: Z,
                        var isLexical: B) {

    def update(newState: State): Unit = {
      initial = F
      state = newState
      if (accepting(state)) {
        resOpt = Some(Result.create(ParseTree.Node(trees, ruleName, ruleType), j))
      }
    }
  }

  object Context {
    @pure def create(ruleName: String, ruleType: U32, accepts: ISZ[State], i: Z): Context = {
      val accepting = MS.create[State, B](17, F)
      for (accept <- accepts) {
        accepting(accept) = T
      }
      return Context(
        ruleName = ruleName,
        ruleType = ruleType,
        accepting = accepting.toIS,
        state = state"0",
        resOpt = None(),
        trees = ISZ[ParseTree](),
        j = i,
        max = i,
        initial = T,
        found = F,
        failIndex = 0,
        isLexical = F
      )
    }
  }

  @record class LContext(val accepting: IS[State, B], var state: State, var j: Z, var afterAcceptIndex: Z) {
    def update(newState: State): Unit = {
      state = newState
      if (accepting(state)) {
        afterAcceptIndex = j + 1
      }
    }
  }

  object LContext {
    @pure def create(accepts: ISZ[State], i: Z): LContext = {
      val accepting = MS.create[State, B](17, F)
      for (accept <- accepts) {
        accepting(accept) = T
      }
      return LContext(accepting = accepting.toIS, state = state"0", j = i, afterAcceptIndex = -1)
    }
  }

  val kind: String = "SireumGrammarParser"

  val minChar: C = '\u0000'
  val maxChar: C = toC(u32"0x0010FFFF")

  val T_AEB64436: U32 = u32"0xAEB64436" /* "grammar" */
  val T_36F2899D: U32 = u32"0x36F2899D" /* ";" */
  val T_ED16D169: U32 = u32"0xED16D169" /* "options" */
  val T_FDCE65E5: U32 = u32"0xFDCE65E5" /* "{" */
  val T_5BF60471: U32 = u32"0x5BF60471" /* "}" */
  val T_EF954474: U32 = u32"0xEF954474" /* "=" */
  val T_763C38BE: U32 = u32"0x763C38BE" /* ":" */
  val T_687111E8: U32 = u32"0x687111E8" /* "|" */
  val T_072BDD2B: U32 = u32"0x072BDD2B" /* "fragment" */
  val T_71F6371D: U32 = u32"0x71F6371D" /* "(" */
  val T_B9401340: U32 = u32"0xB9401340" /* ")" */
  val T_D827FEB7: U32 = u32"0xD827FEB7" /* "?" */
  val T_82283B4B: U32 = u32"0x82283B4B" /* "*" */
  val T_797D7BC8: U32 = u32"0x797D7BC8" /* "+" */
  val T_AAB7E55C: U32 = u32"0xAAB7E55C" /* "~" */
  val T_3A15194D: U32 = u32"0x3A15194D" /* ".." */
  val T_6890427A: U32 = u32"0x6890427A" /* "." */
  val T_46562B21: U32 = u32"0x46562B21" /* "$channel" */
  val T_CHAR: U32 = u32"0xE95F063A"
  val T_STRING: U32 = u32"0xA7CF0FE0"
  val T_INT: U32 = u32"0x589C233C"
  val T_LID: U32 = u32"0x8E18F45B"
  val T_PID: U32 = u32"0xD2EDBEA1"
  val T_PHEADER: U32 = u32"0xEDD2348C"
  val T_LHEADER: U32 = u32"0x2322FC01"
  val T_COMMENT: U32 = u32"0x486B464F"
  val T_WS: U32 = u32"0x0E3F5D1E"
  val T_grammarDef: U32 = u32"0x49D573EC"
  val T_optionsSpec: U32 = u32"0x5A3A1CB5"
  val T_option: U32 = u32"0x47F1F331"
  val T_optionValue: U32 = u32"0xED8E0DA8"
  val T_parserRule: U32 = u32"0x4AF0B412"
  val T_lexerRule: U32 = u32"0x9E30C465"
  val T_block: U32 = u32"0xAA25218B"
  val T_alt: U32 = u32"0xB817E927"
  val T_element: U32 = u32"0x022B2C72"
  val T_atom: U32 = u32"0xBF749739"
  val T_not: U32 = u32"0x94BF4010"
  val T_range: U32 = u32"0x821FF55C"
  val T_terminal: U32 = u32"0xC926557D"
  val T_id: U32 = u32"0x92391AB1"
  val T_channel: U32 = u32"0x239B7220"

  val errorLeaf: ParseTree.Leaf = ParseTree.Leaf("", "<ERROR>", u32"0xE3CDEDDA", F, None())
  val eofLeaf: ParseTree.Leaf = ParseTree.Leaf("", "EOF", u32"0xFC5CB374", F, None())

  def parse(uriOpt: Option[String], input: String, reporter: message.Reporter): Option[ParseTree.Result] = {
    val docInfo = message.DocInfo.create(uriOpt, input)
    val tokens = lex(input, docInfo, T, T, reporter)
    if (reporter.hasError) {
      return None()
    }
    val r = SireumGrammarParser(tokens).parseGrammarDef(0)
    r.kind match {
      case Result.Kind.Normal => return Some(ParseTree.Result(r.tree, docInfo))
      case Result.Kind.LexicalError =>
        reporter.error(Some(message.PosInfo(docInfo, offsetLength(r.newIndex, 1))), kind, s"Could not recognize token")
        return None()
      case Result.Kind.GrammaticalError =>
        val idx: Z = if (r.newIndex < 0) -r.newIndex else r.newIndex
        if (idx < tokens.size) {
          reporter.error(tokens(idx).posOpt, kind, s"Could not parse token: ${tokens(idx).text}")
        } else {
          reporter.error(tokens(idx - 1).posOpt, kind, "Expecting more input but reached the end")
        }
        return None()
    }
  }

  def lex(input: String, docInfo: message.DocInfo, skipHidden: B, stopAtError: B,
          reporter: message.Reporter): ISZ[ParseTree.Leaf] = {
    return SireumGrammarLexer(input, docInfo).tokenizeAll(skipHidden, stopAtError, reporter)
  }

  @strictpure def offsetLength(offset: Z, length: Z): U64 =
    (conversions.Z.toU64(offset) << u64"32") | (conversions.Z.toU64(length) & u64"0xFFFFFFFF")

}

import SireumGrammarParser._

@datatype class SireumGrammarParser(tokens: ISZ[ParseTree.Leaf]) {

  @pure def parseGrammarDef(i: Z): Result = {
    val ctx = Context.create("grammarDef", u32"0x49D573EC" /* grammarDef */, ISZ(state"8"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xAEB64436" /* "grammar" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          val n_id = predictId(ctx.j)
          if (n_id > 0 && parseIdH(ctx, state"2")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x36F2899D" /* ";" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"3")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"3" =>
          ctx.found = F
          val n_optionsSpec = predictOptionsSpec(ctx.j)
          val n_parserRule = predictParserRule(ctx.j)
          val n_lexerRule = predictLexerRule(ctx.j)
          for (n <- 2 to 1 by -1 if !ctx.found) {
            if (n_optionsSpec == n && parseOptionsSpecH(ctx, state"4")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
            if (!ctx.found && n_parserRule == n && parseParserRuleH(ctx, state"6")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
            if (!ctx.found && n_lexerRule == n && parseLexerRuleH(ctx, state"7")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
          }
          tokens(ctx.j).tipe match {
            case u32"0xEDD2348C" /* PHEADER */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"5")
              ctx.found = T
            case u32"0x2322FC01" /* LHEADER */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"6")
              ctx.found = T
            case u32"0xFC5CB374" /* EOF */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"8")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"4" =>
          ctx.found = F
          val n_parserRule = predictParserRule(ctx.j)
          val n_lexerRule = predictLexerRule(ctx.j)
          for (n <- 2 to 1 by -1 if !ctx.found) {
            if (n_parserRule == n && parseParserRuleH(ctx, state"6")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
            if (!ctx.found && n_lexerRule == n && parseLexerRuleH(ctx, state"7")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
          }
          tokens(ctx.j).tipe match {
            case u32"0xEDD2348C" /* PHEADER */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"5")
              ctx.found = T
            case u32"0x2322FC01" /* LHEADER */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"6")
              ctx.found = T
            case u32"0xFC5CB374" /* EOF */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"8")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"5" =>
          ctx.found = F
          val n_parserRule = predictParserRule(ctx.j)
          val n_lexerRule = predictLexerRule(ctx.j)
          for (n <- 2 to 1 by -1 if !ctx.found) {
            if (n_parserRule == n && parseParserRuleH(ctx, state"6")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
            if (!ctx.found && n_lexerRule == n && parseLexerRuleH(ctx, state"7")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
          }
          tokens(ctx.j).tipe match {
            case u32"0x2322FC01" /* LHEADER */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"6")
              ctx.found = T
            case u32"0xFC5CB374" /* EOF */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"8")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"6" =>
          ctx.found = F
          val n_parserRule = predictParserRule(ctx.j)
          val n_lexerRule = predictLexerRule(ctx.j)
          for (n <- 2 to 1 by -1 if !ctx.found) {
            if (n_parserRule == n && parseParserRuleH(ctx, state"6")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
            if (!ctx.found && n_lexerRule == n && parseLexerRuleH(ctx, state"7")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
          }
          tokens(ctx.j).tipe match {
            case u32"0xFC5CB374" /* EOF */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"8")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"7" =>
          ctx.found = F
          val n_lexerRule = predictLexerRule(ctx.j)
          if (n_lexerRule > 0 && parseLexerRuleH(ctx, state"7")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          tokens(ctx.j).tipe match {
            case u32"0xFC5CB374" /* EOF */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"8")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"8" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseOptionsSpec(i: Z): Result = {
    val ctx = Context.create("optionsSpec", u32"0x5A3A1CB5" /* optionsSpec */, ISZ(state"4"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xED16D169" /* "options" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xFDCE65E5" /* "{" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" =>
          ctx.found = F
          val n_option = predictOption(ctx.j)
          if (n_option > 0 && parseOptionH(ctx, state"3")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"3" =>
          ctx.found = F
          val n_option = predictOption(ctx.j)
          if (n_option > 0 && parseOptionH(ctx, state"3")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          tokens(ctx.j).tipe match {
            case u32"0x5BF60471" /* "}" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"4")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"4" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseOption(i: Z): Result = {
    val ctx = Context.create("option", u32"0x47F1F331" /* option */, ISZ(state"4"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          val n_id = predictId(ctx.j)
          if (n_id > 0 && parseIdH(ctx, state"1")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xEF954474" /* "=" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" =>
          ctx.found = F
          val n_optionValue = predictOptionValue(ctx.j)
          if (n_optionValue > 0 && parseOptionValueH(ctx, state"3")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"3" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x36F2899D" /* ";" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"4")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"4" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseOptionValue(i: Z): Result = {
    val ctx = Context.create("optionValue", u32"0xED8E0DA8" /* optionValue */, ISZ(state"1"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          val n_id = predictId(ctx.j)
          if (n_id > 0 && parseIdH(ctx, state"1")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          tokens(ctx.j).tipe match {
            case u32"0x589C233C" /* INT */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseParserRule(i: Z): Result = {
    val ctx = Context.create("parserRule", u32"0x4AF0B412" /* parserRule */, ISZ(state"5"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xD2EDBEA1" /* PID */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x763C38BE" /* ":" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" =>
          ctx.found = F
          val n_alt = predictAlt(ctx.j)
          if (n_alt > 0 && parseAltH(ctx, state"3")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"3" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x687111E8" /* "|" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"4")
              ctx.found = T
            case u32"0x36F2899D" /* ";" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"5")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"4" =>
          ctx.found = F
          val n_alt = predictAlt(ctx.j)
          if (n_alt > 0 && parseAltH(ctx, state"3")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"5" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseLexerRule(i: Z): Result = {
    val ctx = Context.create("lexerRule", u32"0x9E30C465" /* lexerRule */, ISZ(state"7"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x072BDD2B" /* "fragment" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case u32"0x8E18F45B" /* LID */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x8E18F45B" /* LID */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x763C38BE" /* ":" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"3")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"3" =>
          ctx.found = F
          val n_alt = predictAlt(ctx.j)
          if (n_alt > 0 && parseAltH(ctx, state"4")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"4" =>
          ctx.found = F
          val n_channel = predictChannel(ctx.j)
          if (n_channel > 0 && parseChannelH(ctx, state"8")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          tokens(ctx.j).tipe match {
            case u32"0x687111E8" /* "|" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"5")
              ctx.found = T
            case u32"0x36F2899D" /* ";" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"7")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"5" =>
          ctx.found = F
          val n_alt = predictAlt(ctx.j)
          if (n_alt > 0 && parseAltH(ctx, state"6")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"6" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x687111E8" /* "|" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"5")
              ctx.found = T
            case u32"0x36F2899D" /* ";" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"7")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"7" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case state"8" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x687111E8" /* "|" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"9")
              ctx.found = T
            case u32"0x36F2899D" /* ";" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"7")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"9" =>
          ctx.found = F
          val n_alt = predictAlt(ctx.j)
          if (n_alt > 0 && parseAltH(ctx, state"10")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"10" =>
          ctx.found = F
          val n_channel = predictChannel(ctx.j)
          if (n_channel > 0 && parseChannelH(ctx, state"8")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseBlock(i: Z): Result = {
    val ctx = Context.create("block", u32"0xAA25218B" /* block */, ISZ(state"4"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x71F6371D" /* "(" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          val n_alt = predictAlt(ctx.j)
          if (n_alt > 0 && parseAltH(ctx, state"2")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x687111E8" /* "|" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"3")
              ctx.found = T
            case u32"0xB9401340" /* ")" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"4")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"3" =>
          ctx.found = F
          val n_alt = predictAlt(ctx.j)
          if (n_alt > 0 && parseAltH(ctx, state"2")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"4" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseAlt(i: Z): Result = {
    val ctx = Context.create("alt", u32"0xB817E927" /* alt */, ISZ(state"1"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          val n_element = predictElement(ctx.j)
          if (n_element > 0 && parseElementH(ctx, state"1")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          val n_element = predictElement(ctx.j)
          if (n_element > 0 && parseElementH(ctx, state"1")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseElement(i: Z): Result = {
    val ctx = Context.create("element", u32"0x022B2C72" /* element */, ISZ(state"1", state"2"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          val n_atom = predictAtom(ctx.j)
          val n_block = predictBlock(ctx.j)
          for (n <- 2 to 1 by -1 if !ctx.found) {
            if (n_atom == n && parseAtomH(ctx, state"1")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
            if (!ctx.found && n_block == n && parseBlockH(ctx, state"1")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xD827FEB7" /* "?" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case u32"0x82283B4B" /* "*" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case u32"0x797D7BC8" /* "+" */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseAtom(i: Z): Result = {
    val ctx = Context.create("atom", u32"0xBF749739" /* atom */, ISZ(state"1"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          val n_range = predictRange(ctx.j)
          val n_terminal = predictTerminal(ctx.j)
          val n_not = predictNot(ctx.j)
          for (n <- 2 to 1 by -1 if !ctx.found) {
            if (n_range == n && parseRangeH(ctx, state"1")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
            if (!ctx.found && n_terminal == n && parseTerminalH(ctx, state"1")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
            if (!ctx.found && n_not == n && parseNotH(ctx, state"1")) {
              return Result.error(ctx.isLexical, ctx.failIndex)
            }
          }
          tokens(ctx.j).tipe match {
            case u32"0xD2EDBEA1" /* PID */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseNot(i: Z): Result = {
    val ctx = Context.create("not", u32"0x94BF4010" /* not */, ISZ(state"2"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xAAB7E55C" /* "~" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          val n_block = predictBlock(ctx.j)
          if (n_block > 0 && parseBlockH(ctx, state"2")) {
            return Result.error(ctx.isLexical, ctx.failIndex)
          }
          tokens(ctx.j).tipe match {
            case u32"0xE95F063A" /* CHAR */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case u32"0xA7CF0FE0" /* STRING */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseRange(i: Z): Result = {
    val ctx = Context.create("range", u32"0x821FF55C" /* range */, ISZ(state"3"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xE95F063A" /* CHAR */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x3A15194D" /* ".." */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xE95F063A" /* CHAR */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"3")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"3" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseTerminal(i: Z): Result = {
    val ctx = Context.create("terminal", u32"0xC926557D" /* terminal */, ISZ(state"1"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x8E18F45B" /* LID */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case u32"0xE95F063A" /* CHAR */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case u32"0xA7CF0FE0" /* STRING */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case u32"0x6890427A" /* "." */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseId(i: Z): Result = {
    val ctx = Context.create("id", u32"0x92391AB1" /* id */, ISZ(state"1"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x8E18F45B" /* LID */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case u32"0xD2EDBEA1" /* PID */ if !ctx.found =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  @pure def parseChannel(i: Z): Result = {
    val ctx = Context.create("channel", u32"0x239B7220" /* channel */, ISZ(state"6"), i)

    while (ctx.j < tokens.size) {
      ctx.state match {
        case state"0" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xFDCE65E5" /* "{" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"1")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"1" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x46562B21" /* "$channel" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"2")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"2" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0xEF954474" /* "=" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"3")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"3" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x8E18F45B" /* LID */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"4")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"4" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x36F2899D" /* ";" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"5")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"5" =>
          ctx.found = F
          tokens(ctx.j).tipe match {
            case u32"0x5BF60471" /* "}" */ =>
              ctx.trees = ctx.trees :+ tokens(ctx.j)
              ctx.j = ctx.j + 1
              ctx.update(state"6")
              ctx.found = T
            case _ =>
          }
          if (!ctx.found) {
            return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
          }
        case state"6" => return retVal(ctx.max, ctx.resOpt, ctx.initial, T)
        case _ => halt("Infeasible")
      }
      if (ctx.max < ctx.j) {
        ctx.max = ctx.j
      }
    }

    return retVal(ctx.j, ctx.resOpt, ctx.initial, T)
  }

  def parseIdH(ctx: Context, nextState: State): B = {
    val r = parseId(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseOptionsSpecH(ctx: Context, nextState: State): B = {
    val r = parseOptionsSpec(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseParserRuleH(ctx: Context, nextState: State): B = {
    val r = parseParserRule(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseLexerRuleH(ctx: Context, nextState: State): B = {
    val r = parseLexerRule(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseOptionH(ctx: Context, nextState: State): B = {
    val r = parseOption(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseOptionValueH(ctx: Context, nextState: State): B = {
    val r = parseOptionValue(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseAltH(ctx: Context, nextState: State): B = {
    val r = parseAlt(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseChannelH(ctx: Context, nextState: State): B = {
    val r = parseChannel(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseElementH(ctx: Context, nextState: State): B = {
    val r = parseElement(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseAtomH(ctx: Context, nextState: State): B = {
    val r = parseAtom(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseBlockH(ctx: Context, nextState: State): B = {
    val r = parseBlock(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseRangeH(ctx: Context, nextState: State): B = {
    val r = parseRange(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseTerminalH(ctx: Context, nextState: State): B = {
    val r = parseTerminal(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  def parseNotH(ctx: Context, nextState: State): B = {
    val r = parseNot(ctx.j)
    r.kind match {
      case Result.Kind.Normal =>
        ctx.trees = ctx.trees :+ r.tree
        ctx.j = r.newIndex
        ctx.update(nextState)
        ctx.found = T
      case Result.Kind.LexicalError =>
        ctx.failIndex = r.newIndex
        ctx.isLexical = T
        return T
      case Result.Kind.GrammaticalError =>
        val index = r.newIndex
        if (index < 0) {
          ctx.failIndex = index
          return T
        } else if (ctx.max < index) {
          ctx.max = index
        }
    }
    return F
  }

  @pure def predictParserRule(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xD2EDBEA1" /* PID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x763C38BE" /* ":" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictAtom(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xE95F063A" /* CHAR */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x3A15194D" /* ".." */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0x8E18F45B" /* LID */ => return 1
      case u32"0xA7CF0FE0" /* STRING */ => return 1
      case u32"0x6890427A" /* "." */ => return 1
      case u32"0xAAB7E55C" /* "~" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case _ =>
          }
        }

      case u32"0xD2EDBEA1" /* PID */ => return 1
      case _ =>
    }
    return 0
  }

  @pure def predictOptionsSpec(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xED16D169" /* "options" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xFDCE65E5" /* "{" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictAlt(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xE95F063A" /* CHAR */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x3A15194D" /* ".." */ => return 2
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x6890427A" /* "." */ => return 2
            case u32"0xAAB7E55C" /* "~" */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0x8E18F45B" /* LID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x6890427A" /* "." */ => return 2
            case u32"0xAAB7E55C" /* "~" */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0xA7CF0FE0" /* STRING */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x6890427A" /* "." */ => return 2
            case u32"0xAAB7E55C" /* "~" */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0x6890427A" /* "." */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x6890427A" /* "." */ => return 2
            case u32"0xAAB7E55C" /* "~" */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0xAAB7E55C" /* "~" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case _ =>
          }
        }

      case u32"0xD2EDBEA1" /* PID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x6890427A" /* "." */ => return 2
            case u32"0xAAB7E55C" /* "~" */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0x71F6371D" /* "(" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x6890427A" /* "." */ => return 2
            case u32"0xAAB7E55C" /* "~" */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictTerminal(j: Z): Z = {
    tokens(j).tipe match {
      case u32"0x8E18F45B" /* LID */ => return 1
      case u32"0xE95F063A" /* CHAR */ => return 1
      case u32"0xA7CF0FE0" /* STRING */ => return 1
      case u32"0x6890427A" /* "." */ => return 1
      case _ =>
    }
    return 0
  }

  @pure def predictLexerRule(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0x072BDD2B" /* "fragment" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x8E18F45B" /* LID */ => return 2
            case _ =>
          }
        }

      case u32"0x8E18F45B" /* LID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x763C38BE" /* ":" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictElement(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xE95F063A" /* CHAR */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x3A15194D" /* ".." */ => return 2
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0x8E18F45B" /* LID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0xA7CF0FE0" /* STRING */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0x6890427A" /* "." */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0xAAB7E55C" /* "~" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case _ =>
          }
        }

      case u32"0xD2EDBEA1" /* PID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xD827FEB7" /* "?" */ => return 2
            case u32"0x82283B4B" /* "*" */ => return 2
            case u32"0x797D7BC8" /* "+" */ => return 2
            case _ =>
          }
        }
        return 1
      case u32"0x71F6371D" /* "(" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x6890427A" /* "." */ => return 2
            case u32"0xAAB7E55C" /* "~" */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictChannel(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xFDCE65E5" /* "{" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x46562B21" /* "$channel" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictOption(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0x8E18F45B" /* LID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xEF954474" /* "=" */ => return 2
            case _ =>
          }
        }

      case u32"0xD2EDBEA1" /* PID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xEF954474" /* "=" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictOptionValue(j: Z): Z = {
    tokens(j).tipe match {
      case u32"0x8E18F45B" /* LID */ => return 1
      case u32"0xD2EDBEA1" /* PID */ => return 1
      case u32"0x589C233C" /* INT */ => return 1
      case _ =>
    }
    return 0
  }

  @pure def predictBlock(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0x71F6371D" /* "(" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x6890427A" /* "." */ => return 2
            case u32"0xAAB7E55C" /* "~" */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictGrammarDef(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xAEB64436" /* "grammar" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x8E18F45B" /* LID */ => return 2
            case u32"0xD2EDBEA1" /* PID */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictRange(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xE95F063A" /* CHAR */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x3A15194D" /* ".." */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  @pure def predictId(j: Z): Z = {
    tokens(j).tipe match {
      case u32"0x8E18F45B" /* LID */ => return 1
      case u32"0xD2EDBEA1" /* PID */ => return 1
      case _ =>
    }
    return 0
  }

  @pure def predictNot(j: Z): Z = {
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xAAB7E55C" /* "~" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => return 2
            case u32"0xA7CF0FE0" /* STRING */ => return 2
            case u32"0x71F6371D" /* "(" */ => return 2
            case _ =>
          }
        }

      case _ =>
    }
    return 0
  }

  def retVal(n: Z, resOpt: Option[Result], initial: B, noBacktrack: B): Result = {
    resOpt match {
      case Some(res) => return res
      case _ => return Result.error(F, if (noBacktrack && !initial) -n else n)
    }
  }

  @pure def posOpts(docInfo: message.DocInfo,
                    posOpt1: Option[message.Position],
                    posOpt2: Option[message.Position]): Option[message.Position] = {
    val pos1 = posOpt1.get
    val pos2 = posOpt2.get
    return Some(message.PosInfo(docInfo, offsetLength(pos1.offset,
      pos2.offset + pos2.length - pos1.offset)))
  }

}

@datatype class SireumGrammarLexer(input: String, docInfo: message.DocInfo) {

  val cis: ISZ[C] = conversions.String.toCis(input)

  def tokenizeAll(skipHidden: B, stopAtError: B, reporter: message.Reporter): ISZ[ParseTree.Leaf] = {
    var i: Z = 0
    var r = ISZ[ParseTree.Leaf]()
    while (i < cis.size) {
      val result = tokenize(i)
      result match {
        case Result(Result.Kind.Normal, token: ParseTree.Leaf, _) =>
          i = result.newIndex
          if (!(skipHidden && token.isHidden)) {
            r = r :+ token
          }
        case _ =>
          val posOpt: Option[message.Position] = Some(message.PosInfo(docInfo, offsetLength(i, 1)))
          reporter.error(posOpt, kind, s"Could not recognize token")
          if (stopAtError) {
            return r
          }
          r = r :+ errorLeaf(text = conversions.String.fromCis(ISZ(cis(i))), posOpt = posOpt)
          i = i + 1
      }
    }
    r = r :+ eofLeaf
    return r
  }

  @pure def tokenize(i: Z): Result = {
    val r = MBox(Result.error(T, i))
    updateToken(r, lex_grammar(i))
    updateToken(r, lex_u003B(i))
    updateToken(r, lex_options(i))
    updateToken(r, lex_u007B(i))
    updateToken(r, lex_u007D(i))
    updateToken(r, lex_u003D(i))
    updateToken(r, lex_u003A(i))
    updateToken(r, lex_u007C(i))
    updateToken(r, lex_fragment(i))
    updateToken(r, lex_u0028(i))
    updateToken(r, lex_u0029(i))
    updateToken(r, lex_u003F(i))
    updateToken(r, lex_u002A(i))
    updateToken(r, lex_u002B(i))
    updateToken(r, lex_u007E(i))
    updateToken(r, lex_u002Eu002E(i))
    updateToken(r, lex_u002E(i))
    updateToken(r, lex__channel(i))
    updateToken(r, lex_CHAR(i))
    updateToken(r, lex_STRING(i))
    updateToken(r, lex_INT(i))
    updateToken(r, lex_LID(i))
    updateToken(r, lex_PID(i))
    updateToken(r, lex_PHEADER(i))
    updateToken(r, lex_LHEADER(i))
    updateToken(r, lex_COMMENT(i))
    updateToken(r, lex_WS(i))
    return r.value
  }

  def updateToken(r: MBox[Result], rOpt: Option[Result]): Unit = {
    rOpt match {
      case Some(newR) if newR.newIndex > r.value.newIndex => r.value = newR
      case _ =>
    }
  }

  @pure def lit_grammar(i: Z): Z = {
    if (i + 7 >= cis.size) {
      return -1
    }
    if (cis(i) === 'g' && cis(i + 1) === 'r' && cis(i + 2) === 'a' && cis(i + 3) === 'm' && cis(i + 4) === 'm' && cis(i + 5) === 'a' && cis(i + 6) === 'r') {
      return i + 7
    }
    return -1
  }

  @strictpure def lex_grammar(index: Z): Option[Result] =
     lexH(index, lit_grammar(index), """'grammar'""", u32"0xAEB64436" /* "grammar" */, F)

  @pure def lit_u003B(i: Z): Z = {
    if (i < cis.size && cis(i) === ';') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u003B(index: Z): Option[Result] =
     lexH(index, lit_u003B(index), """';'""", u32"0x36F2899D" /* ";" */, F)

  @pure def lit_options(i: Z): Z = {
    if (i + 7 >= cis.size) {
      return -1
    }
    if (cis(i) === 'o' && cis(i + 1) === 'p' && cis(i + 2) === 't' && cis(i + 3) === 'i' && cis(i + 4) === 'o' && cis(i + 5) === 'n' && cis(i + 6) === 's') {
      return i + 7
    }
    return -1
  }

  @strictpure def lex_options(index: Z): Option[Result] =
     lexH(index, lit_options(index), """'options'""", u32"0xED16D169" /* "options" */, F)

  @pure def lit_u007B(i: Z): Z = {
    if (i < cis.size && cis(i) === '{') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u007B(index: Z): Option[Result] =
     lexH(index, lit_u007B(index), """'{'""", u32"0xFDCE65E5" /* "{" */, F)

  @pure def lit_u007D(i: Z): Z = {
    if (i < cis.size && cis(i) === '}') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u007D(index: Z): Option[Result] =
     lexH(index, lit_u007D(index), """'}'""", u32"0x5BF60471" /* "}" */, F)

  @pure def lit_u003D(i: Z): Z = {
    if (i < cis.size && cis(i) === '=') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u003D(index: Z): Option[Result] =
     lexH(index, lit_u003D(index), """'='""", u32"0xEF954474" /* "=" */, F)

  @pure def lit_u003A(i: Z): Z = {
    if (i < cis.size && cis(i) === ':') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u003A(index: Z): Option[Result] =
     lexH(index, lit_u003A(index), """':'""", u32"0x763C38BE" /* ":" */, F)

  @pure def lit_u007C(i: Z): Z = {
    if (i < cis.size && cis(i) === '|') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u007C(index: Z): Option[Result] =
     lexH(index, lit_u007C(index), """'|'""", u32"0x687111E8" /* "|" */, F)

  @pure def lit_fragment(i: Z): Z = {
    if (i + 8 >= cis.size) {
      return -1
    }
    if (cis(i) === 'f' && cis(i + 1) === 'r' && cis(i + 2) === 'a' && cis(i + 3) === 'g' && cis(i + 4) === 'm' && cis(i + 5) === 'e' && cis(i + 6) === 'n' && cis(i + 7) === 't') {
      return i + 8
    }
    return -1
  }

  @strictpure def lex_fragment(index: Z): Option[Result] =
     lexH(index, lit_fragment(index), """'fragment'""", u32"0x072BDD2B" /* "fragment" */, F)

  @pure def lit_u0028(i: Z): Z = {
    if (i < cis.size && cis(i) === '(') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u0028(index: Z): Option[Result] =
     lexH(index, lit_u0028(index), """'('""", u32"0x71F6371D" /* "(" */, F)

  @pure def lit_u0029(i: Z): Z = {
    if (i < cis.size && cis(i) === ')') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u0029(index: Z): Option[Result] =
     lexH(index, lit_u0029(index), """')'""", u32"0xB9401340" /* ")" */, F)

  @pure def lit_u003F(i: Z): Z = {
    if (i < cis.size && cis(i) === '?') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u003F(index: Z): Option[Result] =
     lexH(index, lit_u003F(index), """'?'""", u32"0xD827FEB7" /* "?" */, F)

  @pure def lit_u002A(i: Z): Z = {
    if (i < cis.size && cis(i) === '*') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u002A(index: Z): Option[Result] =
     lexH(index, lit_u002A(index), """'*'""", u32"0x82283B4B" /* "*" */, F)

  @pure def lit_u002B(i: Z): Z = {
    if (i < cis.size && cis(i) === '+') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u002B(index: Z): Option[Result] =
     lexH(index, lit_u002B(index), """'+'""", u32"0x797D7BC8" /* "+" */, F)

  @pure def lit_u007E(i: Z): Z = {
    if (i < cis.size && cis(i) === '~') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u007E(index: Z): Option[Result] =
     lexH(index, lit_u007E(index), """'~'""", u32"0xAAB7E55C" /* "~" */, F)

  @pure def lit_u002Eu002E(i: Z): Z = {
    if (i + 2 >= cis.size) {
      return -1
    }
    if (cis(i) === '.' && cis(i + 1) === '.') {
      return i + 2
    }
    return -1
  }

  @strictpure def lex_u002Eu002E(index: Z): Option[Result] =
     lexH(index, lit_u002Eu002E(index), """'..'""", u32"0x3A15194D" /* ".." */, F)

  @pure def lit_u002E(i: Z): Z = {
    if (i < cis.size && cis(i) === '.') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u002E(index: Z): Option[Result] =
     lexH(index, lit_u002E(index), """'.'""", u32"0x6890427A" /* "." */, F)

  @pure def lit__channel(i: Z): Z = {
    if (i + 8 >= cis.size) {
      return -1
    }
    if (cis(i) === '$' && cis(i + 1) === 'c' && cis(i + 2) === 'h' && cis(i + 3) === 'a' && cis(i + 4) === 'n' && cis(i + 5) === 'n' && cis(i + 6) === 'e' && cis(i + 7) === 'l') {
      return i + 8
    }
    return -1
  }

  @strictpure def lex__channel(index: Z): Option[Result] =
     lexH(index, lit__channel(index), """'$channel'""", u32"0x46562B21" /* "$channel" */, F)

  @pure def dfa_CHAR(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"3"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '\'') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= '&' || '(' <= c && c <= '[' || ']' <= c && c <= maxChar) {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === '\\')) {
            ctx.update(state"4")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"2" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '\'') {
            ctx.update(state"3")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"3" => return ctx.afterAcceptIndex
        case state"4" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '"' || c === '\'' || c === '\\' || c === 'b' || c === 'f' || c === 'n' || c === 'r' || c === 't') {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === 'u')) {
            ctx.update(state"5")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"5" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"6")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"6" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"7")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"7" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"8")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"8" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"2")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_CHAR(index: Z): Option[Result] =
     lexH(index, dfa_CHAR(index), """CHAR""", u32"0xE95F063A", F)

  @pure def dfa_STRING(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"4"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '\'') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= '&' || '(' <= c && c <= '[' || ']' <= c && c <= maxChar) {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === '\\')) {
            ctx.update(state"10")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"2" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= '&' || '(' <= c && c <= '[' || ']' <= c && c <= maxChar) {
            ctx.update(state"3")
            found = T
          }
          if (!found && (c === '\\')) {
            ctx.update(state"5")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"3" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= '&' || '(' <= c && c <= '[' || ']' <= c && c <= maxChar) {
            ctx.update(state"3")
            found = T
          }
          if (!found && (c === '\'')) {
            ctx.update(state"4")
            found = T
          }
          if (!found && (c === '\\')) {
            ctx.update(state"5")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"4" => return ctx.afterAcceptIndex
        case state"5" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '"' || c === '\'' || c === '\\' || c === 'b' || c === 'f' || c === 'n' || c === 'r' || c === 't') {
            ctx.update(state"3")
            found = T
          }
          if (!found && (c === 'u')) {
            ctx.update(state"6")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"6" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"7")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"7" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"8")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"8" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"9")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"9" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"3")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"10" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '"' || c === '\'' || c === '\\' || c === 'b' || c === 'f' || c === 'n' || c === 'r' || c === 't') {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === 'u')) {
            ctx.update(state"11")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"11" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"12")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"12" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"13")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"13" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"14")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"14" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            ctx.update(state"2")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_STRING(index: Z): Option[Result] =
     lexH(index, dfa_STRING(index), """STRING""", u32"0xA7CF0FE0", F)

  @pure def dfa_INT(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"1"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if ('1' <= c && c <= '9') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_INT(index: Z): Option[Result] =
     lexH(index, dfa_INT(index), """INT""", u32"0x589C233C", F)

  @pure def dfa_LID(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"1"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if ('A' <= c && c <= 'Z') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'Z' || c === '_' || 'a' <= c && c <= 'z') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_LID(index: Z): Option[Result] =
     lexH(index, dfa_LID(index), """LID""", u32"0x8E18F45B", F)

  @pure def dfa_PID(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"1"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if ('a' <= c && c <= 'z') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'Z' || c === '_' || 'a' <= c && c <= 'z') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_PID(index: Z): Option[Result] =
     lexH(index, dfa_PID(index), """PID""", u32"0xD2EDBEA1", F)

  @pure def dfa_PHEADER(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"9"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '@') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"1")
            found = T
          }
          if (!found && (c === 'h')) {
            ctx.update(state"2")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"2" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'e') {
            ctx.update(state"3")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"3" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'a') {
            ctx.update(state"4")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"4" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'd') {
            ctx.update(state"5")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"5" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'e') {
            ctx.update(state"6")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"6" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'r') {
            ctx.update(state"7")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"7" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"7")
            found = T
          }
          if (!found && (c === '{')) {
            ctx.update(state"8")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"8" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= '|' || '~' <= c && c <= maxChar) {
            ctx.update(state"8")
            found = T
          }
          if (!found && (c === '}')) {
            ctx.update(state"9")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"9" => return ctx.afterAcceptIndex
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_PHEADER(index: Z): Option[Result] =
     lexH(index, dfa_PHEADER(index), """PHEADER""", u32"0xEDD2348C", F)

  @pure def dfa_LHEADER(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"16"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '@') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"1")
            found = T
          }
          if (!found && (c === 'l')) {
            ctx.update(state"2")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"2" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'e') {
            ctx.update(state"3")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"3" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'x') {
            ctx.update(state"4")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"4" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'e') {
            ctx.update(state"5")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"5" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'r') {
            ctx.update(state"6")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"6" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"6")
            found = T
          }
          if (!found && (c === ':')) {
            ctx.update(state"7")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"7" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"7")
            found = T
          }
          if (!found && (c === ':')) {
            ctx.update(state"8")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"8" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"8")
            found = T
          }
          if (!found && (c === 'h')) {
            ctx.update(state"9")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"9" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'e') {
            ctx.update(state"10")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"10" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'a') {
            ctx.update(state"11")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"11" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'd') {
            ctx.update(state"12")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"12" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'e') {
            ctx.update(state"13")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"13" =>
          val c = cis(ctx.j)
          var found = F
          if (c === 'r') {
            ctx.update(state"14")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"14" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"14")
            found = T
          }
          if (!found && (c === '{')) {
            ctx.update(state"15")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"15" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= '|' || '~' <= c && c <= maxChar) {
            ctx.update(state"15")
            found = T
          }
          if (!found && (c === '}')) {
            ctx.update(state"16")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"16" => return ctx.afterAcceptIndex
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_LHEADER(index: Z): Option[Result] =
     lexH(index, dfa_LHEADER(index), """LHEADER""", u32"0x2322FC01", F)

  @pure def dfa_COMMENT(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"5", state"6"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '/') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '*') {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === '/')) {
            ctx.update(state"7")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"2" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= ')' || '+' <= c && c <= maxChar) {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === '*')) {
            ctx.update(state"3")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"3" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= ')' || '+' <= c && c <= '.' || '0' <= c && c <= maxChar) {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === '*')) {
            ctx.update(state"4")
            found = T
          }
          if (!found && (c === '/')) {
            ctx.update(state"6")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"4" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= ')' || '+' <= c && c <= '.' || '0' <= c && c <= maxChar) {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === '*')) {
            ctx.update(state"3")
            found = T
          }
          if (!found && (c === '/')) {
            ctx.update(state"5")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"5" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= ')' || '+' <= c && c <= maxChar) {
            ctx.update(state"2")
            found = T
          }
          if (!found && (c === '*')) {
            ctx.update(state"3")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"6" => return ctx.afterAcceptIndex
        case state"7" =>
          val c = cis(ctx.j)
          var found = F
          if (minChar <= c && c <= '\u0009' || '\u000B' <= c && c <= '\u000C' || '\u000E' <= c && c <= maxChar) {
            ctx.update(state"7")
            found = T
          }
          if (!found && (c === '\u000A')) {
            ctx.update(state"6")
            found = T
          }
          if (!found && (c === '\u000D')) {
            ctx.update(state"8")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"8" =>
          val c = cis(ctx.j)
          var found = F
          if (c === '\u000A') {
            ctx.update(state"6")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_COMMENT(index: Z): Option[Result] =
     lexH(index, dfa_COMMENT(index), """COMMENT""", u32"0x486B464F", T)

  @pure def dfa_WS(i: Z): Z = {
    val ctx = LContext.create(ISZ(state"1"), i)

    while (ctx.j < cis.size) {
      ctx.state match {
        case state"0" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case state"1" =>
          val c = cis(ctx.j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            ctx.update(state"1")
            found = T
          }
          if (!found) {
            return ctx.afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      ctx.j = ctx.j + 1
    }
    return ctx.afterAcceptIndex
  }

  @strictpure def lex_WS(index: Z): Option[Result] =
     lexH(index, dfa_WS(index), """WS""", u32"0x0E3F5D1E", T)

  @pure def hidden(i: Z): Z = {
     var j: Z = -1
     j = dfa_COMMENT(i)
     if (j > 0) {
       return j
     }
     j = dfa_WS(i)
     if (j > 0) {
       return j
     }
     return -1
  }

  @pure def lexH(index: Z, newIndex: Z, name: String, tipe: U32, isHidden: B): Option[Result] = {
    if (newIndex > 0) {
      return Some(Result.create(ParseTree.Leaf(ops.StringOps.substring(cis, index, newIndex),
        name, tipe, isHidden, Some(message.PosInfo(docInfo, offsetLength(index, newIndex - index)))),
        newIndex))
    } else {
      return None()
    }
  }

}