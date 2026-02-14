// #Sireum
/*
 Copyright (c) 2017-2026,Robby, Kansas State University
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
import org.sireum.U32._
import org.sireum.automaton._

object LLkParserGenerator {
  val minChar: C = '\u0000'
  val maxChar: C = Dfa.maxChar

  @strictpure def c2z(c: C): Z = conversions.U32.toZ(conversions.C.toU32(c))

  @strictpure def z2c(z: Z): C = conversions.U32.toC(conversions.Z.toU32(z))

  /** Computes the complement of a set of character ranges with respect to [minChar, maxChar]. */
  def complementRanges(ranges: ISZ[(C, C)]): ISZ[(C, C)] = {
    if (ranges.isEmpty) {
      return ISZ((minChar, maxChar))
    }
    val sorted = ops.ISZOps(ranges).sortWith((a, b) => c2z(a._1) < c2z(b._1))
    var result = ISZ[(C, C)]()
    var pos = c2z(minChar)
    for (r <- sorted) {
      val rStart = c2z(r._1)
      val rEnd = c2z(r._2)
      if (pos < rStart) {
        result = result :+ (z2c(pos), z2c(rStart - 1))
      }
      if (rEnd + 1 > pos) {
        pos = rEnd + 1
      }
    }
    if (pos <= c2z(maxChar)) {
      result = result :+ (z2c(pos), maxChar)
    }
    return result
  }

  /** Creates a two-state NFA fragment with a single character-range transition. */
  def nfaSingle(ns: Z, label: (C, C)): Nfa[(C, C)] = {
    val g = (Graph.empty[Z, Option[(C, C)]] * ns * (ns + 1)).addDataEdge(Some(label), ns, ns + 1)
    return Nfa(initial = ns, accept = ns + 1, g = g, nextState = ns + 2)
  }

  /** Thompson's construction for a single grammar element.
    *
    * @param e  the grammar element to convert
    * @param ns the next available state number
    * @return an NFA fragment
    */
  def nfaElement(e: GrammarAst.Element, ns: Z): Nfa[(C, C)] = {
    e match {
      case e: GrammarAst.Element.Char =>
        return nfaSingle(ns, (e.value, e.value))
      case e: GrammarAst.Element.Str =>
        val chars = conversions.String.toCis(e.value)
        if (chars.isEmpty) {
          return Nfa(initial = ns, accept = ns,
            g = Graph.empty[Z, Option[(C, C)]] * ns, nextState = ns + 1)
        }
        var nfa = nfaSingle(ns, (chars(0), chars(0)))
        var i: Z = 1
        while (i < chars.size) {
          val next = nfaSingle(nfa.nextState, (chars(i), chars(i)))
          nfa = Nfa.concat(nfa, next)
          i = i + 1
        }
        return nfa
      case e: GrammarAst.Element.Range =>
        return nfaSingle(ns, (e.from.value, e.to.value))
      case _: GrammarAst.Element.Dot =>
        return nfaSingle(ns, (minChar, maxChar))
      case e: GrammarAst.Element.Block =>
        return nfaAlts(e.alts, ns)
      case e: GrammarAst.Element.Opt =>
        val inner = nfaElement(e.element, ns + 2)
        var g = (Graph.empty[Z, Option[(C, C)]] * ns * (ns + 1))
        g = Nfa.mergeGraphs(g, inner.g)
        g = g.addDataEdge(None[(C, C)](), ns, inner.initial)
        g = g.addDataEdge(None[(C, C)](), inner.accept, ns + 1)
        g = g.addDataEdge(None[(C, C)](), ns, ns + 1)
        return Nfa(initial = ns, accept = ns + 1, g = g, nextState = inner.nextState)
      case e: GrammarAst.Element.Star =>
        val inner = nfaElement(e.element, ns + 2)
        var g = (Graph.empty[Z, Option[(C, C)]] * ns * (ns + 1))
        g = Nfa.mergeGraphs(g, inner.g)
        g = g.addDataEdge(None[(C, C)](), ns, inner.initial)
        g = g.addDataEdge(None[(C, C)](), inner.accept, ns + 1)
        g = g.addDataEdge(None[(C, C)](), ns, ns + 1)
        g = g.addDataEdge(None[(C, C)](), inner.accept, inner.initial)
        return Nfa(initial = ns, accept = ns + 1, g = g, nextState = inner.nextState)
      case e: GrammarAst.Element.Plus =>
        val inner = nfaElement(e.element, ns + 2)
        var g = (Graph.empty[Z, Option[(C, C)]] * ns * (ns + 1))
        g = Nfa.mergeGraphs(g, inner.g)
        g = g.addDataEdge(None[(C, C)](), ns, inner.initial)
        g = g.addDataEdge(None[(C, C)](), inner.accept, ns + 1)
        g = g.addDataEdge(None[(C, C)](), inner.accept, inner.initial)
        return Nfa(initial = ns, accept = ns + 1, g = g, nextState = inner.nextState)
      case e: GrammarAst.Element.Neg =>
        return nfaNeg(e.element, ns)
      case _: GrammarAst.Element.Ref =>
        halt("Lexer rule should not contain Ref after fragment inlining")
    }
  }

  /** Thompson's construction for a sequence of elements (an Alt). */
  def nfaAlt(alt: GrammarAst.Alt, ns: Z): Nfa[(C, C)] = {
    if (alt.elements.isEmpty) {
      return Nfa(initial = ns, accept = ns,
        g = Graph.empty[Z, Option[(C, C)]] * ns, nextState = ns + 1)
    }
    var nfa = nfaElement(alt.elements(0), ns)
    var i: Z = 1
    while (i < alt.elements.size) {
      val next = nfaElement(alt.elements(i), nfa.nextState)
      nfa = Nfa.concat(nfa, next)
      i = i + 1
    }
    return nfa
  }

  /** Thompson's construction for a set of alternatives (union). */
  def nfaAlts(alts: ISZ[GrammarAst.Alt], ns: Z): Nfa[(C, C)] = {
    if (alts.size == 1) {
      return nfaAlt(alts(0), ns)
    }
    var childNfas = ISZ[Nfa[(C, C)]]()
    var s = ns + 2
    var i: Z = 0
    while (i < alts.size) {
      val child = nfaAlt(alts(i), s)
      childNfas = childNfas :+ child
      s = child.nextState
      i = i + 1
    }
    var g = Graph.empty[Z, Option[(C, C)]] * ns * (ns + 1)
    for (child <- childNfas) {
      g = Nfa.mergeGraphs(g, child.g)
      g = g.addDataEdge(None[(C, C)](), ns, child.initial)
      g = g.addDataEdge(None[(C, C)](), child.accept, ns + 1)
    }
    return Nfa(initial = ns, accept = ns + 1, g = g, nextState = s)
  }

  /** Builds an NFA for the complement of an element (~e).
    *
    * Constructs DFA for e, completes it with a sink state, complements
    * accepting states, then intersects with a single-char DFA to restrict
    * the result to single-character matches.
    */
  def nfaNeg(e: GrammarAst.Element, ns: Z): Nfa[(C, C)] = {
    val innerNfa = nfaElement(e, z"0")
    val innerDfa = nfaToDfa(innerNfa)
    val allStates = innerDfa.g.nodesInverse
    val sinkState: Z = allStates.size
    var completedG = innerDfa.g * sinkState
    for (state <- allStates :+ sinkState) {
      var coveredRanges = ISZ[(C, C)]()
      for (edge <- completedG.outgoing(state)) {
        val de = edge.asInstanceOf[Graph.Edge.Data[Z, (C, C)]]
        coveredRanges = coveredRanges :+ de.data
      }
      val gaps = complementRanges(coveredRanges)
      for (gap <- gaps) {
        completedG = completedG.addDataEdge(gap, state, sinkState)
      }
    }
    var complementAccepting = HashSSet.empty[Z]
    for (state <- allStates :+ sinkState) {
      if (!innerDfa.accepting.contains(state)) {
        complementAccepting = complementAccepting + state
      }
    }
    var g = Graph.empty[Z, Option[(C, C)]] * ns * (ns + 1)
    for (edge <- completedG.outgoing(innerDfa.initial)) {
      val de = edge.asInstanceOf[Graph.Edge.Data[Z, (C, C)]]
      if (complementAccepting.contains(de.dest)) {
        g = g.addDataEdge(Some(de.data), ns, ns + 1)
      }
    }
    return Nfa(initial = ns, accept = ns + 1, g = g, nextState = ns + 2)
  }

  /** Splits overlapping character-range transitions into disjoint intervals.
    *
    * @param charTransitions each entry is (lo, hi, targetNfaState)
    * @return disjoint intervals, each with the list of target NFA states
    */
  def splitIntervals(charTransitions: ISZ[(C, C, Z)]): ISZ[(C, C, ISZ[Z])] = {
    if (charTransitions.isEmpty) {
      return ISZ()
    }
    var boundaries = HashSSet.empty[Z]
    for (t <- charTransitions) {
      boundaries = boundaries + c2z(t._1)
      boundaries = boundaries + (c2z(t._2) + 1)
    }
    val sorted = ops.ISZOps(boundaries.elements).sortWith((a, b) => a < b)
    var result = ISZ[(C, C, ISZ[Z])]()
    var i: Z = 0
    while (i < sorted.size - 1) {
      val lo = sorted(i)
      val hi = sorted(i + 1) - 1
      if (lo <= hi) {
        var targets = ISZ[Z]()
        for (t <- charTransitions) {
          if (c2z(t._1) <= lo && hi <= c2z(t._2)) {
            targets = targets :+ t._3
          }
        }
        if (targets.nonEmpty) {
          result = result :+ (z2c(lo), z2c(hi), targets)
        }
      }
      i = i + 1
    }
    return result
  }

  /** Adapts `splitIntervals` for use with `Nfa.toDfa`.
    *
    * Converts between the `(label, target)` pair format used by
    * `Nfa.toDfa` and the `(lo, hi, target)` triple format used by
    * `splitIntervals`.
    */
  def splitTransitionsCC(transitions: ISZ[((C, C), Z)]): ISZ[((C, C), ISZ[Z])] = {
    var charTransitions = ISZ[(C, C, Z)]()
    for (t <- transitions) {
      charTransitions = charTransitions :+ (t._1._1, t._1._2, t._2)
    }
    val intervals = splitIntervals(charTransitions)
    var result = ISZ[((C, C), ISZ[Z])]()
    for (interval <- intervals) {
      result = result :+ ((interval._1, interval._2), interval._3)
    }
    return result
  }

  /** Sort comparison for `(C, C)` edge signatures used by `Dfa.minimize`. */
  @strictpure def sortEdgeCC(a: ((C, C), Z), b: ((C, C), Z)): B =
    c2z(a._1._1) < c2z(b._1._1)

  /** Converts an NFA to a DFA via the subset construction algorithm. */
  def nfaToDfa(nfa: Nfa[(C, C)]): Dfa[(C, C)] = {
    return nfa.toDfa(splitTransitionsCC _)
  }

  /** Minimizes a DFA using partition refinement. */
  def minimize(dfa: Dfa[(C, C)]): Dfa[(C, C)] = {
    return dfa.minimize(sortEdgeCC _)
  }

  /** Builds a minimized DFA from a lexer rule (with fragments already inlined). */
  def buildDfa(r: GrammarAst.Rule): Dfa[(C, C)] = {
    val nfa = nfaAlts(r.alts, z"0")
    val dfa = nfaToDfa(nfa)
    return minimize(dfa)
  }

  /** Generates a parser source file that uses LexerDfas for lexing.
    *
    * Builds DFAs for all string literals and lexer rules, serializes them
    * into a compact LexerDfas, and emits a single parser object that
    * deserializes both the NGrammar and LexerDfas at load time.
    *
    * @param licenseOpt  optional license header ST
    * @param fileInfo    file information ST (e.g., generator annotation)
    * @param packageOpt  optional package declaration ST
    * @param name        the base name (e.g., "SlangLl2")
    * @param g           the parsed grammar AST
    * @param reporter    for error reporting
    * @return the generated source, or None on error
    */
  def gen(licenseOpt: Option[ST], fileInfo: ST, packageOpt: Option[ST], name: String, g: GrammarAst.Grammar, reporter: message.Reporter): Option[ST] = {
    val gn = g.normalize
    val ng: NGrammar = gn.toNGrammar match {
      case Some(n) => n
      case _ =>
        return None()
    }
    val strs = gn.collectStrs
    val lexRules: ISZ[GrammarAst.Rule] = for (r <- gn.rules if r.isLexer) yield r
    val nameMap = ng.pt.nameMap

    // Build DFA infos for string literals
    var dfaInfos = ISZ[(automaton.Dfa[(C, C)], String, U32, B)]()
    for (s <- strs) {
      val nfa = nfaElement(s, z"0")
      val dfa = minimize(nfaToDfa(nfa))
      val litName = s"'${s.value}'"
      dfaInfos = dfaInfos :+ (dfa, litName, nameMap.get(litName).get, F)
    }

    // Build DFA infos for single-character literals
    val chars = gn.collectChars
    for (ch <- chars) {
      val nfa = nfaElement(ch, z"0")
      val dfa = minimize(nfaToDfa(nfa))
      val litName = s"'${conversions.String.fromCis(ISZ(ch.value))}'"
      dfaInfos = dfaInfos :+ (dfa, litName, nameMap.get(litName).get, F)
    }

    // Build DFA infos for lexer rules
    for (r <- lexRules) {
      val dfa = buildDfa(r)
      dfaInfos = dfaInfos :+ (dfa, r.name, nameMap.get(r.name).get, r.isHidden)
    }

    val eofTypeOpt: Option[U32] = nameMap.get("EOF")
    val lexerDfas = LexerDfas.fromDfas(dfaInfos = dfaInfos, eofTypeOpt = eofTypeOpt)

    val maxChunkSize: Z = 60000

    // Serialize NGrammar
    val compact = ng.toCompact
    val grammarInit: ST = {
      if (compact.size <= maxChunkSize) {
        st"""NGrammar.fromCompact("$compact")"""
      } else {
        val compactOps = ops.StringOps(compact)
        var chunkVals = ISZ[ST]()
        var chunkRefs = ISZ[ST]()
        var ci: Z = 0
        var idx: Z = 0
        while (ci < compact.size) {
          val end: Z = if (ci + maxChunkSize > compact.size) compact.size else ci + maxChunkSize
          chunkVals = chunkVals :+ st"""val s$idx = "${compactOps.substring(ci, end)}""""
          chunkRefs = chunkRefs :+ st"$$s$idx"
          ci = end
          idx = idx + 1
        }
        st"""{
            |  ${(chunkVals, "\n")}
            |  NGrammar.fromCompact(st"${(chunkRefs, "")}".render)
            |}"""
      }
    }

    // Serialize LexerDfas
    val lexerCompact = lexerDfas.toCompact
    val lexerInit: ST = {
      if (lexerCompact.size <= maxChunkSize) {
        st"""LexerDfas.fromCompact("$lexerCompact")"""
      } else {
        val compactOps = ops.StringOps(lexerCompact)
        var chunkVals = ISZ[ST]()
        var chunkRefs = ISZ[ST]()
        var ci: Z = 0
        var idx: Z = 0
        while (ci < lexerCompact.size) {
          val end: Z = if (ci + maxChunkSize > lexerCompact.size) lexerCompact.size else ci + maxChunkSize
          chunkVals = chunkVals :+ st"""val s$idx = "${compactOps.substring(ci, end)}""""
          chunkRefs = chunkRefs :+ st"$$s$idx"
          ci = end
          idx = idx + 1
        }
        st"""{
            |  ${(chunkVals, "\n")}
            |  LexerDfas.fromCompact(st"${(chunkRefs, "")}".render)
            |}"""
      }
    }

    return Some(
      st"""// #Sireum
          |$licenseOpt
          |$fileInfo
          |$packageOpt
          |
          |import org.sireum._
          |import org.sireum.parser._
          |
          |object ${name}Parser {
          |  val g: NGrammar = $grammarInit
          |  val lexerDfas: LexerDfas = $lexerInit
          |
          |  def parseRule(uriOpt: Option[String], content: String, ruleName: String, reporter: message.Reporter): Option[ParseTree] = {
          |    val cis = conversions.String.toCis(content)
          |    val docInfo = message.DocInfo.createFromCis(uriOpt, cis)
          |    val chars = Indexable.IszDocInfo[C](cis, docInfo)
          |    val (errorIndex, tokens) = lexerDfas.tokens(chars, T)
          |    if (errorIndex >= 0) {
          |      reporter.error(chars.posOpt(errorIndex, 1), "${name}Parser", st"Unrecognized character '$${ops.COps(cis(errorIndex)).escapeString}'".render)
          |      return None()
          |    }
          |    return g.parse(ruleName, Indexable.fromIsz(tokens), reporter)
          |  }
          |
          |  def parse(uriOpt: Option[String], content: String, reporter: message.Reporter): Option[ParseTree] = {
          |    return parseRule(uriOpt, content, "${ops.StringOps(gn.rules(0).name).escapeST}", reporter)
          |  }
          |}"""
    )
  }
}
