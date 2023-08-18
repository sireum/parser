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
import org.sireum.parser.{GrammarAst => AST}

object LookAhead {

  val kind: String = "LookAhead"

  @datatype class Case(val value: ISZ[Case.Value])

  object Case {

    @datatype trait Value

    object Value {
      @datatype class Str(val value: String) extends Value
      @datatype class Terminal(val name: String) extends Value
    }

  }

  @datatype class Trie(val accept: B, val value: Case.Value, val subs: HashSMap[Case.Value, Trie]) {
    def update(i: Z, values: ISZ[Case.Value]): Trie = {
      val thisL = this
      if (i >= values.size) {
        return thisL(accept = T)
      } else {
        val value = values(i)
        val trie: Trie = subs.get(value) match {
          case Some(t) => t
          case _ => Trie(F, value, HashSMap.empty)
        }
        return thisL(subs = thisL.subs + value ~> trie.update(i + 1, values))
      }
    }
  }

  def compute(k: Z,
              nameRuleMap: HashMap[String, AST.Rule],
              parserDfaMap: HashSMap[String, (Dfa, ISZ[AST.Element])],
              reporter: message.Reporter): HashMap[String, Trie] = {
    return LookAhead(k, nameRuleMap, parserDfaMap).compute(reporter)
  }
}

@record class LookAhead(val k: Z,
                        val nameRuleMap: HashMap[String, AST.Rule],
                        val parserDfaMap: HashSMap[String, (Dfa, ISZ[AST.Element])]) {

  var cache: HashMap[(String, Z), ISZ[LookAhead.Case]] = HashMap.empty

  def computeRule(seen: HashSet[(String, Z)], size: Z, name: String, dfa: Dfa, atoms: ISZ[AST.Element], reporter: message.Reporter): ISZ[LookAhead.Case] = {

    val key = (name, size)
    cache.get(key) match {
      case Some(r) => return r
      case _ =>
    }

    var r = HashSSet.empty[LookAhead.Case]

    def rec(state: Z, acc: ISZ[LookAhead.Case.Value]): Unit = {
      if (dfa.accepting.contains(state)) {
        r = r + LookAhead.Case(acc)
      }
      if (size + acc.size == k) {
        r = r + LookAhead.Case(acc)
        return
      }
      if (size + acc.size > k) {
        r = r + LookAhead.Case(for (i <- 0 until k - size) yield acc(i))
        return
      }
      val edges = dfa.g.outgoing(state)
      if (edges.isEmpty) {
        r = r + LookAhead.Case(acc)
      } else {
        for (ed <- edges if !Dfa.isReject(ed)) {
          val edge = ed.asInstanceOf[Graph.Edge.Data[Z, (C, C)]]
          val lo = conversions.U32.toZ(conversions.C.toU32(edge.data._1))
          val hi = conversions.U32.toZ(conversions.C.toU32(edge.data._2))
          for (i <- lo to hi) {
            atoms(i) match {
              case e: AST.Element.Char => rec(edge.dest, acc :+ LookAhead.Case.Value.Str(conversions.String.fromCis(ISZ(e.value))))
              case e: AST.Element.Str => rec(edge.dest, acc :+ LookAhead.Case.Value.Str(e.value))
              case e: AST.Element.Ref =>
                if (e.isTerminal) {
                  rec(edge.dest, acc :+ LookAhead.Case.Value.Terminal(e.name))
                } else {
                  val (refDfa, refAtom) = parserDfaMap.get(e.name).get
                  if (seen.contains((e.name, size + acc.size))) {
                    reporter.error(None(), "LookAhead", s"A left-recusive rule $name is detected")
                  } else {
                    val cases = computeRule(seen + ((e.name, size + acc.size)), size + acc.size, e.name, refDfa, refAtom, reporter)
                    for (cas <- cases) {
                      rec(edge.dest, acc ++ cas.value)
                    }
                    if (refDfa.accepting.contains(refDfa.initial)) {
                      rec(edge.dest, acc)
                    }
                  }
                }
              case e => halt(s"Infeasible: $e")
            }
          }
        }
      }
    }

    rec(dfa.initial, ISZ())

    val res = r.elements
    cache = cache + key ~> res
    return res
  }

  def compute(reporter: message.Reporter): HashMap[String, LookAhead.Trie] = {
    var caseMap = HashMap.empty[String, ISZ[LookAhead.Case]]
    for (p <- parserDfaMap.entries) {
      val cases = computeRule(HashSet.empty, 0, p._1, p._2._1, p._2._2, reporter)
      caseMap = caseMap + p._1 ~> cases
    }
    var r = HashMap.empty[String, LookAhead.Trie]
    for (p <- caseMap.entries) {
      var trie = LookAhead.Trie(F, LookAhead.Case.Value.Str(p._1), HashSMap.empty)
      for (cas <- p._2) {
        trie = trie.update(0, cas.value)
      }
      r = r + p._1 ~> trie
    }
    return r
  }
}
