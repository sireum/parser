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

package org.sireum.parser

import org.sireum._
import org.sireum.message.Reporter
import org.sireum.parser.{GrammarAst => AST}

@datatype class ParserGenerator {

  val tqs: String = "\"\"\""

  def gen(licenseOpt: Option[ST], fileInfo: ST, packageOpt: Option[ST], name: String, ast: AST.Grammar,
          memoize: B, reporter: Reporter): Option[ST] = {

    val options = HashMap.empty[String, String] ++ ast.options
    var kOpt: Option[Z] = None()
    options.get("k") match {
      case Some(v) => Z(v) match {
        case Some(n) => kOpt = Some(n)
        case _ =>
          reporter.error(None(), "ParserGenerator", s"Expecting an integer for k, but found: $v")
          return None()
      }
      case _ =>
    }
    if (kOpt.isEmpty) {
      reporter.error(None(), "ParserGenerator", s"Please specify the k lookahead option")
      return None()
    }

    val lexerDfaMap = ParserGenerator.Ext.computeLexerDfas(ast, reporter)

    if (reporter.hasError) {
      return None()
    }

    var lexerDefs = ISZ[ST]()

    def genLexer(): Unit = {

      val literalNameMap = literalCharStrNames(ast)
      var typ = 0

      def lexST(lexname: ST, recognizerName: ST, tokenString: ST, isHidden: B): ST = {
        val r =
          st"""@strictpure def $lexname(index: Z): Option[Result] =
              |   lexH(index, $recognizerName(index), $tokenString, ${if (isHidden) "T" else "F"})"""
        typ = typ + 1
        return r
      }

      var lexNames = ISZ[ST]()

      for (p <- literalNameMap.entries) {
        val s: String = p._1 match {
          case Either.Left(c) => lexerDefs = lexerDefs :+ genLiteralC(c, p._2); c.string
          case Either.Right(s) => lexerDefs = lexerDefs :+ genLiteralString(s, p._2); s
        }
        val lexname = lexName(ops.StringOps(p._2).substring(4, p._2.size))
        lexNames = lexNames :+ lexname
        lexerDefs = lexerDefs :+ lexST(lexname, st"${p._2}", st"$tqs'$s'$tqs", F)
      }

      var hiddens = ISZ[ST]()
      var hiddenNames = HashSet.empty[String]
      for (rule <- ast.rules if rule.isHidden) {
        hiddenNames = hiddenNames + rule.name
        hiddens = hiddens :+
          st"""j = ${dfaName(rule.name)}(i)
              |if (j > 0) {
              |  return j
              |}"""
      }

      for (p <- lexerDfaMap.entries) {
        val dfaname = dfaName(p._1)
        lexerDefs = lexerDefs :+ genLexerDfa(dfaname, p._2)

        val lexname = lexName(p._1)
        lexNames = lexNames :+ lexname
        lexerDefs = lexerDefs :+ lexST(lexname, dfaname, st"$tqs${p._1}$tqs", hiddenNames.contains(p._1))
      }

      lexerDefs = lexerDefs :+
        st"""@pure def hidden(i: Z): Z = {
            |   var j: Z = -1
            |   ${(hiddens, "\n")}
            |   return -1
            |}"""

      lexerDefs = lexerDefs :+
        st"""@pure def lexH(index: Z, newIndex: Z, name: String, isHidden: B): Option[Result] = {
            |  if (newIndex > 0) {
            |    return Some(Result(ParseTree.Leaf(ops.StringOps.substring(cis, index, newIndex),
            |      name, isHidden, Some(message.PosInfo(docInfo, offsetLength(index, newIndex - index)))),
            |      newIndex))
            |  } else {
            |    return None()
            |  }
            |}"""

      lexerDefs =
        st"""def tokenizeAll(skipHidden: B, stopAtError: B, reporter: message.Reporter): ISZ[ParseTree.Leaf] = {
            |  var i: Z = 0
            |  var r = ISZ[ParseTree.Leaf]()
            |  while (i < cis.size) {
            |    tokenize(i) match {
            |      case Some(lr@Result(token: ParseTree.Leaf, _)) =>
            |        i = lr.newIndex
            |        if (!(skipHidden && token.isHidden)) {
            |          r = r :+ token
            |        }
            |      case _ =>
            |        val offsetLength = (conversions.Z.toU64(i) << u64"32") | conversions.Z.toU64(1)
            |        val posOpt: Option[message.Position] = Some(message.PosInfo(docInfo, offsetLength))
            |        reporter.error(posOpt, kind, s"Could not recognize character '$${ops.COps(cis(i)).escapeString}'")
            |        if (stopAtError) {
            |          return r
            |        }
            |        r = r :+ ParseTree.Leaf(conversions.String.fromCis(ISZ(cis(i))), "<ERROR>", F, posOpt)
            |        i = i + 1
            |    }
            |  }
            |  r = r :+ ParseTree.Leaf("", "EOF", F, None())
            |  return r
            |}
            |
            |@pure def tokenize(i: Z): Option[Result] = {
            |  var r = Result(ParseTree.Leaf("", "", T, None()), -1)
            |  def update(rOpt: Option[Result]): Unit = {
            |    rOpt match {
            |      case Some(newR) if newR.newIndex > r.newIndex => r = newR
            |      case _ =>
            |    }
            |  }
            |  ${(for (lexname <- lexNames) yield st"""update($lexname(i))""", "\n")}
            |  return if (r.newIndex > i) Some(r) else None()
            |}""" +: lexerDefs
    }

    val parserDfaMap = ParserGenerator.Ext.computeParserDfas(ast)

    var parserDefs = ISZ[ST]()

    val nameRuleMap = HashMap.empty[String, AST.Rule] ++ (for (rule <- ast.rules if !rule.isFragment && !rule.isHidden) yield (rule.name, rule))

    val k = kOpt.get

    val laMap = LookAhead.compute(k, nameRuleMap, parserDfaMap, reporter)

    if (reporter.hasError) {
      return None()
    }

    def genParser(): Unit = {

      for (p <- parserDfaMap.entries) {
        parserDefs = parserDefs :+ genParserDfa(k, memoize, parseName(p._1), p._1, p._2._1, p._2._2, laMap)
      }

      for (p <- laMap.entries) {
        val trie = p._2
        val sts = genTries(k, trie)
        parserDefs = parserDefs :+
          st"""${if (memoize) "@memoize" else "@pure"} def ${predictName(p._1)}(j: Z): B = {
              |  var shouldTry = F
              |  ${(sts, "\n")}
              |  return shouldTry
              |}"""
      }
    }

    genLexer()
    genParser()

    val parserOpt: Option[ST] = ast.rules match {
      case ISZ(first, _*) if !first.isLexer =>
        Some(
          st"""def parse(uriOpt: Option[String], input: String, reporter: message.Reporter): Option[ParseTree.Result] = {
              |  val docInfo = message.DocInfo.create(uriOpt, input)
              |  val tokens = lex(input, docInfo, T, T, reporter)
              |  if (reporter.hasError) {
              |    return None()
              |  }
              |  ${name}Parser(tokens).${parseName(first.name)}(0) match {
              |    case Either.Left(r) => return Some(ParseTree.Result(r.tree, docInfo))
              |    case Either.Right(r) =>
              |      val idx: Z = if (r < 0) -r else r
              |      if (idx < tokens.size) {
              |        reporter.error(tokens(idx - 1).posOpt, kind, s"Could not parse token: $${tokens(idx).text}")
              |      } else {
              |        reporter.error(tokens(idx - 1).posOpt, kind, "Expecting more input but reached the end")
              |      }
              |      return None()
              |  }
              |}"""
        )
      case _ => None()
    }

    return Some(
      st"""// #Sireum
          |$licenseOpt
          |$fileInfo
          |$packageOpt
          |
          |import org.sireum._
          |import org.sireum.U32._
          |import org.sireum.U64._
          |import org.sireum.conversions.U32.toC
          |
          |object ${name}Parser {
          |
          |  @datatype class Result(val tree: ParseTree, val newIndex: Z)
          |
          |  val kind: String = "${name}Parser"
          |
          |  val minChar: C = '\u0000'
          |  val maxChar: C = toC(u32"0x0010FFFF")
          |
          |  $parserOpt
          |
          |  def lex(input: String, docInfo: message.DocInfo, skipHidden: B, stopAtError: B,
          |          reporter: message.Reporter): ISZ[ParseTree.Leaf] = {
          |    return ${name}Lexer(input, docInfo).tokenizeAll(skipHidden, stopAtError, reporter)
          |  }
          |
          |  @pure def posOpts(docInfo: message.DocInfo,
          |                    posOpt1: Option[message.Position],
          |                    posOpt2: Option[message.Position]): Option[message.Position] = {
          |    val pos1 = posOpt1.get
          |    val pos2 = posOpt2.get
          |    return Some(message.PosInfo(docInfo, offsetLength(pos1.offset,
          |      pos2.offset + pos2.length - pos1.offset)))
          |  }
          |
          |  @strictpure def offsetLength(offset: Z, length: Z): U64 =
          |    (conversions.Z.toU64(offset) << u64"32") | (conversions.Z.toU64(length) & u64"0xFFFFFFFF")
          |}
          |
          |import ${name}Parser._
          |
          |@datatype class ${name}Parser(tokens: ISZ[ParseTree.Leaf]) {
          |
          |  ${(parserDefs, "\n\n")}
          |
          |  def retVal(n: Z, resOpt: Option[Result], initial: B, noBacktrack: B): Either[Result, Z] = {
          |    resOpt match {
          |      case Some(res) => return Either.Left(res)
          |      case _ =>
          |        return if (noBacktrack) Either.Right(if (!initial) -n else n) else Either.Right(n)
          |    }
          |  }
          |}
          |
          |@datatype class ${name}Lexer(input: String, docInfo: message.DocInfo) {
          |
          |  val cis: ISZ[C] = conversions.String.toCis(input)
          |
          |  ${(lexerDefs, "\n\n")}
          |
          |}"""
    )
  }

