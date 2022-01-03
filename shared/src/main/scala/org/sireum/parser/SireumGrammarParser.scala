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

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"8" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """grammarDef""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).text === "grammar") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found) {
            parseId(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"2")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" =>
          var found = F
          if (!found && tokens(j).text === ";") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"3")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"3" =>
          var found = F
          if (!found) {
            parseOptionsSpec(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"4")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).ruleName === "PHEADER") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"5")
            found = T
          }
          if (!found && tokens(j).ruleName === "LHEADER") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"6")
            found = T
          }
          if (!found) {
            parseParserRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"6")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            parseLexerRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"7")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).ruleName === "EOF") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"8")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"4" =>
          var found = F
          if (!found && tokens(j).ruleName === "PHEADER") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"5")
            found = T
          }
          if (!found && tokens(j).ruleName === "LHEADER") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"6")
            found = T
          }
          if (!found) {
            parseParserRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"6")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            parseLexerRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"7")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).ruleName === "EOF") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"8")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"5" =>
          var found = F
          if (!found && tokens(j).ruleName === "LHEADER") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"6")
            found = T
          }
          if (!found) {
            parseParserRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"6")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            parseLexerRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"7")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).ruleName === "EOF") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"8")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"6" =>
          var found = F
          if (!found) {
            parseParserRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"6")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            parseLexerRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"7")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).ruleName === "EOF") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"8")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"7" =>
          var found = F
          if (!found) {
            parseLexerRule(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"7")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).ruleName === "EOF") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"8")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"8" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseOptionsSpec(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"4" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """optionsSpec""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).text === "options") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found && tokens(j).text === "{") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" =>
          var found = F
          if (!found) {
            parseOption(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"3")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"3" =>
          var found = F
          if (!found) {
            parseOption(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"3")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).text === "}") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"4")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"4" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseOption(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"4" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """option""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found) {
            parseId(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found && tokens(j).text === "=") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" =>
          var found = F
          if (!found) {
            parseOptionValue(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"3")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"3" =>
          var found = F
          if (!found && tokens(j).text === ";") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"4")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"4" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseOptionValue(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """optionValue""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found) {
            parseId(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).ruleName === "INT") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseParserRule(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"5" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """parserRule""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).ruleName === "PID") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found && tokens(j).text === ":") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" =>
          var found = F
          if (!found) {
            parseAlt(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"3")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"3" =>
          var found = F
          if (!found && tokens(j).text === "|") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"4")
            found = T
          }
          if (!found && tokens(j).text === ";") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"5")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"4" =>
          var found = F
          if (!found) {
            parseAlt(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"3")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"5" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseLexerRule(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"7" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """lexerRule""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).text === "fragment") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found && tokens(j).ruleName === "LID") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found && tokens(j).ruleName === "LID") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" =>
          var found = F
          if (!found && tokens(j).text === ":") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"3")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"3" =>
          var found = F
          if (!found) {
            parseAlt(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"4")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"4" =>
          var found = F
          if (!found && tokens(j).text === "|") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"5")
            found = T
          }
          if (!found) {
            parseChannel(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"8")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).text === ";") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"7")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"5" =>
          var found = F
          if (!found) {
            parseAlt(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"6")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"6" =>
          var found = F
          if (!found && tokens(j).text === "|") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"5")
            found = T
          }
          if (!found && tokens(j).text === ";") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"7")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"7" => return retVal(max)
        case u32"8" =>
          var found = F
          if (!found && tokens(j).text === "|") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"9")
            found = T
          }
          if (!found && tokens(j).text === ";") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"7")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"9" =>
          var found = F
          if (!found) {
            parseAlt(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"10")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"10" =>
          var found = F
          if (!found) {
            parseChannel(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"8")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseBlock(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"4" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """block""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).text === "(") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found) {
            parseAlt(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"2")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" =>
          var found = F
          if (!found && tokens(j).text === "|") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"3")
            found = T
          }
          if (!found && tokens(j).text === ")") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"4")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"3" =>
          var found = F
          if (!found) {
            parseAlt(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"2")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"4" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseAlt(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """alt""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found) {
            parseElement(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found) {
            parseElement(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseElement(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case u32"2" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """element""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found) {
            parseAtom(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            parseBlock(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found && tokens(j).text === "?") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found && tokens(j).text === "*") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found && tokens(j).text === "+") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseAtom(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """atom""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found) {
            parseRange(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            parseTerminal(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            parseNot(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"1")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found && tokens(j).ruleName === "PID") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseNot(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"2" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """not""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).text === "~") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found && tokens(j).ruleName === "CHAR") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found && tokens(j).ruleName === "STRING") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            parseBlock(j) match {
              case Either.Left(r) =>
                trees = trees :+ r.tree
                j = r.newIndex
                update(u32"2")
                found = T
              case res@Either.Right(r) =>
                if (r < 0) {
                  return res
                } else if (max < r) {
                  max = r
                }
            }
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseRange(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"3" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """range""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).ruleName === "CHAR") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found && tokens(j).text === "..") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" =>
          var found = F
          if (!found && tokens(j).ruleName === "CHAR") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"3")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"3" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseTerminal(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """terminal""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).ruleName === "LID") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found && tokens(j).ruleName === "CHAR") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found && tokens(j).ruleName === "STRING") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found && tokens(j).text === ".") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseId(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"1" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """id""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).ruleName === "LID") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found && tokens(j).ruleName === "PID") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
  }

  @pure def parseChannel(i: Z): Either[Result, Z] = {
    var state = u32"0"
    var resOpt: Option[Result] = None()
    var trees = ISZ[ParseTree]()
    var j = i
    var max = i
    var initial = T

    def update(newState: U32): Unit = {
      initial = F
      state = newState
      state match {
        case u32"6" => 
        case _ => return
      }
      resOpt = Some(Result(ParseTree.Node(trees, """channel""",  None()), j))
    }

    def retVal(n: Z): Either[Result, Z] = {
      resOpt match {
        case Some(res) => return Either.Left(res)
        case _ =>
          return Either.Right(n)
      }
    }

    while (j < tokens.size) {
      state match {
        case u32"0" =>
          var found = F
          if (!found && tokens(j).text === "{") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"1")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"1" =>
          var found = F
          if (!found && tokens(j).text === "$channel") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"2")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"2" =>
          var found = F
          if (!found && tokens(j).text === "=") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"3")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"3" =>
          var found = F
          if (!found && tokens(j).ruleName === "LID") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"4")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"4" =>
          var found = F
          if (!found && tokens(j).text === ";") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"5")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"5" =>
          var found = F
          if (!found && tokens(j).text === "}") {
            trees = trees :+ tokens(j)
            j = j + 1
            update(u32"6")
            found = T
          }
          if (!found) {
            return retVal(max)
          }
        case u32"6" => return retVal(max)
        case _ => halt("Infeasible")
      }
      if (max < j) {
        max = j
      }
    }

    return retVal(j)
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
          r = r :+ ParseTree.Leaf(conversions.String.fromCis(ISZ(cis(i))), "<ERROR>", F, posOpt)
          i = i + 1
      }
    }
    r = r :+ ParseTree.Leaf("", "EOF", F, None())
    return r
  }

  @pure def tokenize(i: Z): Option[Result] = {
    var r = Result(ParseTree.Leaf("", "", T, None()), -1)
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
     lexH(index, lit_grammar(index), """'grammar'""", F)

  @pure def lit_u003B(i: Z): Z = {
    if (i < cis.size && cis(i) === ';') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u003B(index: Z): Option[Result] =
     lexH(index, lit_u003B(index), """';'""", F)

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
     lexH(index, lit_options(index), """'options'""", F)

  @pure def lit_u007B(i: Z): Z = {
    if (i < cis.size && cis(i) === '{') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u007B(index: Z): Option[Result] =
     lexH(index, lit_u007B(index), """'{'""", F)

  @pure def lit_u007D(i: Z): Z = {
    if (i < cis.size && cis(i) === '}') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u007D(index: Z): Option[Result] =
     lexH(index, lit_u007D(index), """'}'""", F)

  @pure def lit_u003D(i: Z): Z = {
    if (i < cis.size && cis(i) === '=') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u003D(index: Z): Option[Result] =
     lexH(index, lit_u003D(index), """'='""", F)

  @pure def lit_u003A(i: Z): Z = {
    if (i < cis.size && cis(i) === ':') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u003A(index: Z): Option[Result] =
     lexH(index, lit_u003A(index), """':'""", F)

  @pure def lit_u007C(i: Z): Z = {
    if (i < cis.size && cis(i) === '|') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u007C(index: Z): Option[Result] =
     lexH(index, lit_u007C(index), """'|'""", F)

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
     lexH(index, lit_fragment(index), """'fragment'""", F)

  @pure def lit_u0028(i: Z): Z = {
    if (i < cis.size && cis(i) === '(') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u0028(index: Z): Option[Result] =
     lexH(index, lit_u0028(index), """'('""", F)

  @pure def lit_u0029(i: Z): Z = {
    if (i < cis.size && cis(i) === ')') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u0029(index: Z): Option[Result] =
     lexH(index, lit_u0029(index), """')'""", F)

  @pure def lit_u003F(i: Z): Z = {
    if (i < cis.size && cis(i) === '?') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u003F(index: Z): Option[Result] =
     lexH(index, lit_u003F(index), """'?'""", F)

  @pure def lit_u002A(i: Z): Z = {
    if (i < cis.size && cis(i) === '*') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u002A(index: Z): Option[Result] =
     lexH(index, lit_u002A(index), """'*'""", F)

  @pure def lit_u002B(i: Z): Z = {
    if (i < cis.size && cis(i) === '+') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u002B(index: Z): Option[Result] =
     lexH(index, lit_u002B(index), """'+'""", F)

  @pure def lit_u007E(i: Z): Z = {
    if (i < cis.size && cis(i) === '~') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u007E(index: Z): Option[Result] =
     lexH(index, lit_u007E(index), """'~'""", F)

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
     lexH(index, lit_u002Eu002E(index), """'..'""", F)

  @pure def lit_u002E(i: Z): Z = {
    if (i < cis.size && cis(i) === '.') {
      return i + 1
    }
    return -1
  }

  @strictpure def lex_u002E(index: Z): Option[Result] =
     lexH(index, lit_u002E(index), """'.'""", F)

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
     lexH(index, lit__channel(index), """'$channel'""", F)

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
          if (!found && c === '\'') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= '&') {
            update(u32"2")
            found = T
          }
          if (!found && '(' <= c && c <= '[') {
            update(u32"2")
            found = T
          }
          if (!found && c === '\\') {
            update(u32"4")
            found = T
          }
          if (!found && ']' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (!found && c === '\'') {
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
          if (!found && c === '"') {
            update(u32"2")
            found = T
          }
          if (!found && c === '\'') {
            update(u32"2")
            found = T
          }
          if (!found && c === '\\') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'b') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'f') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'n') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'r') {
            update(u32"2")
            found = T
          }
          if (!found && c === 't') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'u') {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"6")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"6")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"7")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"7")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"7" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"8")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"8")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"2")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"2")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
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
     lexH(index, dfa_CHAR(index), """CHAR""", F)

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
          if (!found && c === '\'') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= '&') {
            update(u32"2")
            found = T
          }
          if (!found && '(' <= c && c <= '[') {
            update(u32"2")
            found = T
          }
          if (!found && c === '\\') {
            update(u32"10")
            found = T
          }
          if (!found && ']' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= '&') {
            update(u32"3")
            found = T
          }
          if (!found && '(' <= c && c <= '[') {
            update(u32"3")
            found = T
          }
          if (!found && c === '\\') {
            update(u32"5")
            found = T
          }
          if (!found && ']' <= c && c <= maxChar) {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= '&') {
            update(u32"3")
            found = T
          }
          if (!found && c === '\'') {
            update(u32"4")
            found = T
          }
          if (!found && '(' <= c && c <= '[') {
            update(u32"3")
            found = T
          }
          if (!found && c === '\\') {
            update(u32"5")
            found = T
          }
          if (!found && ']' <= c && c <= maxChar) {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"4" => return afterAcceptIndex
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (!found && c === '"') {
            update(u32"3")
            found = T
          }
          if (!found && c === '\'') {
            update(u32"3")
            found = T
          }
          if (!found && c === '\\') {
            update(u32"3")
            found = T
          }
          if (!found && c === 'b') {
            update(u32"3")
            found = T
          }
          if (!found && c === 'f') {
            update(u32"3")
            found = T
          }
          if (!found && c === 'n') {
            update(u32"3")
            found = T
          }
          if (!found && c === 'r') {
            update(u32"3")
            found = T
          }
          if (!found && c === 't') {
            update(u32"3")
            found = T
          }
          if (!found && c === 'u') {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"7")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"7")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"7" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"8")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"8")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"9")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"9")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"9")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"9" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"3")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"3")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"10" =>
          val c = cis(j)
          var found = F
          if (!found && c === '"') {
            update(u32"2")
            found = T
          }
          if (!found && c === '\'') {
            update(u32"2")
            found = T
          }
          if (!found && c === '\\') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'b') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'f') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'n') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'r') {
            update(u32"2")
            found = T
          }
          if (!found && c === 't') {
            update(u32"2")
            found = T
          }
          if (!found && c === 'u') {
            update(u32"11")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"11" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"12")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"12")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"12")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"12" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"13")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"13")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"13")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"13" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"14")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"14")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
            update(u32"14")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"14" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"2")
            found = T
          }
          if (!found && 'A' <= c && c <= 'F') {
            update(u32"2")
            found = T
          }
          if (!found && 'a' <= c && c <= 'f') {
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
     lexH(index, dfa_STRING(index), """STRING""", F)

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
          if (!found && '1' <= c && c <= '9') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
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
     lexH(index, dfa_INT(index), """INT""", F)

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
          if (!found && 'A' <= c && c <= 'Z') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"1")
            found = T
          }
          if (!found && 'A' <= c && c <= 'Z') {
            update(u32"1")
            found = T
          }
          if (!found && c === '_') {
            update(u32"1")
            found = T
          }
          if (!found && 'a' <= c && c <= 'z') {
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
     lexH(index, dfa_LID(index), """LID""", F)

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
          if (!found && 'a' <= c && c <= 'z') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && '0' <= c && c <= '9') {
            update(u32"1")
            found = T
          }
          if (!found && 'A' <= c && c <= 'Z') {
            update(u32"1")
            found = T
          }
          if (!found && c === '_') {
            update(u32"1")
            found = T
          }
          if (!found && 'a' <= c && c <= 'z') {
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
     lexH(index, dfa_PID(index), """PID""", F)

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
          if (!found && c === '@') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"1")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"1")
            found = T
          }
          if (!found && c === ' ') {
            update(u32"1")
            found = T
          }
          if (!found && c === 'h') {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'e') {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'a') {
            update(u32"4")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"4" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'd') {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'e') {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'r') {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"7" =>
          val c = cis(j)
          var found = F
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"7")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"7")
            found = T
          }
          if (!found && c === ' ') {
            update(u32"7")
            found = T
          }
          if (!found && c === '{') {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= '|') {
            update(u32"8")
            found = T
          }
          if (!found && c === '}') {
            update(u32"9")
            found = T
          }
          if (!found && '~' <= c && c <= maxChar) {
            update(u32"8")
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
     lexH(index, dfa_PHEADER(index), """PHEADER""", F)

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
          if (!found && c === '@') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"1")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"1")
            found = T
          }
          if (!found && c === ' ') {
            update(u32"1")
            found = T
          }
          if (!found && c === 'l') {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'e') {
            update(u32"3")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'x') {
            update(u32"4")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"4" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'e') {
            update(u32"5")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'r') {
            update(u32"6")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" =>
          val c = cis(j)
          var found = F
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"6")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"6")
            found = T
          }
          if (!found && c === ' ') {
            update(u32"6")
            found = T
          }
          if (!found && c === ':') {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"7" =>
          val c = cis(j)
          var found = F
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"7")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"7")
            found = T
          }
          if (!found && c === ' ') {
            update(u32"7")
            found = T
          }
          if (!found && c === ':') {
            update(u32"8")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"8")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"8")
            found = T
          }
          if (!found && c === ' ') {
            update(u32"8")
            found = T
          }
          if (!found && c === 'h') {
            update(u32"9")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"9" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'e') {
            update(u32"10")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"10" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'a') {
            update(u32"11")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"11" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'd') {
            update(u32"12")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"12" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'e') {
            update(u32"13")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"13" =>
          val c = cis(j)
          var found = F
          if (!found && c === 'r') {
            update(u32"14")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"14" =>
          val c = cis(j)
          var found = F
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"14")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"14")
            found = T
          }
          if (!found && c === ' ') {
            update(u32"14")
            found = T
          }
          if (!found && c === '{') {
            update(u32"15")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"15" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= '|') {
            update(u32"15")
            found = T
          }
          if (!found && c === '}') {
            update(u32"16")
            found = T
          }
          if (!found && '~' <= c && c <= maxChar) {
            update(u32"15")
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
     lexH(index, dfa_LHEADER(index), """LHEADER""", F)

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
          if (!found && c === '/') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && c === '*') {
            update(u32"2")
            found = T
          }
          if (!found && c === '/') {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"2" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= ')') {
            update(u32"2")
            found = T
          }
          if (!found && c === '*') {
            update(u32"3")
            found = T
          }
          if (!found && '+' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"3" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= ')') {
            update(u32"2")
            found = T
          }
          if (!found && c === '*') {
            update(u32"4")
            found = T
          }
          if (!found && '+' <= c && c <= '.') {
            update(u32"2")
            found = T
          }
          if (!found && c === '/') {
            update(u32"6")
            found = T
          }
          if (!found && '0' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"4" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= ')') {
            update(u32"2")
            found = T
          }
          if (!found && c === '*') {
            update(u32"3")
            found = T
          }
          if (!found && '+' <= c && c <= '.') {
            update(u32"2")
            found = T
          }
          if (!found && c === '/') {
            update(u32"5")
            found = T
          }
          if (!found && '0' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"5" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= ')') {
            update(u32"2")
            found = T
          }
          if (!found && c === '*') {
            update(u32"3")
            found = T
          }
          if (!found && '+' <= c && c <= maxChar) {
            update(u32"2")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"6" => return afterAcceptIndex
        case u32"7" =>
          val c = cis(j)
          var found = F
          if (!found && minChar <= c && c <= '\u0009') {
            update(u32"7")
            found = T
          }
          if (!found && c === '\u000A') {
            update(u32"6")
            found = T
          }
          if (!found && '\u000B' <= c && c <= '\u000C') {
            update(u32"7")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"8")
            found = T
          }
          if (!found && '\u000E' <= c && c <= maxChar) {
            update(u32"7")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"8" =>
          val c = cis(j)
          var found = F
          if (!found && c === '\u000A') {
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
     lexH(index, dfa_COMMENT(index), """COMMENT""", T)

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
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"1")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"1")
            found = T
          }
          if (!found && c === ' ') {
            update(u32"1")
            found = T
          }
          if (!found) {
            return afterAcceptIndex
          }
        case u32"1" =>
          val c = cis(j)
          var found = F
          if (!found && '\u0009' <= c && c <= '\u000A') {
            update(u32"1")
            found = T
          }
          if (!found && c === '\u000D') {
            update(u32"1")
            found = T
          }
          if (!found && c === ' ') {
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
     lexH(index, dfa_WS(index), """WS""", T)

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

  @pure def lexH(index: Z, newIndex: Z, name: String, isHidden: B): Option[Result] = {
    if (newIndex > 0) {
      return Some(Result(ParseTree.Leaf(ops.StringOps.substring(cis, index, newIndex),
        name, isHidden, Some(message.PosInfo(docInfo, offsetLength(index, newIndex - index)))),
        newIndex))
    } else {
      return None()
    }
  }

}