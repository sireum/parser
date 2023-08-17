// #Sireum
/*
 Copyright (c) 2017-2023, Robby, Kansas State University
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
          memoize: B, predictive: B, backtracking: B, reporter: Reporter): Option[ST] = {

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
    val predict: B = if (kOpt.isEmpty) F else predictive

    val lexerDfaMap = ParserGenerator.Ext.computeLexerDfas(ast, reporter)

    if (reporter.hasError) {
      return None()
    }

    var maxNumOfStates: Z = 0
    var objectVals = ISZ[ST]()
    var lexerDefs = ISZ[ST]()

    def genLexer(): Unit = {

      val literalNameMap = literalCharStrNames(ast)
      var typ = 0

      def lexST(lexname: ST, recognizerName: ST, tokenString: ST, tokenTipe: ST, isHidden: B): ST = {
        val r = st"""@pure def $lexname(index: Z): Option[Result] = { return lexH(index, $recognizerName(index), $tokenString, $tokenTipe, ${if (isHidden) "T" else "F"}) }"""
        typ = typ + 1
        return r
      }

      var lexNames = ISZ[ST]()

      for (p <- literalNameMap.entries) {
        val s: String = p._1 match {
          case Either.Left(c) =>
            lexerDefs = lexerDefs :+ genLiteralC(c, p._2)
            c.string
          case Either.Right(s) =>
            lexerDefs = lexerDefs :+ genLiteralString(s, p._2)
            s
        }
        val lexname = lexName(ops.StringOps(p._2).substring(4, p._2.size))
        lexNames = lexNames :+ lexname
        val valcode = valCode(s)
        val sEscape = escape(s)
        val valueST = st"""u32"0x${(valcode, "")}" /* "$sEscape" */"""
        objectVals = objectVals :+ st"""val T_$valcode: U32 = $valueST"""
        lexerDefs = lexerDefs :+ lexST(lexname, st"${p._2}", st"$tqs'$s'$tqs", valueST, F)
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
        val numOfStates = p._2.g.nodes.size
        if (numOfStates > maxNumOfStates) {
          maxNumOfStates = numOfStates
        }
        val tname = p._1
        val dfaname = dfaName(tname)
        lexerDefs = lexerDefs :+ genLexerDfa(dfaname, p._2)

        val lexname = lexName(tname)
        lexNames = lexNames :+ lexname
        val valcode = valCode(tname)
        val valueST = st"""u32"0x${(valcode, "")}""""
        objectVals = objectVals :+ st"""val T_$tname: U32 = $valueST"""
        lexerDefs = lexerDefs :+ lexST(lexname, dfaname, st"$tqs${p._1}$tqs", valueST, hiddenNames.contains(p._1))
      }

      lexerDefs = lexerDefs :+
        st"""@pure def hidden(i: Z): Z = {
            |   var j: Z = -1
            |   ${(hiddens, "\n")}
            |   return -1
            |}"""

      lexerDefs = lexerDefs :+
        st"""@pure def lexH(index: Z, newIndex: Z, name: String, tipe: U32, isHidden: B): Option[Result] = {
            |  if (newIndex > 0) {
            |    return Some(Result.create(ParseTree.Leaf(conversions.String.fromCis(for (i <- index until newIndex) yield cis.at(i)),
            |      name, tipe, isHidden, cis.posOpt(index, newIndex - index)), newIndex))
            |  } else {
            |    return None()
            |  }
            |}"""

      lexerDefs =
        st"""def tokenizeAll(skipHidden: B, stopAtError: B, reporter: message.Reporter): ISZ[Result] = {
            |  var i: Z = 0
            |  var r = ISZ[Result]()
            |  var done = F
            |  while (!done && cis.has(i)) {
            |    tokenize(i, skipHidden) match {
            |      case Some(result) =>
            |        if (result.kind == Result.Kind.Normal) {
            |          i = result.newIndex
            |          r = r :+ result
            |        } else {
            |          val posOpt = cis.posOpt(i, 1)
            |          reporter.error(posOpt, kind, s"Could not recognize token")
            |          if (stopAtError) {
            |            return r
            |          }
            |          r = r :+ result(tree = errorLeaf(text = conversions.String.fromCis(ISZ(cis.at(i))), posOpt = posOpt))
            |          i = i + 1
            |        }
            |      case _ => done = T
            |    }
            |  }
            |  r = r :+ Result.create(eofLeaf, -1)
            |  return r
            |}
            |
            |@pure def tokenize(i: Z, skipHidden: B): Option[Result] = {
            |  val r = MBox(Result.error(T, i))
            |  tokenizeH(r, i)
            |  while (skipHidden && r.value.leaf.isHidden && cis.has(r.value.newIndex)) {
            |    tokenizeH(r, r.value.newIndex)
            |  }
            |  return if (skipHidden && r.value.leaf.isHidden) None() else Some(r.value)
            |}
            |
            |def tokenizeH(r: MBox[Result], i: Z): Unit = {
            |  ${(for (lexname <- lexNames) yield st"""updateToken(r, $lexname(i))""", "\n")}
            |}
            |
            |def updateToken(r: MBox[Result], rOpt: Option[Result]): Unit = {
            |  rOpt match {
            |    case Some(newR) if newR.newIndex > r.value.newIndex => r.value = newR
            |    case _ =>
            |  }
            |}""" +: lexerDefs
    }

    val parserDfaMap = ParserGenerator.Ext.computeParserDfas(ast)

    var parserDefs = ISZ[ST]()

    val nameRuleMap = HashMap.empty[String, AST.Rule] ++ (for (rule <- ast.rules if !rule.isFragment && !rule.isHidden) yield (rule.name, rule))

    val k = kOpt.get

    val laMap = LookAhead.compute(if (predict) k else 1, nameRuleMap, parserDfaMap, reporter)

    if (reporter.hasError) {
      return None()
    }

    def genParser(): Unit = {

      var condDefs = HashSSet.empty[String]

      for (p <- parserDfaMap.entries) {
        val numOfStates = p._2._1.g.nodes.size
        if (numOfStates > maxNumOfStates) {
          maxNumOfStates = numOfStates
        }
        val valueST = st"""u32"0x${valCode(p._1)}""""
        objectVals = objectVals :+ st"""val T_${p._1}: U32 = $valueST"""
        val (parserST, cds): (ST, ISZ[String]) =
          if (predict && !backtracking) genPredictiveParserDfa(k, memoize, backtracking, parseName(p._1), p._1,
            valueST, p._2._1, p._2._2)
          else genBacktrackingParserDfa(memoize, predict, parseName(p._1), p._1,
            valueST, p._2._1, p._2._2)
        parserDefs = parserDefs :+ parserST
        condDefs = condDefs ++ cds
      }

      for (cd <- condDefs.elements) {
        parserDefs = parserDefs :+ condDefST(cd)
      }

      if (predict) {
        for (p <- laMap.entries) {
          val trie = p._2
          val sts = genTries(k, trie)
          parserDefs = parserDefs :+
            st"""${if (memoize) "@memoize" else "@pure"} def ${predictName(p._1)}(j: Z): Z = {
                |  ${(sts, "\n")}
                |  return 0
                |}"""
        }
      }
    }

    genLexer()
    genParser()

    val parserOpt: Option[ST] = ast.rules match {
      case ISZ(first, _*) if !first.isLexer =>
        Some(
          st"""def parse(uriOpt: Option[String], input: String, reporter: message.Reporter): Option[ParseTree] = {
              |  val docInfo = message.DocInfo.create(uriOpt, input)
              |  val tokens = lex(input, docInfo, T, T, reporter)
              |  if (reporter.hasError) {
              |    return None()
              |  }
              |  val r = ${name}Parser(Indexable.fromIsz(tokens)).${parseName(first.name)}(0)
              |  r.kind match {
              |    case Result.Kind.Normal => return Some(r.tree)
              |    case Result.Kind.LexicalError =>
              |      reporter.error(Some(message.PosInfo(docInfo, offsetLength(r.newIndex, 1))), kind, s"Could not recognize token")
              |      return None()
              |    case Result.Kind.GrammaticalError =>
              |      val idx: Z = if (r.newIndex < 0) -r.newIndex else r.newIndex
              |      if (idx < tokens.size) {
              |        val token = tokens(idx).leaf
              |        reporter.error(token.posOpt, kind, s"Could not parse token: \"$${ops.StringOps(token.text).escapeST.render}\"")
              |      } else {
              |        val token = tokens(idx - 1).leaf
              |        reporter.error(token.posOpt, kind, "Expecting more input but reached the end")
              |      }
              |      return None()
              |  }
              |}
              |
              |def parseIndexable(input: Indexable.Pos[C], reporter: message.Reporter): Option[ParseTree] = {
              |  val it = IndexableToken(input, T)
              |  val r = ${name}Parser(it).${parseName(first.name)}(0)
              |  r.kind match {
              |    case Result.Kind.Normal => return Some(r.tree)
              |    case Result.Kind.LexicalError =>
              |      reporter.error(input.posOpt(r.newIndex, 1), kind, s"Could not recognize token")
              |      return None()
              |    case Result.Kind.GrammaticalError =>
              |      val idx: Z = if (r.newIndex < 0) -r.newIndex else r.newIndex
              |      if (it.has(idx)) {
              |        val token = it.at(idx).leaf
              |        reporter.error(token.posOpt, kind, s"Could not parse token: \"$${ops.StringOps(token.text).escapeST.render}\"")
              |      } else {
              |        val token = it.at(idx - 1).leaf
              |        reporter.error(token.posOpt, kind, "Expecting more input but reached the end")
              |      }
              |      return None()
              |  }
              |}"""
        )
      case _ => None()
    }

    val imptOpt: Option[String] = if (packageOpt.isEmpty ||
      ops.StringOps(packageOpt.get.render).trim != "package org.sireum.parser") Some(
      "import org.sireum.parser.ParseTree"
    ) else None()

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
          |$imptOpt
          |
          |object $name {
          |  @range(min = 0, max = ${maxNumOfStates - 1}) class State
          |}
          |
          |import $name.State
          |import $name.State._
          |
          |object ${name}Parser {
          |
          |  @datatype class Result(val kind: Result.Kind.Type, val tree: ParseTree, val newIndex: Z) {
          |    def leaf: ParseTree.Leaf = {
          |      return tree.asInstanceOf[ParseTree.Leaf]
          |    }
          |  }
          |
          |  object Result {
          |
          |    @enum object Kind {
          |      'Normal
          |      'LexicalError
          |      'GrammaticalError
          |    }
          |
          |    @strictpure def create(tree: ParseTree, newIndex: Z): Result =
          |      Result(Result.Kind.Normal, tree, newIndex)
          |
          |    @strictpure def error(isLexical: B, index: Z): Result =
          |      Result(if (isLexical) Result.Kind.LexicalError else Result.Kind.GrammaticalError, errorLeaf, index)
          |
          |  }
          |
          |  @record class Context(val ruleName: String,
          |                        val ruleType: U32,
          |                        val accepting: IS[State, B],
          |                        var state: State,
          |                        var resOpt: Option[Result],
          |                        var j: Z,
          |                        var max: Z,
          |                        var initial: B,
          |                        var trees: ISZ[ParseTree],
          |                        var found: B,
          |                        var failIndex: Z,
          |                        var isLexical: B) {
          |
          |    def updateTerminal(token: ParseTree.Leaf, newState: State): Unit = {
          |      found = T
          |      j = j + 1
          |      initial = F
          |      state = newState
          |      trees = trees :+ token
          |      if (accepting(state)) {
          |        resOpt = Some(Result.create(ParseTree.Node(trees, ruleName, ruleType), j))
          |      }
          |    }
          |
          |    def updateNonTerminal(r: Result, newState: State): Unit = {
          |      found = T
          |      initial = F
          |      j = r.newIndex
          |      state = newState
          |      trees = trees :+ r.tree
          |      if (accepting(state)) {
          |        resOpt = Some(Result.create(ParseTree.Node(trees, ruleName, ruleType), j))
          |      }
          |    }
          |  }
          |
          |  object Context {
          |    @pure def create(ruleName: String, ruleType: U32, accepts: ISZ[State], i: Z): Context = {
          |      val accepting = MS.create[State, B]($maxNumOfStates, F)
          |      for (accept <- accepts) {
          |        accepting(accept) = T
          |      }
          |      return Context(
          |        ruleName = ruleName,
          |        ruleType = ruleType,
          |        accepting = accepting.toIS,
          |        state = state"0",
          |        resOpt = None(),
          |        trees = ISZ[ParseTree](),
          |        j = i,
          |        max = i,
          |        initial = T,
          |        found = F,
          |        failIndex = 0,
          |        isLexical = F
          |      )
          |    }
          |  }
          |
          |  @record class LContext(val accepting: IS[State, B], var state: State, var j: Z, var afterAcceptIndex: Z, var found: B) {
          |    def update(newState: State): Unit = {
          |      state = newState
          |      found = T
          |      if (accepting(state)) {
          |        afterAcceptIndex = j + 1
          |      }
          |    }
          |  }
          |
          |  object LContext {
          |    @pure def create(accepts: ISZ[State], i: Z): LContext = {
          |      val accepting = MS.create[State, B]($maxNumOfStates, F)
          |      for (accept <- accepts) {
          |        accepting(accept) = T
          |      }
          |      return LContext(accepting = accepting.toIS, state = state"0", j = i, afterAcceptIndex = -1, found = F)
          |    }
          |  }
          |
          |  @datatype class IndexableToken(val input: Indexable.Pos[C], val skipHidden: B) extends Indexable[Result] {
          |    val lexer: ${name}Lexer = ${name}Lexer(input)
          |
          |    override def at(i: Z): Result = {
          |      return _at(i)
          |    }
          |
          |    override def has(i: Z): B = {
          |      return _has(i)
          |    }
          |
          |    @memoize def _has(i: Z): B = {
          |      assert(i >= 0)
          |      if (i == 0) {
          |        return T
          |      }
          |      if (!_has(i - 1)) {
          |        return F
          |      }
          |      val prev = _at(i - 1)
          |      return prev.kind == Result.Kind.Normal && prev.newIndex != -1
          |    }
          |
          |    @memoize def _at(i: Z): Result = {
          |      if (i == 0) {
          |        if (input.has(0)) {
          |          lexer.tokenize(0, skipHidden) match {
          |            case Some(result) => return result
          |            case _ =>
          |          }
          |        }
          |      } else {
          |        val prev = _at(i - 1)
          |        if (input.has(prev.newIndex)) {
          |          lexer.tokenize(prev.newIndex, skipHidden) match {
          |            case Some(result) => return result
          |            case _ =>
          |          }
          |        }
          |      }
          |      return Result(Result.Kind.Normal, eofLeaf, -1)
          |    }
          |
          |  }
          |
          |  val kind: String = "${name}Parser"
          |
          |  val minChar: C = '\u0000'
          |  val maxChar: C = toC(u32"0x0010FFFF")
          |
          |  ${(objectVals, "\n")}
          |
          |  val errorLeaf: ParseTree.Leaf = ParseTree.Leaf("<ERROR>", "<ERROR>", u32"0x${(valCode("<ERROR>"), "")}", F, None())
          |  val eofLeaf: ParseTree.Leaf = ParseTree.Leaf("<EOF>", "EOF", u32"0x${(valCode("EOF"), "")}", F, None())
          |
          |  $parserOpt
          |
          |  def lex(input: String, docInfo: message.DocInfo, skipHidden: B, stopAtError: B,
          |          reporter: message.Reporter): ISZ[Result] = {
          |    return ${name}Lexer(Indexable.fromIszDocInfo(conversions.String.toCis(input), docInfo)).tokenizeAll(skipHidden, stopAtError, reporter)
          |  }
          |
          |  @pure def offsetLength(offset: Z, length: Z): U64 = {
          |    return (conversions.Z.toU64(offset) << u64"32") | (conversions.Z.toU64(length) & u64"0xFFFFFFFF")
          |  }
          |
          |}
          |
          |import ${name}Parser._
          |
          |@datatype class ${name}Parser(tokens: Indexable[Result]) {
          |
          |  ${(parserDefs, "\n\n")}
          |
          |  def retVal(n: Z, resOpt: Option[Result], initial: B, noBacktrack: B): Result = {
          |    resOpt match {
          |      case Some(res) => return res
          |      case _ => return Result.error(F, if (noBacktrack && !initial) -n else n)
          |    }
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
          |}
          |
          |@datatype class ${name}Lexer(cis: Indexable.Pos[C]) {
          |
          |  ${(lexerDefs, "\n\n")}
          |
          |}"""
    )
  }

  @memoize def valCode(name: String): ISZ[U8] = {
    val sha3 = crypto.SHA3.init256
    sha3.update(conversions.String.toU8is(name))
    return ops.ISZOps(sha3.finalise()).take(4)
  }

  @pure def predictName(name: String): ST = {
    return st"predict${ops.StringOps(name).firstToUpper}"
  }

  @pure def parseName(name: String): ST = {
    return st"parse${ops.StringOps(name).firstToUpper}"
  }

  @strictpure def lexName(name: String): ST = st"lex_$name"

  @strictpure def dfaName(name: String): ST = st"dfa_$name"

  @pure def edgesOf(dfa: Dfa, node: Z): ISZ[(Z, Z, Z)] = {
    val outgoing = dfa.g.outgoing(node)
    if (outgoing.size == 1 && Dfa.isReject(outgoing(0))) {
      return ISZ()
    }
    var r = ISZ[(Z, Z, Z)]()
    for (e <- outgoing) {
      val edge = e.asInstanceOf[Graph.Edge.Data[Z, (C, C)]]
      val lo = conversions.U32.toZ(conversions.C.toU32(edge.data._1))
      val hi = conversions.U32.toZ(conversions.C.toU32(edge.data._2))
      r = r :+ ((edge.dest, lo, hi))
    }
    return r
  }

  def genPredictiveParserDfa(k: Z, memoize: B, backtracking: B, name: ST, ruleName: String, valueST: ST, dfa: Dfa,
                             atoms: ISZ[AST.Element]): (ST, ISZ[String]) = {
    val noBacktrack: B = !backtracking || ops.StringOps(ruleName).endsWith("_cut")
    var transitions = ISZ[ST]()
    var condDefs = HashSSet.empty[String]
    for (node <- dfa.g.nodesInverse) {
      var refNameDests = HashSSet.empty[(String, Z)]
      var cases = ISZ[ST]()
      for (t <- edgesOf(dfa, node)) {
        val (dest, lo, hi) = t
        for (i <- lo to hi) {
          atoms(i) match {
            case e: AST.Element.Ref if !e.isTerminal =>
              refNameDests = refNameDests + ((e.name, dest))
              condDefs = condDefs + e.name
            case _ =>
          }
        }
      }
      for (t <- edgesOf(dfa, node)) {
        val (dest, lo, hi) = t
        for (i <- lo to hi) {
          atoms(i) match {
            case e: AST.Element.Char =>
              cases = cases :+ terminalST(conversions.String.fromCis(ISZ(e.value)), dest, F, None())
            case e: AST.Element.Str =>
              cases = cases :+ terminalST(e.value, dest, F, None())
            case e: AST.Element.Ref if e.isTerminal =>
              cases = cases :+ terminalST(e.name, dest, T, None())
            case _ =>
          }
        }
      }
      if (cases.isEmpty && refNameDests.isEmpty) {
        transitions = transitions :+
          st"""case state"$node" => return retVal(ctx.max, ctx.resOpt, ctx.initial, ${if (noBacktrack) "T" else "F"})"""
      } else {
        val matchOpt: Option[ST] = if (cases.isEmpty) None() else Some(
          if (refNameDests.isEmpty)
            st"""token.tipe match {
                |  ${(cases, "\n")}
                |  case _ =>
                |}"""
          else
            st"""if (!ctx.found) {
                |  token.tipe match {
                |    ${(cases, "\n")}
                |    case _ =>
                |  }
                |}"""
        )
        var refs = ISZ[ST]()
        var checkRefs = ISZ[ST]()
        for (p <- refNameDests.elements) {
          refs = refs :+ st"""val n_${p._1} = ${predictName(p._1)}(ctx.j)"""
          if (refNameDests.size > 1) {
            checkRefs = checkRefs :+
              st"""if (n_${p._1} == n && ${parseName(p._1)}H(ctx, state"${p._2}")) {
                  |  return Result.error(ctx.isLexical, ctx.failIndex)
                  |}"""
          }
        }
        val checkRefsOpt: Option[ST] = if (refNameDests.isEmpty) {
          None()
        } else {
          if (refNameDests.size == 1) {
            val p = refNameDests.elements(0)
            Some(
              st"""if (n_${p._1} > 0 && ${parseName(p._1)}H(ctx, state"${p._2}")) {
                  |  return Result.error(ctx.isLexical, ctx.failIndex)
                  |}"""
            )
          } else {
            Some(
              st"""for (n <- $k to 1 by -1 if !ctx.found) {
                  |  ${(checkRefs, " else ")}
                  |}"""
            )
          }
        }
        transitions = transitions :+
          st"""case state"$node" =>
              |  ctx.found = F
              |  ${(refs, "\n")}
              |  $checkRefsOpt
              |  $matchOpt
              |  if (!ctx.found) {
              |    return retVal(ctx.max, ctx.resOpt, ctx.initial, ${if (noBacktrack) "T" else "F"})
              |  }"""
      }
    }
    return (parserDfaST(memoize, name, ruleName, valueST, dfa, transitions, noBacktrack), condDefs.elements)
  }

  def genBacktrackingParserDfa(memoize: B, predict: B, name: ST, ruleName: String, valueST: ST, dfa: Dfa,
                               atoms: ISZ[AST.Element]): (ST, ISZ[String]) = {
    var transitions = ISZ[ST]()
    var condDefs = HashSSet.empty[String]
    for (node <- dfa.g.nodesInverse) {
      var cases = ISZ[ST]()
      var notFoundOpt: Option[ST] = None()
      var notFoundRefOpt: Option[ST] = None()
      for (t <- edgesOf(dfa, node)) {
        val (dest, lo, hi) = t
        for (i <- lo to hi) {
          atoms(i) match {
            case e: AST.Element.Char =>
              cases = cases :+ terminalST(conversions.String.fromCis(ISZ(e.value)), dest, F, notFoundOpt)
            case e: AST.Element.Str =>
              cases = cases :+ terminalST(e.value, dest, F, notFoundOpt)
            case e: AST.Element.Ref =>
              if (e.isTerminal) {
                cases = cases :+ terminalST(e.name, dest, T, notFoundOpt)
              } else {
                if (notFoundOpt.isEmpty) {
                  notFoundOpt = Some(st" if !ctx.found")
                }
                val predictOpt: Option[ST] = if (predict) Some(st"${predictName(e.name)}(ctx.j) > 0 && ") else None()
                val parsename = parseName(e.name)
                condDefs = condDefs + e.name
                cases = cases :+
                  st"""case _ if $notFoundRefOpt$predictOpt${parsename}H(ctx, state"$dest") => return Result.error(ctx.isLexical, ctx.failIndex)"""
              }
            case e => halt(s"Infeasible: $e")
          }
          if (notFoundRefOpt.isEmpty) {
            notFoundRefOpt = Some(st"!ctx.found && ")
          }
        }
      }
      if (cases.isEmpty) {
        transitions = transitions :+ st"""case state"$node" => return retVal(ctx.max, ctx.resOpt, ctx.initial, F)"""
      } else {
        val matchOpt: Option[ST] = if (cases.isEmpty) None() else Some(
          st"""token.tipe match {
              |  ${(cases, "\n")}
              |  case _ =>
              |}"""
        )
        transitions = transitions :+
          st"""case state"$node" =>
              |  ctx.found = F
              |  $matchOpt
              |  if (!ctx.found) {
              |    return retVal(ctx.max, ctx.resOpt, ctx.initial, F)
              |  }"""
      }
    }
    return (parserDfaST(memoize, name, ruleName, valueST, dfa, transitions, F), condDefs.elements)
  }

  @pure def condDefST(name: String): ST = {
    return st"""def ${parseName(name)}H(ctx: Context, nextState: State): B = {
               |  val r = ${parseName(name)}(ctx.j)
               |  r.kind match {
               |    case Result.Kind.Normal => ctx.updateNonTerminal(r, nextState)
               |    case Result.Kind.LexicalError =>
               |      ctx.failIndex = r.newIndex
               |      ctx.isLexical = T
               |      return T
               |    case Result.Kind.GrammaticalError =>
               |      val index = r.newIndex
               |      if (index < 0) {
               |        ctx.failIndex = index
               |        return T
               |      } else if (ctx.max < index) {
               |        ctx.max = index
               |      }
               |  }
               |  return F
               |}"""
  }

  @pure def parserDfaST(memoize: B, name: ST, ruleName: String, valueST: ST, dfa: Dfa,
                              transitions: ISZ[ST], noBacktrack: B): ST = {
    return st"""${if (memoize) "@memoize" else "@pure"} def $name(i: Z): Result = {
               |  val ctx = Context.create("$ruleName", $valueST, ISZ(${(for (s <- dfa.accepting.elements) yield st"""state"$s"""", ", ")}), i)
               |
               |  while (tokens.has(ctx.j)) {
               |    val token: ParseTree.Leaf = {
               |      val result = tokens.at(ctx.j)
               |      if (result.kind != Result.Kind.Normal) {
               |        return result
               |      }
               |      result.leaf
               |    }
               |    ctx.state match {
               |      ${(transitions, "\n")}
               |      case _ => halt("Infeasible")
               |    }
               |    if (ctx.max < ctx.j) {
               |      ctx.max = ctx.j
               |    }
               |  }
               |
               |  return retVal(ctx.j, ctx.resOpt, ctx.initial, ${if (noBacktrack) "T" else "F"})
               |}"""
  }

  @pure def terminalST(text: String, dest: Z, plain: B, notFoundOpt: Option[ST]): ST = {
    return st"""case u32"0x${(valCode(text), "")}" /* ${if (plain) text else st"\"${escape(text)}\"" } */$notFoundOpt => ctx.updateTerminal(token, state"$dest")"""
  }

  def genTries(k: Z, ruleTrie: LookAhead.Trie): ISZ[ST] = {
    var r = ISZ[ST]()

    var offs = HashSSet.empty[Z]

    def rec(depth: Z, trie: LookAhead.Trie): ST = {
      val subs: ISZ[ST] =
        if (depth >= k) ISZ()
        else for (sub <- trie.subs.values) yield rec(depth + 1, sub)
      val subsST: ST = if (subs.isEmpty) {
        st"return $depth"
      } else {
        offs = offs + depth
        val idx = st"j$depth"
        val size = st"hasJ$depth"
        val acceptOpt: Option[ST] = if (trie.accept) Some(st"return $depth") else None()
        st"""if ($size) {
            |  val tokenJ$depth = tokens.at($idx)
            |  if (tokenJ$depth.kind == Result.Kind.Normal) {
            |    tokenJ$depth.leaf.tipe match {
            |      ${(subs, "\n")}
            |      case _ =>
            |    }
            |  }
            |}
            |$acceptOpt"""
      }
      val cond: ST = trie.value match {
        case v: LookAhead.Case.Value.Str => st"""case u32"0x${valCode(v.value)}" /* "${escape(v.value)}" */"""
        case v: LookAhead.Case.Value.Terminal => st"""case u32"0x${(valCode(v.name), "")}" /* ${v.name} */"""
      }
      val rf: ST = if (subs.isEmpty) st"$cond => $subsST" else
        st"""$cond =>
            |  $subsST"""
      return rf
    }

    if (!(ruleTrie.accept || ruleTrie.subs.isEmpty)) {
      r = r :+
        st"""val tokenJ = tokens.at(j)
            |if (tokenJ.kind == Result.Kind.Normal) {
            |  tokenJ.leaf.tipe match {
            |    ${(for (trie <- ruleTrie.subs.values) yield rec(1, trie), "\n")}
            |    case _ =>
            |  }
            |}"""
    }

    var vars = ISZ[ST]()
    for (i <- offs.elements) {
      vars = vars :+ st"val j$i = j + $i"
      vars = vars :+ st"val hasJ$i = tokens.has(j$i)"
    }
    return vars ++ r
  }

  def genLexerDfa(name: ST, dfa: Dfa): ST = {
    var transitions = ISZ[ST]()
    for (node <- dfa.g.nodesInverse) {
      var conds = ISZ[ST]()
      val outgoing = dfa.g.outgoing(node)
      if (!(outgoing.size == 1 && Dfa.isReject(outgoing(0)))) {
        var destMap = HashSMap.empty[Z, ISZ[(C, C)]]
        for (e <- outgoing) {
          val edge = e.asInstanceOf[Graph.Edge.Data[Z, (C, C)]]
          val ds: ISZ[(C, C)] = destMap.get(e.dest) match {
            case Some(l) => l
            case _ => ISZ()
          }
          destMap = destMap + edge.dest ~> (ds :+ edge.data)
        }
        for (p <- destMap.entries) {
          val dest = p._1
          var cs = ISZ[ST]()
          for (data <- p._2) {
            val (lo, hi) = data
            cs = cs :+ (if (lo == hi) st"c == ${c2ST(lo)}" else st"${c2ST(lo)} <= c && c <= ${c2ST(hi)}")
          }
          conds = conds :+
            st"""if (${(cs, " || ")}) {
                |  ctx.update(state"$dest")
                |}"""
        }
      }
      if (conds.isEmpty) {
        transitions = transitions :+
          st"""case state"$node" => return ctx.afterAcceptIndex"""
      } else {
        transitions = transitions :+
          st"""case state"$node" =>
              |  val c = cis.at(ctx.j)
              |  ctx.found = F
              |  ${(conds, " else ")}
              |  if (!ctx.found) {
              |    return ctx.afterAcceptIndex
              |  }"""
      }
    }
    val r =
      st"""@pure def $name(i: Z): Z = {
          |  val ctx = LContext.create(ISZ(${(for (s <- dfa.accepting.elements) yield st"""state"$s"""", ", ")}), i)
          |
          |  while (cis.has(ctx.j)) {
          |    ctx.state match {
          |      ${(transitions, "\n")}
          |      case _ => halt("Infeasible")
          |    }
          |    ctx.j = ctx.j + 1
          |  }
          |  return ctx.afterAcceptIndex
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
          |  if (cis.has(i) && cis.at(i) == ${c2ST(c)}) {
          |    return i + 1
          |  }
          |  return -1
          |}"""
    return r
  }

  def genLiteralString(s: String, name: String): ST = {
    val cis = conversions.String.toCis(s)
    val sts: ISZ[ST] = for (i <- cis.indices) yield
      if (i == 0) st"cis.at(i) == ${c2ST(cis(i))}" else st"cis.at(i + $i) == ${c2ST(cis(i))}"
    val r =
      st"""@pure def $name(i: Z): Z = {
          |  if (!cis.has(i + ${s.size - 1})) {
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
    if (c == '$') {
      return st"_"
    }
    if ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || '0' <= c && c <= '9' || c == '_') {
      return st"$c"
    }
    val hex = ops.COps(c).toUnicodeHex
    return st"u${hex._1}${hex._2}${hex._3}${hex._4}"
  }

  @memoize def escape(s: String): ST = {
    return ops.StringOps(s).escapeST
  }

  @memoize def genNameC(c: C): ST = {
    return st"lit_${genNameH(c)}"
  }

  @memoize def genNameString(s: String): ST = {
    val cis = conversions.String.toCis(s)
    return st"lit_${(for (c <- cis) yield genNameH(c), "")}"
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