  @strictpure def predictName(name: String): ST = st"predict${ops.StringOps(name).firstToUpper}"

  @strictpure def parseName(name: String): ST = st"parse${ops.StringOps(name).firstToUpper}"

  @strictpure def lexName(name: String): ST = st"lex_$name"

  @strictpure def dfaName(name: String): ST = st"dfa_$name"

  def genParserDfa(k: Z, memoize: B, name: ST, ruleName: String, dfa: Dfa, atoms: ISZ[AST.Element],
                   laMap: HashMap[String, LookAhead.Trie]): ST = {
    val noBacktrack: B = ops.StringOps(ruleName).endsWith("_cut")
    var transitions = ISZ[ST]()
    for (node <- dfa.g.nodesInverse) {
      var conds = ISZ[ST]()
      val outgoing = dfa.g.outgoing(node)
      if (!(outgoing.size === 1 && Dfa.isReject(outgoing(0)))) {
        for (e <- outgoing) {
          val edge = e.asInstanceOf[Graph.Edge.Data[Z, (C, C)]]
          val lo = conversions.U32.toZ(conversions.C.toU32(edge.data._1))
          val hi = conversions.U32.toZ(conversions.C.toU32(edge.data._2))
          for (i <- lo to hi) {
            atoms(i) match {
              case e: AST.Element.Char =>
                val s = conversions.String.fromCis(ISZ(e.value))
                conds = conds :+
                  st"""if (!found && tokens(j).text === "${ops.StringOps(s).escapeST}") {
                      |  trees = trees :+ tokens(j)
                      |  j = j + 1
                      |  update(u32"${edge.dest}")
                      |  found = T
                      |}"""
              case e: AST.Element.Str =>
                conds = conds :+
                  st"""if (!found && tokens(j).text === "${ops.StringOps(e.value).escapeST}") {
                      |  trees = trees :+ tokens(j)
                      |  j = j + 1
                      |  update(u32"${edge.dest}")
                      |  found = T
                      |}"""
              case e: AST.Element.Ref =>
                if (e.isTerminal) {
                  conds = conds :+
                    st"""if (!found && tokens(j).ruleName === "${ops.StringOps(e.name).escapeST}") {
                        |  trees = trees :+ tokens(j)
                        |  j = j + 1
                        |  update(u32"${edge.dest}")
                        |  found = T
                        |}"""
                } else {
                  conds = conds :+
                    st"""if (!found && ${predictName(e.name)}(j)) {
                        |  ${parseName(e.name)}(j) match {
                        |    case Either.Left(r) =>
                        |      trees = trees :+ r.tree
                        |      j = r.newIndex
                        |      update(u32"${edge.dest}")
                        |      found = T
                        |    case res@Either.Right(r) =>
                        |      if (r < 0) {
                        |        return res
                        |      } else if (max < r) {
                        |        max = r
                        |      }
                        |  }
                        |}"""
                }
              case e => halt(s"Infeasible: $e")
            }
          }
        }
      }
      if (conds.isEmpty) {
        transitions = transitions :+
          st"""case u32"$node" => return retVal(max, resOpt, initial, ${if (noBacktrack) "T" else "F"})"""
      } else {
        transitions = transitions :+
          st"""case u32"$node" =>
              |  var found = F
              |  ${(conds, "\n")}
              |  if (!found) {
              |    return retVal(max, resOpt, initial, ${if (noBacktrack) "T" else "F"})
              |  }"""
      }
    }
    val r =
      st"""${if (memoize) "@memoize" else "@pure"} def $name(i: Z): Either[Result, Z] = {
          |  var state = u32"0"
          |  var resOpt: Option[Result] = None()
          |  var trees = ISZ[ParseTree]()
          |  var j = i
          |  var max = i
          |  var initial = T
          |
          |  def update(newState: U32): Unit = {
          |    initial = F
          |    state = newState
          |    state match {
          |      ${(for (s <- dfa.accepting.elements) yield st"""case u32"$s" => """, "\n")}
          |      case _ => return
          |    }
          |    resOpt = Some(Result(ParseTree.Node(trees, $tqs$ruleName$tqs,  None()), j))
          |  }
          |
          |  while (j < tokens.size) {
          |    state match {
          |      ${(transitions, "\n")}
          |      case _ => halt("Infeasible")
          |    }
          |    if (max < j) {
          |      max = j
          |    }
          |  }
          |
          |  return retVal(j, resOpt, initial, ${if (noBacktrack) "T" else "F"})
          |}"""
    return r
  }

