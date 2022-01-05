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

object SireumGrammarParser {

  @datatype class Result(val tree: ParseTree, val newIndex: Z)

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

  def parse(uriOpt: Option[String], input: String, reporter: message.Reporter): Option[ParseTree.Result] = {
    val docInfo = message.DocInfo.create(uriOpt, input)
    val tokens = lex(input, docInfo, T, T, reporter)
    if (reporter.hasError) {
      return None()
    }
    SireumGrammarParser(tokens).parseGrammarDef(0) match {
      case Either.Left(r) => return Some(ParseTree.Result(r.tree, docInfo))
      case Either.Right(r) =>
        val idx: Z = if (r < 0) -r else r
        if (idx < tokens.size) {
          reporter.error(tokens(idx - 1).posOpt, kind, s"Could not parse token: ${tokens(idx).text}")
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

  @pure def posOpts(docInfo: message.DocInfo,
                    posOpt1: Option[message.Position],
                    posOpt2: Option[message.Position]): Option[message.Position] = {
    val pos1 = posOpt1.get
    val pos2 = posOpt2.get
    return Some(message.PosInfo(docInfo, offsetLength(pos1.offset,
      pos2.offset + pos2.length - pos1.offset)))
  }

  @strictpure def offsetLength(offset: Z, length: Z): U64 =
    (conversions.Z.toU64(offset) << u64"32") | (conversions.Z.toU64(length) & u64"0xFFFFFFFF")
}

import SireumGrammarParser._

@datatype class SireumGrammarParser(tokens: ISZ[ParseTree.Leaf]) {

  @pure def parseGrammarDef(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"8" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """grammarDef""", u32"0x49D573EC" /* grammarDef */, None()), j))
    }

