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
import org.sireum.automaton._
import org.sireum.parser.{GrammarAst => AST}
import dk.brics.automaton.{Automaton, State}
import org.sireum.message.Reporter

object DfaBuilder {

  def computeParserDfas(ast: AST.Grammar): HashSMap[String, (Dfa[(C, C)], ISZ[AST.Element])] = {
    var r = HashSMap.empty[String, (Dfa[(C, C)], ISZ[AST.Element])]

    def constructDfa(ast: AST.Rule): (Dfa[(C, C)], ISZ[AST.Element]) = {
      var atoms = ISZ[AST.Element]()

      def constructElement(e: AST.Element): Automaton = {
        e match {
          case _: AST.Element.Char =>
          case _: AST.Element.Str =>
          case _: AST.Element.Ref =>
          case e: AST.Element.Opt => return constructElement(e.element).optional
          case e: AST.Element.Star => return constructElement(e.element).repeat
          case e: AST.Element.Plus => return constructElement(e.element).repeat(1)
          case e: AST.Element.Block => return constructAlts(e.alts)
          case _: AST.Element.Dot => halt(s"Infeasible: $e")
          case e: AST.Element.Range => halt(s"Infeasible: $e")
          case e: AST.Element.Neg => halt(s"Infeasible: $e")
        }
        val index = atoms.size.toInt
        atoms = atoms :+ e
        return Automaton.makeChar(index)
      }

      def constructAlt(alt: AST.Alt): Automaton = {
        val es: ISZ[Automaton] = for (e <- alt.elements) yield constructElement(e)
        return ops.ISZOps(ops.ISZOps(es).drop(1)).foldLeft((a1: Automaton, a2: Automaton) => a1.concatenate(a2), es(0))
      }

      def constructAlts(alts: ISZ[AST.Alt]): Automaton = {
        val as: ISZ[Automaton] = for (alt <- alts) yield constructAlt(alt)
        return ops.ISZOps(ops.ISZOps(as).drop(1)).foldLeft((a1: Automaton, a2: Automaton) => a1.union(a2), as(0))
      }

      val a = constructAlts(ast.alts)
      a.minimize()

      var g = Graph.empty[Z, (C, C)]
      var accepts = HashSSet.empty[Z]
      val statePool = scala.collection.mutable.HashMap.empty[State, Z]

      def numOf(state: State): Z = {
        statePool.get(state) match {
          case scala.Some(n) => return n
          case _ =>
            val r: Z = statePool.size
            statePool(state) = r
            if (state.isAccept) {
              accepts = accepts + r
            }
            return r
        }
      }

      def rec(state: State): Unit = {
        val from = numOf(state)
        if (g.nodes.contains(from)) {
          return
        }
        g = g * from
        import scala.jdk.CollectionConverters._
        for (t <- state.getSortedTransitions(false).asScala) {
          rec(t.getDest)
          val min = C(t.getMin)
          val max = C(t.getMax)
          val to = numOf(t.getDest)
          g = g * to
          g = g +@ (from ~> to, (min, max))
        }
      }

      val init = a.getInitialState
      rec(init)
      return (Dfa(statePool(init), accepts, g), atoms)
    }

    for (rule <- ast.rules if !rule.isLexer) {
      r = r + rule.name ~> constructDfa(rule)
    }

    return r
  }

  def computeLexerDfas(ast: AST.Grammar, reporter: Reporter): HashSMap[String, Dfa[(C, C)]] = {
    var fragmentMap = Map.empty[String, AST.Rule]
    for (rule <- ast.rules if rule.isFragment) {
      fragmentMap = fragmentMap + rule.name ~> rule
    }

    var r = HashSMap.empty[String, Dfa[(C, C)]]

    def constructDfa(ast: AST.Rule): Dfa[(C, C)] = {
      def constructElement(e: AST.Element): Automaton = {
        e match {
          case _: AST.Element.Dot => return Automaton.makeAnyChar
          case e: AST.Element.Char => return Automaton.makeChar(e.value.value)
          case e: AST.Element.Range => return Automaton.makeCharRange(e.from.value.value, e.to.value.value)
          case e: AST.Element.Str =>
            val a = Automaton.makeString(e.value.value)
            a.expandSingleton()
            return a
          case e: AST.Element.Opt => return constructElement(e.element).optional
          case e: AST.Element.Star => return constructElement(e.element).repeat
          case e: AST.Element.Plus => return constructElement(e.element).repeat(1)
          case e: AST.Element.Block => return constructAlts(e.alts)
          case e: AST.Element.Neg => return constructElement(e.element).complement.intersection(Automaton.makeAnyChar)
          case e: AST.Element.Ref =>
            fragmentMap.get(e.name) match {
              case Some(rule) => return constructAlts(rule.alts)
              case _ =>
                reporter.error(e.posOpt, ParserGenerator.kind, s"Could not find lexical rule: ${e.name}")
                return Automaton.makeEmpty
            }
        }
      }

      def constructAlt(alt: AST.Alt): Automaton = {
        val es: ISZ[Automaton] = for (e <- alt.elements) yield constructElement(e)
        return ops.ISZOps(ops.ISZOps(es).drop(1)).foldLeft((a1: Automaton, a2: Automaton) => a1.concatenate(a2), es(0))
      }

      def constructAlts(alts: ISZ[AST.Alt]): Automaton = {
        val as: ISZ[Automaton] = for (alt <- alts) yield constructAlt(alt)
        return ops.ISZOps(ops.ISZOps(as).drop(1)).foldLeft((a1: Automaton, a2: Automaton) => a1.union(a2), as(0))
      }

      val a = constructAlts(ast.alts)
      a.minimize()
      var g = Graph.empty[Z, (C, C)]
      var accepts = HashSSet.empty[Z]
      val statePool = scala.collection.mutable.HashMap.empty[State, Z]

      def numOf(state: State): Z = {
        statePool.get(state) match {
          case scala.Some(n) => return n
          case _ =>
            val r: Z = statePool.size
            statePool(state) = r
            if (state.isAccept) {
              accepts = accepts + r
            }
            return r
        }
      }

      def rec(state: State): Unit = {
        val from = numOf(state)
        if (g.nodes.contains(from)) {
          return
        }
        g = g * from
        import scala.jdk.CollectionConverters._
        for (t <- state.getSortedTransitions(false).asScala) {
          rec(t.getDest)
          val min = C(t.getMin)
          val max = C(t.getMax)
          val to = numOf(t.getDest)
          g = g * to
          g = g +@ (from ~> to, (min, max))
        }
      }

      val init = a.getInitialState
      rec(init)
      return Dfa(statePool(init), accepts, g)
    }

    for (rule <- ast.rules if rule.isLexer && !rule.isFragment) {
      r = r + rule.name ~> constructDfa(rule)
    }

    return r
  }
}