  def genTries(k: Z, ruleTrie: LookAhead.Trie): ISZ[ST] = {
    var r = ISZ[ST]()

    var offs = HashSSet.empty[Z]

    def rec(depth: Z, trie: LookAhead.Trie): ST = {
      val (idx, size): (ST, ST) = if (depth == 0) {
        (st"j", st"?")
      } else {
        offs = offs + depth
        (st"j$depth", st"off$depth")
      }
      val subs: ISZ[ST] =
        if (trie.accept || depth >= k) ISZ()
        else for (sub <- trie.subs.values) yield rec(depth + 1, sub)
      val subsOpt: Option[ST] = if (subs.isEmpty) None() else Some(st" && (${(subs, " || ")})")
      val cond: ST = trie.value match {
        case v: LookAhead.Case.Value.Str => st"""tokens($idx).text === "${ops.StringOps(v.value).escapeST}""""
        case v: LookAhead.Case.Value.Terminal => st"""tokens($idx).ruleName === "${v.name}""""
      }
      return if (depth == 0) st"$cond$subsOpt" else st"$size && $cond$subsOpt"
    }
    if (ruleTrie.accept || ruleTrie.subs.isEmpty) {
      r = r :+ st"shouldTry = T"
    } else {
      for (trie <- ruleTrie.subs.values) {
        val cond = rec(0, trie)
        r = r :+
          st"""if (!shouldTry && $cond) {
              |  shouldTry = T
              |}"""
      }
    }

    var vars = ISZ[ST]()
    for (i <- offs.elements) {
      vars = vars :+ st"val j$i = j + $i"
      vars = vars :+ st"val off$i = j$i < tokens.size"
    }
    return vars ++ r
  }