    def parseIdH(nextState: U32): B = {
      parseId(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    def parseOptionsSpecH(nextState: U32): B = {
      parseOptionsSpec(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    def parseParserRuleH(nextState: U32): B = {
      parseParserRule(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    def parseLexerRuleH(nextState: U32): B = {
      parseLexerRule(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0xAEB64436" /* "grammar" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          val n_id = predictId(j)
          if (n_id > 0 && parseIdH(u32"2")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" =>
          found = F
          tokens(j).tipe match {
            case u32"0x36F2899D" /* ";" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"3")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"3" =>
          found = F
          val n_optionsSpec = predictOptionsSpec(j)
          val n_parserRule = predictParserRule(j)
          val n_lexerRule = predictLexerRule(j)
          for (n <- 2 to 1 by -1 if !found) {
            if (n_optionsSpec == n && parseOptionsSpecH(u32"4")) {
              return Either.Right(failIndex)
            }
            if (!found && n_parserRule == n && parseParserRuleH(u32"6")) {
              return Either.Right(failIndex)
            }
            if (!found && n_lexerRule == n && parseLexerRuleH(u32"7")) {
              return Either.Right(failIndex)
            }
          }
          tokens(j).tipe match {
            case u32"0xEDD2348C" /* PHEADER */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"5")
              found = T
            case u32"0x2322FC01" /* LHEADER */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"6")
              found = T
            case u32"0xFC5CB374" /* EOF */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"8")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"4" =>
          found = F
          val n_parserRule = predictParserRule(j)
          val n_lexerRule = predictLexerRule(j)
          for (n <- 2 to 1 by -1 if !found) {
            if (n_parserRule == n && parseParserRuleH(u32"6")) {
              return Either.Right(failIndex)
            }
            if (!found && n_lexerRule == n && parseLexerRuleH(u32"7")) {
              return Either.Right(failIndex)
            }
          }
          tokens(j).tipe match {
            case u32"0xEDD2348C" /* PHEADER */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"5")
              found = T
            case u32"0x2322FC01" /* LHEADER */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"6")
              found = T
            case u32"0xFC5CB374" /* EOF */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"8")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"5" =>
          found = F
          val n_parserRule = predictParserRule(j)
          val n_lexerRule = predictLexerRule(j)
          for (n <- 2 to 1 by -1 if !found) {
            if (n_parserRule == n && parseParserRuleH(u32"6")) {
              return Either.Right(failIndex)
            }
            if (!found && n_lexerRule == n && parseLexerRuleH(u32"7")) {
              return Either.Right(failIndex)
            }
          }
          tokens(j).tipe match {
            case u32"0x2322FC01" /* LHEADER */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"6")
              found = T
            case u32"0xFC5CB374" /* EOF */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"8")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"6" =>
          found = F
          val n_parserRule = predictParserRule(j)
          val n_lexerRule = predictLexerRule(j)
          for (n <- 2 to 1 by -1 if !found) {
            if (n_parserRule == n && parseParserRuleH(u32"6")) {
              return Either.Right(failIndex)
            }
            if (!found && n_lexerRule == n && parseLexerRuleH(u32"7")) {
              return Either.Right(failIndex)
            }
          }
          tokens(j).tipe match {
            case u32"0xFC5CB374" /* EOF */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"8")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"7" =>
          found = F
          val n_lexerRule = predictLexerRule(j)
          if (n_lexerRule > 0 && parseLexerRuleH(u32"7")) {
            return Either.Right(failIndex)
          }
          tokens(j).tipe match {
            case u32"0xFC5CB374" /* EOF */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"8")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"8" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseOptionsSpec(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"4" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """optionsSpec""", u32"0x5A3A1CB5" /* optionsSpec */, None()), j))
    }

    def parseOptionH(nextState: U32): B = {
      parseOption(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0xED16D169" /* "options" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          tokens(j).tipe match {
            case u32"0xFDCE65E5" /* "{" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" =>
          found = F
          val n_option = predictOption(j)
          if (n_option > 0 && parseOptionH(u32"3")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"3" =>
          found = F
          val n_option = predictOption(j)
          if (n_option > 0 && parseOptionH(u32"3")) {
            return Either.Right(failIndex)
          }
          tokens(j).tipe match {
            case u32"0x5BF60471" /* "}" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"4")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"4" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseOption(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"4" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """option""", u32"0x47F1F331" /* option */, None()), j))
    }

    def parseIdH(nextState: U32): B = {
      parseId(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    def parseOptionValueH(nextState: U32): B = {
      parseOptionValue(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          val n_id = predictId(j)
          if (n_id > 0 && parseIdH(u32"1")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          tokens(j).tipe match {
            case u32"0xEF954474" /* "=" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" =>
          found = F
          val n_optionValue = predictOptionValue(j)
          if (n_optionValue > 0 && parseOptionValueH(u32"3")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"3" =>
          found = F
          tokens(j).tipe match {
            case u32"0x36F2899D" /* ";" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"4")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"4" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseOptionValue(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """optionValue""", u32"0xED8E0DA8" /* optionValue */, None()), j))
    }

    def parseIdH(nextState: U32): B = {
      parseId(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          val n_id = predictId(j)
          if (n_id > 0 && parseIdH(u32"1")) {
            return Either.Right(failIndex)
          }
          tokens(j).tipe match {
            case u32"0x589C233C" /* INT */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseParserRule(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"5" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """parserRule""", u32"0x4AF0B412" /* parserRule */, None()), j))
    }

    def parseAltH(nextState: U32): B = {
      parseAlt(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0xD2EDBEA1" /* PID */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          tokens(j).tipe match {
            case u32"0x763C38BE" /* ":" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" =>
          found = F
          val n_alt = predictAlt(j)
          if (n_alt > 0 && parseAltH(u32"3")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"3" =>
          found = F
          tokens(j).tipe match {
            case u32"0x687111E8" /* "|" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"4")
              found = T
            case u32"0x36F2899D" /* ";" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"5")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"4" =>
          found = F
          val n_alt = predictAlt(j)
          if (n_alt > 0 && parseAltH(u32"3")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"5" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseLexerRule(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"7" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """lexerRule""", u32"0x9E30C465" /* lexerRule */, None()), j))
    }

    def parseAltH(nextState: U32): B = {
      parseAlt(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    def parseChannelH(nextState: U32): B = {
      parseChannel(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0x072BDD2B" /* "fragment" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case u32"0x8E18F45B" /* LID */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          tokens(j).tipe match {
            case u32"0x8E18F45B" /* LID */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" =>
          found = F
          tokens(j).tipe match {
            case u32"0x763C38BE" /* ":" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"3")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"3" =>
          found = F
          val n_alt = predictAlt(j)
          if (n_alt > 0 && parseAltH(u32"4")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"4" =>
          found = F
          val n_channel = predictChannel(j)
          if (n_channel > 0 && parseChannelH(u32"8")) {
            return Either.Right(failIndex)
          }
          tokens(j).tipe match {
            case u32"0x687111E8" /* "|" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"5")
              found = T
            case u32"0x36F2899D" /* ";" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"7")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"5" =>
          found = F
          val n_alt = predictAlt(j)
          if (n_alt > 0 && parseAltH(u32"6")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"6" =>
          found = F
          tokens(j).tipe match {
            case u32"0x687111E8" /* "|" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"5")
              found = T
            case u32"0x36F2899D" /* ";" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"7")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"7" => return retVal(max, resOpt, initial, T)
        case u32"8" =>
          found = F
          tokens(j).tipe match {
            case u32"0x687111E8" /* "|" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"9")
              found = T
            case u32"0x36F2899D" /* ";" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"7")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"9" =>
          found = F
          val n_alt = predictAlt(j)
          if (n_alt > 0 && parseAltH(u32"10")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"10" =>
          found = F
          val n_channel = predictChannel(j)
          if (n_channel > 0 && parseChannelH(u32"8")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseBlock(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"4" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """block""", u32"0xAA25218B" /* block */, None()), j))
    }

    def parseAltH(nextState: U32): B = {
      parseAlt(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0x71F6371D" /* "(" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          val n_alt = predictAlt(j)
          if (n_alt > 0 && parseAltH(u32"2")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" =>
          found = F
          tokens(j).tipe match {
            case u32"0x687111E8" /* "|" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"3")
              found = T
            case u32"0xB9401340" /* ")" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"4")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"3" =>
          found = F
          val n_alt = predictAlt(j)
          if (n_alt > 0 && parseAltH(u32"2")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"4" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseAlt(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """alt""", u32"0xB817E927" /* alt */, None()), j))
    }

    def parseElementH(nextState: U32): B = {
      parseElement(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          val n_element = predictElement(j)
          if (n_element > 0 && parseElementH(u32"1")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          val n_element = predictElement(j)
          if (n_element > 0 && parseElementH(u32"1")) {
            return Either.Right(failIndex)
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseElement(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case u32"2" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """element""", u32"0x022B2C72" /* element */, None()), j))
    }

    def parseAtomH(nextState: U32): B = {
      parseAtom(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    def parseBlockH(nextState: U32): B = {
      parseBlock(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          val n_atom = predictAtom(j)
          val n_block = predictBlock(j)
          for (n <- 2 to 1 by -1 if !found) {
            if (n_atom == n && parseAtomH(u32"1")) {
              return Either.Right(failIndex)
            }
            if (!found && n_block == n && parseBlockH(u32"1")) {
              return Either.Right(failIndex)
            }
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          tokens(j).tipe match {
            case u32"0xD827FEB7" /* "?" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case u32"0x82283B4B" /* "*" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case u32"0x797D7BC8" /* "+" */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseAtom(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """atom""", u32"0xBF749739" /* atom */, None()), j))
    }

    def parseRangeH(nextState: U32): B = {
      parseRange(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    def parseTerminalH(nextState: U32): B = {
      parseTerminal(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    def parseNotH(nextState: U32): B = {
      parseNot(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          val n_range = predictRange(j)
          val n_terminal = predictTerminal(j)
          val n_not = predictNot(j)
          for (n <- 2 to 1 by -1 if !found) {
            if (n_range == n && parseRangeH(u32"1")) {
              return Either.Right(failIndex)
            }
            if (!found && n_terminal == n && parseTerminalH(u32"1")) {
              return Either.Right(failIndex)
            }
            if (!found && n_not == n && parseNotH(u32"1")) {
              return Either.Right(failIndex)
            }
          }
          tokens(j).tipe match {
            case u32"0xD2EDBEA1" /* PID */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseNot(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"2" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """not""", u32"0x94BF4010" /* not */, None()), j))
    }

    def parseBlockH(nextState: U32): B = {
      parseBlock(j) match {
        case Either.Left(r) =>
          trees = trees :+ r.tree
          j = r.newIndex
          update(nextState)
          found = T
          return F
        case Either.Right(r) =>
          if (r < 0) {
            failIndex = r
            return T
          } else if (max < r) {
            max = r
          }
          return F
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0xAAB7E55C" /* "~" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          val n_block = predictBlock(j)
          if (n_block > 0 && parseBlockH(u32"2")) {
            return Either.Right(failIndex)
          }
          tokens(j).tipe match {
            case u32"0xE95F063A" /* CHAR */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case u32"0xA7CF0FE0" /* STRING */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseRange(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"3" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """range""", u32"0x821FF55C" /* range */, None()), j))
    }


    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0xE95F063A" /* CHAR */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          tokens(j).tipe match {
            case u32"0x3A15194D" /* ".." */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" =>
          found = F
          tokens(j).tipe match {
            case u32"0xE95F063A" /* CHAR */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"3")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"3" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseTerminal(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """terminal""", u32"0xC926557D" /* terminal */, None()), j))
    }


    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0x8E18F45B" /* LID */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case u32"0xE95F063A" /* CHAR */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case u32"0xA7CF0FE0" /* STRING */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case u32"0x6890427A" /* "." */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseId(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """id""", u32"0x92391AB1" /* id */, None()), j))
    }


    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0x8E18F45B" /* LID */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case u32"0xD2EDBEA1" /* PID */ if !found =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def parseChannel(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T
    var found = F
    var failIndex: Z = 0

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"6" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """channel""", u32"0x239B7220" /* channel */, None()), j))
    }


    while (j < tokens.size) {
      state match {
        case u32"0" =>
          found = F
          tokens(j).tipe match {
            case u32"0xFDCE65E5" /* "{" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"1")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"1" =>
          found = F
          tokens(j).tipe match {
            case u32"0x46562B21" /* "$channel" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"2")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"2" =>
          found = F
          tokens(j).tipe match {
            case u32"0xEF954474" /* "=" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"3")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"3" =>
          found = F
          tokens(j).tipe match {
            case u32"0x8E18F45B" /* LID */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"4")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"4" =>
          found = F
          tokens(j).tipe match {
            case u32"0x36F2899D" /* ";" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"5")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"5" =>
          found = F
          tokens(j).tipe match {
            case u32"0x5BF60471" /* "}" */ =>
              trees = trees :+ tokens(j)
              j = j + 1
              update(u32"6")
              found = T
            case _ =>
          }
          if (!found) {
            return retVal(max, resOpt, initial, T)
          }
        case u32"6" => return retVal(max, resOpt, initial, T)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j, resOpt, initial, T)
  }

  @pure def predictParserRule(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xD2EDBEA1" /* PID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x763C38BE" /* ":" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictAtom(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xE95F063A" /* CHAR */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x3A15194D" /* ".." */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0x8E18F45B" /* LID */ => num = 1
      case u32"0xA7CF0FE0" /* STRING */ => num = 1
      case u32"0x6890427A" /* "." */ => num = 1
      case u32"0xAAB7E55C" /* "~" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case _ =>
          }
        }

      case u32"0xD2EDBEA1" /* PID */ => num = 1
      case _ =>
    }
    return num
  }

  @pure def predictOptionsSpec(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xED16D169" /* "options" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xFDCE65E5" /* "{" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictAlt(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xE95F063A" /* CHAR */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x3A15194D" /* ".." */ => num = 2
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x6890427A" /* "." */ => num = 2
            case u32"0xAAB7E55C" /* "~" */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0x8E18F45B" /* LID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x6890427A" /* "." */ => num = 2
            case u32"0xAAB7E55C" /* "~" */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0xA7CF0FE0" /* STRING */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x6890427A" /* "." */ => num = 2
            case u32"0xAAB7E55C" /* "~" */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0x6890427A" /* "." */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x6890427A" /* "." */ => num = 2
            case u32"0xAAB7E55C" /* "~" */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0xAAB7E55C" /* "~" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case _ =>
          }
        }

      case u32"0xD2EDBEA1" /* PID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x6890427A" /* "." */ => num = 2
            case u32"0xAAB7E55C" /* "~" */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0x71F6371D" /* "(" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x6890427A" /* "." */ => num = 2
            case u32"0xAAB7E55C" /* "~" */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictTerminal(j: Z): Z = {
    var num: Z = 0
    tokens(j).tipe match {
      case u32"0x8E18F45B" /* LID */ => num = 1
      case u32"0xE95F063A" /* CHAR */ => num = 1
      case u32"0xA7CF0FE0" /* STRING */ => num = 1
      case u32"0x6890427A" /* "." */ => num = 1
      case _ =>
    }
    return num
  }

  @pure def predictLexerRule(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0x072BDD2B" /* "fragment" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x8E18F45B" /* LID */ => num = 2
            case _ =>
          }
        }

      case u32"0x8E18F45B" /* LID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x763C38BE" /* ":" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictElement(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xE95F063A" /* CHAR */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x3A15194D" /* ".." */ => num = 2
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0x8E18F45B" /* LID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0xA7CF0FE0" /* STRING */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0x6890427A" /* "." */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0xAAB7E55C" /* "~" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case _ =>
          }
        }

      case u32"0xD2EDBEA1" /* PID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xD827FEB7" /* "?" */ => num = 2
            case u32"0x82283B4B" /* "*" */ => num = 2
            case u32"0x797D7BC8" /* "+" */ => num = 2
            case _ =>
          }
        }
        if (num == 0) {
          num = 1
        }
      case u32"0x71F6371D" /* "(" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x6890427A" /* "." */ => num = 2
            case u32"0xAAB7E55C" /* "~" */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictChannel(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xFDCE65E5" /* "{" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x46562B21" /* "$channel" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictOption(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0x8E18F45B" /* LID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xEF954474" /* "=" */ => num = 2
            case _ =>
          }
        }

      case u32"0xD2EDBEA1" /* PID */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xEF954474" /* "=" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictOptionValue(j: Z): Z = {
    var num: Z = 0
    tokens(j).tipe match {
      case u32"0x8E18F45B" /* LID */ => num = 1
      case u32"0xD2EDBEA1" /* PID */ => num = 1
      case u32"0x589C233C" /* INT */ => num = 1
      case _ =>
    }
    return num
  }

  @pure def predictBlock(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0x71F6371D" /* "(" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x6890427A" /* "." */ => num = 2
            case u32"0xAAB7E55C" /* "~" */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictGrammarDef(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xAEB64436" /* "grammar" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x8E18F45B" /* LID */ => num = 2
            case u32"0xD2EDBEA1" /* PID */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictRange(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xE95F063A" /* CHAR */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0x3A15194D" /* ".." */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  @pure def predictId(j: Z): Z = {
    var num: Z = 0
    tokens(j).tipe match {
      case u32"0x8E18F45B" /* LID */ => num = 1
      case u32"0xD2EDBEA1" /* PID */ => num = 1
      case _ =>
    }
    return num
  }

  @pure def predictNot(j: Z): Z = {
    var num: Z = 0
    val j1 = j + 1
    val off1 = j1 < tokens.size
    tokens(j).tipe match {
      case u32"0xAAB7E55C" /* "~" */ =>
        if (off1) {
          tokens(j1).tipe match {
            case u32"0xE95F063A" /* CHAR */ => num = 2
            case u32"0xA7CF0FE0" /* STRING */ => num = 2
            case u32"0x71F6371D" /* "(" */ => num = 2
            case _ =>
          }
        }

      case _ =>
    }
    return num
  }

  def retVal(n: Z, resOpt: Option[Result], initial: B, noBacktrack: B): Either[Result, Z] = {
    resOpt match {
      case Some(res) => return Either.Left(res)
      case _ =>
        return if (noBacktrack) Either.Right(if (!initial) -n else n) else Either.Right(n)
    }
  }
}

@datatype class SireumGrammarLexer(input: String, docInfo: message.DocInfo) {

  val cis: ISZ[C] = conversions.String.toCis(input)

  def tokenizeAll(skipHidden: B, stopAtError: B, reporter: message.Reporter): ISZ[ParseTree.Leaf] = {
    var i: Z = 0
    var r = ISZ[ParseTree.Leaf]()
    while (i < cis.size) {
      tokenize(i) match {
        case Some(lr@Result(token: ParseTree.Leaf, _)) =>
          i = lr.newIndex
          if (!(skipHidden && token.isHidden)) {
            r = r :+ token
          }
        case _ =>
          val offsetLength = (conversions.Z.toU64(i) << u64"32") | conversions.Z.toU64(1)
          val posOpt: Option[message.Position] = Some(message.PosInfo(docInfo, offsetLength))
          reporter.error(posOpt, kind, s"Could not recognize character '${ops.COps(cis(i)).escapeString}'")
          if (stopAtError) {
            return r
          }
          r = r :+ ParseTree.Leaf(conversions.String.fromCis(ISZ(cis(i))), "<ERROR>",u32"0xE3CDEDDA", F, posOpt)
          i = i + 1
      }
    }
    r = r :+ ParseTree.Leaf("", "EOF", u32"0xFC5CB374", F, None())
    return r
  }

  @pure def tokenize(i: Z): Option[Result] = {
    var r = Result(ParseTree.Leaf("", "", u32"-2", T, None()), -1)
    def update(rOpt: Option[Result]): Unit = {
      rOpt match {
        case Some(newR) if newR.newIndex > r.newIndex => r = newR
        case _ =>
      }
    }
    update(lex_grammar(i))
    update(lex_u003B(i))
    update(lex_options(i))
    update(lex_u007B(i))
    update(lex_u007D(i))
    update(lex_u003D(i))
    update(lex_u003A(i))
    update(lex_u007C(i))
    update(lex_fragment(i))
    update(lex_u0028(i))
    update(lex_u0029(i))
    update(lex_u003F(i))
    update(lex_u002A(i))
    update(lex_u002B(i))
    update(lex_u007E(i))
    update(lex_u002Eu002E(i))
    update(lex_u002E(i))
    update(lex__channel(i))
    update(lex_CHAR(i))
    update(lex_STRING(i))
    update(lex_INT(i))
    update(lex_LID(i))
    update(lex_PID(i))
    update(lex_PHEADER(i))
    update(lex_LHEADER(i))
    update(lex_COMMENT(i))
    update(lex_WS(i))
    return if (r.newIndex > i) Some(r) else None()
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
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"3" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if (c === '\'') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= '&' || '(' <= c && c <= '[' || ']' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found && (c === '\\')) {
            update(u32"4")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (c === '\'') {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" => return afterAcceptIndex
        case u32"4" =>
          val c = cis(j)
          var found = F
          if (c === '"' || c === '\'' || c === '\\' || c === 'b' || c === 'f' || c === 'n' || c === 'r' || c === 't') {
            update(u32"2")
            found = T
          }
          if (!found && (c === 'u')) {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"5" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"7" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
  }

  @strictpure def lex_CHAR(index: Z): Option[Result] =
     lexH(index, dfa_CHAR(index), """CHAR""", u32"0xE95F063A", F)

  @pure def dfa_STRING(i: Z): Z = {
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"4" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if (c === '\'') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= '&' || '(' <= c && c <= '[' || ']' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found && (c === '\\')) {
            update(u32"10")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= '&' || '(' <= c && c <= '[' || ']' <= c && c <= maxChar) {
            update(u32"3")
            found = T
          }
          if (!found && (c === '\\')) {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= '&' || '(' <= c && c <= '[' || ']' <= c && c <= maxChar) {
            update(u32"3")
            found = T
          }
          if (!found && (c === '\'')) {
            update(u32"4")
            found = T
          }
          if (!found && (c === '\\')) {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"4" => return afterAcceptIndex
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (c === '"' || c === '\'' || c === '\\' || c === 'b' || c === 'f' || c === 'n' || c === 'r' || c === 't') {
            update(u32"3")
            found = T
          }
          if (!found && (c === 'u')) {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"7" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"9")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"9" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"10" =>
          val c = cis(j)
          var found = F
          if (c === '"' || c === '\'' || c === '\\' || c === 'b' || c === 'f' || c === 'n' || c === 'r' || c === 't') {
            update(u32"2")
            found = T
          }
          if (!found && (c === 'u')) {
            update(u32"11")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"11" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"12")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"12" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"13")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"13" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"14")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"14" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f') {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
  }

  @strictpure def lex_STRING(index: Z): Option[Result] =
     lexH(index, dfa_STRING(index), """STRING""", u32"0xA7CF0FE0", F)

  @pure def dfa_INT(i: Z): Z = {
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"1" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if ('1' <= c && c <= '9') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
  }

  @strictpure def lex_INT(index: Z): Option[Result] =
     lexH(index, dfa_INT(index), """INT""", u32"0x589C233C", F)

  @pure def dfa_LID(i: Z): Z = {
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"1" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if ('A' <= c && c <= 'Z') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'Z' || c === '_' || 'a' <= c && c <= 'z') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
  }

  @strictpure def lex_LID(index: Z): Option[Result] =
     lexH(index, dfa_LID(index), """LID""", u32"0x8E18F45B", F)

  @pure def dfa_PID(i: Z): Z = {
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"1" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if ('a' <= c && c <= 'z') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if ('0' <= c && c <= '9' || 'A' <= c && c <= 'Z' || c === '_' || 'a' <= c && c <= 'z') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
  }

  @strictpure def lex_PID(index: Z): Option[Result] =
     lexH(index, dfa_PID(index), """PID""", u32"0xD2EDBEA1", F)

  @pure def dfa_PHEADER(i: Z): Z = {
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"9" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if (c === '@') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"1")
            found = T
          }
          if (!found && (c === 'h')) {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (c === 'e') {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" =>
          val c = cis(j)
          var found = F
          if (c === 'a') {
            update(u32"4")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"4" =>
          val c = cis(j)
          var found = F
          if (c === 'd') {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (c === 'e') {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" =>
          val c = cis(j)
          var found = F
          if (c === 'r') {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"7" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"7")
            found = T
          }
          if (!found && (c === '{')) {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= '|' || '~' <= c && c <= maxChar) {
            update(u32"8")
            found = T
          }
          if (!found && (c === '}')) {
            update(u32"9")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"9" => return afterAcceptIndex
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
  }

  @strictpure def lex_PHEADER(index: Z): Option[Result] =
     lexH(index, dfa_PHEADER(index), """PHEADER""", u32"0xEDD2348C", F)

  @pure def dfa_LHEADER(i: Z): Z = {
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"16" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if (c === '@') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"1")
            found = T
          }
          if (!found && (c === 'l')) {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (c === 'e') {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" =>
          val c = cis(j)
          var found = F
          if (c === 'x') {
            update(u32"4")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"4" =>
          val c = cis(j)
          var found = F
          if (c === 'e') {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (c === 'r') {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"6")
            found = T
          }
          if (!found && (c === ':')) {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"7" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"7")
            found = T
          }
          if (!found && (c === ':')) {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"8")
            found = T
          }
          if (!found && (c === 'h')) {
            update(u32"9")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"9" =>
          val c = cis(j)
          var found = F
          if (c === 'e') {
            update(u32"10")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"10" =>
          val c = cis(j)
          var found = F
          if (c === 'a') {
            update(u32"11")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"11" =>
          val c = cis(j)
          var found = F
          if (c === 'd') {
            update(u32"12")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"12" =>
          val c = cis(j)
          var found = F
          if (c === 'e') {
            update(u32"13")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"13" =>
          val c = cis(j)
          var found = F
          if (c === 'r') {
            update(u32"14")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"14" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"14")
            found = T
          }
          if (!found && (c === '{')) {
            update(u32"15")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"15" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= '|' || '~' <= c && c <= maxChar) {
            update(u32"15")
            found = T
          }
          if (!found && (c === '}')) {
            update(u32"16")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"16" => return afterAcceptIndex
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
  }

  @strictpure def lex_LHEADER(index: Z): Option[Result] =
     lexH(index, dfa_LHEADER(index), """LHEADER""", u32"0x2322FC01", F)

  @pure def dfa_COMMENT(i: Z): Z = {
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"5" => 
         case u32"6" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if (c === '/') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (c === '*') {
            update(u32"2")
            found = T
          }
          if (!found && (c === '/')) {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= ')' || '+' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found && (c === '*')) {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= ')' || '+' <= c && c <= '.' || '0' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found && (c === '*')) {
            update(u32"4")
            found = T
          }
          if (!found && (c === '/')) {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"4" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= ')' || '+' <= c && c <= '.' || '0' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found && (c === '*')) {
            update(u32"3")
            found = T
          }
          if (!found && (c === '/')) {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= ')' || '+' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found && (c === '*')) {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" => return afterAcceptIndex
        case u32"7" =>
          val c = cis(j)
          var found = F
          if (minChar <= c && c <= '\u0009' || '\u000B' <= c && c <= '\u000C' || '\u000E' <= c && c <= maxChar) {
            update(u32"7")
            found = T
          }
          if (!found && (c === '\u000A')) {
            update(u32"6")
            found = T
          }
          if (!found && (c === '\u000D')) {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if (c === '\u000A') {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
  }

  @strictpure def lex_COMMENT(index: Z): Option[Result] =
     lexH(index, dfa_COMMENT(index), """COMMENT""", u32"0x486B464F", T)

  @pure def dfa_WS(i: Z): Z = {
    var state = u32"0"
    var afterAcceptIndex: Z = -1
    var j = i

    def update(newState: U32): Unit = {
       state = newState
       state match {
         case u32"1" => 
         case _ => return
       }
       afterAcceptIndex = j + 1
    }

    while (j < cis.size) {
      state match {
        case u32"0" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if ('\u0009' <= c && c <= '\u000A' || c === '\u000D' || c === ' ') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case _ => halt("Infeasible")
      }
      j = j + 1
    }
    return afterAcceptIndex
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
      return Some(Result(ParseTree.Leaf(ops.StringOps.substring(cis, index, newIndex),
        name, tipe, isHidden, Some(message.PosInfo(docInfo, offsetLength(index, newIndex - index)))),
        newIndex))
    } else {
      return None()
    }
  }

}