  def genLexerDfa(name: ST, dfa: Dfa): ST = {
    var transitions = ISZ[ST]()
    for (node <- dfa.g.nodesInverse) {
      var conds = ISZ[ST]()
      val outgoing = dfa.g.outgoing(node)
      if (!(outgoing.size === 1 && Dfa.isReject(outgoing(0)))) {
        for (e <- outgoing) {
          val edge = e.asInstanceOf[Graph.Edge.Data[Z, (C, C)]]
          val (lo, hi) = edge.data
          val cond: ST = if (lo == hi) st"c === ${c2ST(lo)}" else st"${c2ST(lo)} <= c && c <= ${c2ST(hi)}"
          conds = conds :+
            st"""if (!found && $cond) {
                |  update(u32"${edge.dest}")
                |  found = T
                |}"""
        }
      }
      if (conds.isEmpty) {
        transitions = transitions :+
          st"""case u32"$node" => return afterAcceptIndex"""
      } else {
        transitions = transitions :+
          st"""case u32"$node" =>
              |  val c = cis(j)
              |  var found = F
              |  ${(conds, "\n")}
              |  if (!found) {
              |    return afterAcceptIndex
              |  }"""
      }
    }
    val r =
      st"""@pure def $name(i: Z): Z = {
          |  var state = u32"0"
          |  var afterAcceptIndex: Z = -1
          |  var j = i
          |
          |  def update(newState: U32): Unit = {
          |     state = newState
          |     state match {
          |       ${(for (s <- dfa.accepting.elements) yield st"""case u32"$s" => """, "\n")}
          |       case _ => return
          |     }
          |     afterAcceptIndex = j + 1
          |  }
          |
          |  while (j < cis.size) {
          |    state match {
          |      ${(transitions, "\n")}
          |      case _ => halt("Infeasible")
          |    }
          |    j = j + 1
          |  }
          |  return afterAcceptIndex
          |}"""
    return r
  }

  def c2ST(c: C): ST = {
    if (c == '\u0000') {
      return st"minChar"
    } else if (c == Dfa.maxChar) {
      return st"maxChar"
    } else if (c > '\uFFFF') {
      return st"""toC(u32"0x${conversions.C.toU32(c)}")"""
    } else {
      c.native match {
        case '\'' =>
          return st"'\\''"
        case '\\' =>
          return st"'\\\\'"
        case _ if '\u0020' <= c && c <= '\u007E' =>
          return st"'$c'"
        case _ =>
          val hex = ops.COps(c).toUnicodeHex
          return st"'\\u${hex._1}${hex._2}${hex._3}${hex._4}'"
      }
    }
  }

  def genLiteralC(c: C, name: String): ST = {
    val r: ST =
      st"""@pure def $name(i: Z): Z = {
          |  if (i < cis.size && cis(i) === ${c2ST(c)}) {
          |    return i + 1
          |  }
          |  return -1
          |}"""
    return r
  }

  def genLiteralString(s: String, name: String): ST = {
    val cis = conversions.String.toCis(s)
    val sts: ISZ[ST] = for (i <- cis.indices) yield
      if (i == 0) st"cis(i) === ${c2ST(cis(i))}" else st"cis(i + $i) === ${c2ST(cis(i))}"
    val r =
      st"""@pure def $name(i: Z): Z = {
          |  if (i + ${s.size} >= cis.size) {
          |    return -1
          |  }
          |  if (${(sts, " && ")}) {
          |    return i + ${s.size}
          |  }
          |  return -1
          |}"""
    return r
  }

  def genNameH(c: C): ST = {
    if (c === '$') {
      return st"_"
    }
    if ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || '0' <= c && c <= '9' || c == '_') {
      return st"$c"
    }
    val hex = ops.COps(c).toUnicodeHex
    return st"u${hex._1}${hex._2}${hex._3}${hex._4}"
  }

  @memoize def genNameC(c: C): ST = {
    return st"lit_${genNameH(c)}"
  }

  @memoize def genNameString(s: String): ST = {
    val cis = conversions.String.toCis(s)
    return st"lit_${(for (c <-cis) yield genNameH(c), "")}"
  }

  def literalCharStrNames(ast: AST.Grammar): HashSMap[Either[C, String], String] = {
    var r = HashSMap.empty[Either[C, String], String]
    var seen = HashSet.empty[String]
    def collectElement(e: AST.Element): Unit = {
      e match {
        case e: AST.Element.Block =>
          for (alt <- e.alts; el <- alt.elements) {
            collectElement(el)
          }
        case e: AST.Element.Range => collectElement(e.from); collectElement(e.to)
        case e: AST.Element.Opt => collectElement(e.element)
        case e: AST.Element.Star => collectElement(e.element)
        case e: AST.Element.Plus => collectElement(e.element)
        case e: AST.Element.Neg => collectElement(e.element)
        case _: AST.Element.Dot =>
        case _: AST.Element.Ref =>
        case e: AST.Element.Char =>
          if (seen.contains(e.value.string)) {
            return
          }
          r = r + Either.Left(e.value) ~> genNameC(e.value).render
          seen = seen + e.value.string
        case e: AST.Element.Str =>
          if (seen.contains(e.value)) {
            return
          }
          r = r + Either.Right(e.value) ~> genNameString(e.value).render
          seen = seen + e.value
      }
    }
    for (r <- ast.rules if !r.isLexer; alt <- r.alts; e <- alt.elements) {
      collectElement(e)
    }
    return r
  }
}

object ParserGenerator {

  val kind: String = "Parser Generator"

  @ext("DfaBuilder") object Ext {
    def computeParserDfas(ast: AST.Grammar): HashSMap[String, (Dfa, ISZ[AST.Element])] = $
    def computeLexerDfas(ast: AST.Grammar, reporter: Reporter): HashSMap[String, Dfa] = $
  }
}
