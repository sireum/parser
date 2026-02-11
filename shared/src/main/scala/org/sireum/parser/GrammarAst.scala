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
import org.sireum.message.Position

object GrammarAst {

  @datatype class Grammar(val id: String,
                          val options: ISZ[(String, String)],
                          val pheaderOpt: Option[String],
                          val lheaderOpt: Option[String],
                          val rules: ISZ[Rule]) {

    @memoize def k: Z = {
      for (p <- options) {
        val (k, v) = p
        if (k == "k") {
          Z(v) match {
            case Some(n) => return n
            case _ =>
          }
        }
      }
      halt("Could not find option for k look ahead")
    }

    /** Computes an LL(k) parsing table.
      *
      * @param k the lookahead depth
      * @return a map from parser rule name to its decision table, where each
      *         decision table maps a k-length terminal lookahead sequence to
      *         the applicable alternative indices (0-based into the rule's alts).
      *         A cell with more than one index indicates an LL(k) conflict.
      */
    @memoize def computeParsingTable: HashSMap[String, HashSMap[ISZ[String], ISZ[Z]]] = {
      val firstKMap = computeFirstKSets(k)
      val followKMap = computeFollowKSets(k, firstKMap)
      var table = HashSMap.empty[String, HashSMap[ISZ[String], ISZ[Z]]]
      for (r <- rules if !r.isLexer) {
        var ruleTable = HashSMap.empty[ISZ[String], ISZ[Z]]
        for (i <- 0 until r.alts.size) {
          val altFirst = firstKOfAlt(r.alts(i), k, firstKMap)
          val ruleFollow: HashSSet[ISZ[String]] = followKMap.get(r.name) match {
            case Some(f) => f
            case _ => HashSSet.empty
          }
          val lookahead = truncConcatK(k, altFirst, ruleFollow)
          for (la <- lookahead.elements) {
            val existing: ISZ[Z] = ruleTable.get(la) match {
              case Some(v) => v
              case _ => ISZ()
            }
            ruleTable = ruleTable + la ~> (existing :+ i)
          }
        }
        table = table + r.name ~> ruleTable
      }
      return table
    }

    /** Checks whether the grammar is LL(k) compliant.
      *
      * @param k the lookahead depth
      * @return an empty map if the grammar is LL(k), otherwise a map from
      *         each conflicting rule name to its ambiguous lookahead sequences
      *         and their competing alternative indices.
      */
    @memoize def checkLLk: HashSMap[String, HashSMap[ISZ[String], ISZ[Z]]] = {
      val table = computeParsingTable
      var conflicts = HashSMap.empty[String, HashSMap[ISZ[String], ISZ[Z]]]
      for (entry <- table.entries) {
        var ruleConflicts = HashSMap.empty[ISZ[String], ISZ[Z]]
        for (cell <- entry._2.entries) {
          if (cell._2.size > 1) {
            ruleConflicts = ruleConflicts + cell._1 ~> cell._2
          }
        }
        if (ruleConflicts.nonEmpty) {
          conflicts = conflicts + entry._1 ~> ruleConflicts
        }
      }
      return conflicts
    }

    /** Computes a conflict-free LL(k) parsing table.
      *
      * @param k the lookahead depth
      * @return `Some(table)` where each cell maps to a single alternative index
      *         if the grammar is LL(k), or `None` if there are conflicts.
      */
    @memoize def computeParsingTableOpt: Option[HashSMap[String, HashSMap[ISZ[String], Z]]] = {
      if (checkLLk.nonEmpty) {
        return None()
      }
      val table = computeParsingTable
      var result = HashSMap.empty[String, HashSMap[ISZ[String], Z]]
      for (entry <- table.entries) {
        var ruleTable = HashSMap.empty[ISZ[String], Z]
        for (cell <- entry._2.entries) {
          ruleTable = ruleTable + cell._1 ~> cell._2(0)
        }
        result = result + entry._1 ~> ruleTable
      }
      return Some(result)
    }

    /** Computes a compact predictive parsing table as a trie with default-based compression.
      *
      * @param k the lookahead depth
      * @return `Some(table)` with O(k) lookup per prediction if the grammar is LL(k),
      *         or `None` if there are conflicts.
      */
    @memoize def computePredictiveTableOpt: Option[PredictiveTable] = {
      computeParsingTableOpt match {
        case Some(table) => return Some(PredictiveTable.build(k, table))
        case _ => return None()
      }
    }

    @memoize def computeFirstSets: HashSMap[String, HashSSet[String]] = {
      return computeFirstSetsEpsilon._1
    }

    @memoize def computeFollowSets: HashSMap[String, HashSSet[String]] = {
      val fe = computeFirstSetsEpsilon
      val firstMap = fe._1
      val epsilonSet = fe._2
      var followMap = HashSMap.empty[String, HashSSet[String]]
      for (r <- rules if !r.isLexer) {
        followMap = followMap + r.name ~> HashSSet.empty
      }
      var changed: B = T
      while (changed) {
        var totalBefore: Z = 0
        for (entry <- followMap.entries) {
          totalBefore = totalBefore + entry._2.size
        }
        for (r <- rules if !r.isLexer) {
          for (alt <- r.alts) {
            val rFollow = followMap.get(r.name).get
            followMap = followOfElements(alt.elements, rFollow, firstMap, epsilonSet, followMap)
          }
        }
        var totalAfter: Z = 0
        for (entry <- followMap.entries) {
          totalAfter = totalAfter + entry._2.size
        }
        changed = totalAfter != totalBefore
      }
      return followMap
    }

    @memoize def computeFirstSetsEpsilon: (HashSMap[String, HashSSet[String]], HashSSet[String]) = {
      var firstMap = HashSMap.empty[String, HashSSet[String]]
      var epsilonSet = HashSSet.empty[String]
      for (r <- rules) {
        if (r.isLexer) {
          firstMap = firstMap + r.name ~> (HashSSet.empty[String] + r.name)
        } else {
          firstMap = firstMap + r.name ~> HashSSet.empty
        }
      }
      var changed: B = T
      while (changed) {
        changed = F
        for (r <- rules if !r.isLexer) {
          val p = firstOfAlts(r.alts, firstMap, epsilonSet)
          val oldFirsts = firstMap.get(r.name).get
          var newFirsts = oldFirsts
          for (e <- p._2.elements) {
            newFirsts = newFirsts + e
          }
          if (newFirsts.size != oldFirsts.size) {
            firstMap = firstMap + r.name ~> newFirsts
            changed = T
          }
          if (p._1 && !epsilonSet.contains(r.name)) {
            epsilonSet = epsilonSet + r.name
            changed = T
          }
        }
      }
      return (firstMap, epsilonSet)
    }

    def firstOfAlts(alts: ISZ[Alt],
                    firstMap: HashSMap[String, HashSSet[String]],
                    epsilonSet: HashSSet[String]): (B, HashSSet[String]) = {
      var epsilon: B = F
      var firsts = HashSSet.empty[String]
      for (alt <- alts) {
        val p = firstOfAlt(alt, firstMap, epsilonSet)
        if (p._1) {
          epsilon = T
        }
        for (e <- p._2.elements) {
          firsts = firsts + e
        }
      }
      return (epsilon, firsts)
    }

    def firstOfAlt(alt: Alt,
                   firstMap: HashSMap[String, HashSSet[String]],
                   epsilonSet: HashSSet[String]): (B, HashSSet[String]) = {
      var firsts = HashSSet.empty[String]
      for (e <- alt.elements) {
        val p = firstOfElement(e, firstMap, epsilonSet)
        for (f <- p._2.elements) {
          firsts = firsts + f
        }
        if (!p._1) {
          return (F, firsts)
        }
      }
      return (T, firsts)
    }

    def firstOfElement(e: Element,
                       firstMap: HashSMap[String, HashSSet[String]],
                       epsilonSet: HashSSet[String]): (B, HashSSet[String]) = {
      val empty = HashSSet.empty[String]
      e match {
        case _: Element.Dot => return (F, empty)
        case c: Element.Char => return (F, empty + c.value.string)
        case s: Element.Str => return (F, empty + s.value)
        case _: Element.Range => return (F, empty)
        case _: Element.Neg => return (F, empty)
        case ref: Element.Ref =>
          firstMap.get(ref.name) match {
            case Some(firsts) => return (epsilonSet.contains(ref.name), firsts)
            case _ => return (F, empty)
          }
        case block: Element.Block =>
          return firstOfAlts(block.alts, firstMap, epsilonSet)
        case opt: Element.Opt =>
          val p = firstOfElement(opt.element, firstMap, epsilonSet)
          return (T, p._2)
        case star: Element.Star =>
          val p = firstOfElement(star.element, firstMap, epsilonSet)
          return (T, p._2)
        case plus: Element.Plus =>
          return firstOfElement(plus.element, firstMap, epsilonSet)
      }
    }

    def followOfElements(elements: ISZ[Element], tailFollow: HashSSet[String],
                         firstMap: HashSMap[String, HashSSet[String]],
                         epsilonSet: HashSSet[String],
                         followMap: HashSMap[String, HashSSet[String]]): HashSMap[String, HashSSet[String]] = {
      var fm = followMap
      var followCtx = tailFollow
      var i = elements.size - 1
      while (i >= 0) {
        fm = followOfElement(elements(i), followCtx, firstMap, epsilonSet, fm)
        val p = firstOfElement(elements(i), firstMap, epsilonSet)
        if (p._1) {
          for (f <- p._2.elements) {
            followCtx = followCtx + f
          }
        } else {
          followCtx = p._2
        }
        i = i - 1
      }
      return fm
    }

    def followOfElement(e: Element, followCtx: HashSSet[String],
                        firstMap: HashSMap[String, HashSSet[String]],
                        epsilonSet: HashSSet[String],
                        followMap: HashSMap[String, HashSSet[String]]): HashSMap[String, HashSSet[String]] = {
      e match {
        case ref: Element.Ref =>
          if (!ref.isTerminal) {
            followMap.get(ref.name) match {
              case Some(existing) =>
                var updated = existing
                for (f <- followCtx.elements) {
                  updated = updated + f
                }
                return followMap + ref.name ~> updated
              case _ => return followMap
            }
          } else {
            return followMap
          }
        case block: Element.Block =>
          var fm = followMap
          for (alt <- block.alts) {
            fm = followOfElements(alt.elements, followCtx, firstMap, epsilonSet, fm)
          }
          return fm
        case opt: Element.Opt =>
          return followOfElement(opt.element, followCtx, firstMap, epsilonSet, followMap)
        case star: Element.Star =>
          val p = firstOfElement(star.element, firstMap, epsilonSet)
          var innerFollow = followCtx
          for (f <- p._2.elements) {
            innerFollow = innerFollow + f
          }
          return followOfElement(star.element, innerFollow, firstMap, epsilonSet, followMap)
        case plus: Element.Plus =>
          val p = firstOfElement(plus.element, firstMap, epsilonSet)
          var innerFollow = followCtx
          for (f <- p._2.elements) {
            innerFollow = innerFollow + f
          }
          return followOfElement(plus.element, innerFollow, firstMap, epsilonSet, followMap)
        case _ => return followMap
      }
    }

    def truncConcatK(k: Z, s1: HashSSet[ISZ[String]], s2: HashSSet[ISZ[String]]): HashSSet[ISZ[String]] = {
      var result = HashSSet.empty[ISZ[String]]
      for (a <- s1.elements) {
        if (a.size >= k) {
          result = result + (for (j <- 0 until k) yield a(j))
        } else {
          for (b <- s2.elements) {
            val combined = a ++ b
            if (combined.size >= k) {
              result = result + (for (j <- 0 until k) yield combined(j))
            } else {
              result = result + combined
            }
          }
        }
      }
      return result
    }

    def firstKOfStar(inner: HashSSet[ISZ[String]], k: Z): HashSSet[ISZ[String]] = {
      var result = HashSSet.empty[ISZ[String]] + ISZ[String]()
      var done: B = F
      while (!done) {
        val oldSize = result.size
        for (e <- truncConcatK(k, result, inner).elements) {
          result = result + e
        }
        done = result.size == oldSize
      }
      return result
    }

    def firstKOfElement(e: Element, k: Z,
                        firstKMap: HashSMap[String, HashSSet[ISZ[String]]]): HashSSet[ISZ[String]] = {
      val empty = HashSSet.empty[ISZ[String]]
      e match {
        case _: Element.Dot => return empty
        case c: Element.Char => return empty + ISZ(c.value.string)
        case s: Element.Str => return empty + ISZ(s.value)
        case _: Element.Range => return empty
        case _: Element.Neg => return empty
        case ref: Element.Ref =>
          firstKMap.get(ref.name) match {
            case Some(firsts) => return firsts
            case _ => return empty
          }
        case block: Element.Block =>
          return firstKOfAlts(block.alts, k, firstKMap)
        case opt: Element.Opt =>
          return firstKOfElement(opt.element, k, firstKMap) + ISZ[String]()
        case star: Element.Star =>
          return firstKOfStar(firstKOfElement(star.element, k, firstKMap), k)
        case plus: Element.Plus =>
          val inner = firstKOfElement(plus.element, k, firstKMap)
          return truncConcatK(k, inner, firstKOfStar(inner, k))
      }
    }

    def firstKOfAlt(alt: Alt, k: Z,
                    firstKMap: HashSMap[String, HashSSet[ISZ[String]]]): HashSSet[ISZ[String]] = {
      var acc = HashSSet.empty[ISZ[String]] + ISZ[String]()
      for (e <- alt.elements) {
        acc = truncConcatK(k, acc, firstKOfElement(e, k, firstKMap))
      }
      return acc
    }

    def firstKOfAlts(alts: ISZ[Alt], k: Z,
                     firstKMap: HashSMap[String, HashSSet[ISZ[String]]]): HashSSet[ISZ[String]] = {
      var result = HashSSet.empty[ISZ[String]]
      for (alt <- alts) {
        for (e <- firstKOfAlt(alt, k, firstKMap).elements) {
          result = result + e
        }
      }
      return result
    }

    @memoize def computeFirstKSets(k: Z): HashSMap[String, HashSSet[ISZ[String]]] = {
      var firstKMap = HashSMap.empty[String, HashSSet[ISZ[String]]]
      for (r <- rules) {
        if (r.isLexer) {
          firstKMap = firstKMap + r.name ~> (HashSSet.empty[ISZ[String]] + ISZ(r.name))
        } else {
          firstKMap = firstKMap + r.name ~> HashSSet.empty
        }
      }
      var changed: B = T
      while (changed) {
        changed = F
        for (r <- rules if !r.isLexer) {
          val computed = firstKOfAlts(r.alts, k, firstKMap)
          val old = firstKMap.get(r.name).get
          var updated = old
          for (e <- computed.elements) {
            updated = updated + e
          }
          if (updated.size != old.size) {
            firstKMap = firstKMap + r.name ~> updated
            changed = T
          }
        }
      }
      return firstKMap
    }

    def followKOfElement(e: Element, followCtx: HashSSet[ISZ[String]], k: Z,
                         firstKMap: HashSMap[String, HashSSet[ISZ[String]]],
                         followKMap: HashSMap[String, HashSSet[ISZ[String]]]): HashSMap[String, HashSSet[ISZ[String]]] = {
      e match {
        case ref: Element.Ref =>
          if (!ref.isTerminal) {
            followKMap.get(ref.name) match {
              case Some(existing) =>
                var updated = existing
                for (f <- followCtx.elements) {
                  updated = updated + f
                }
                return followKMap + ref.name ~> updated
              case _ => return followKMap
            }
          } else {
            return followKMap
          }
        case block: Element.Block =>
          var fm = followKMap
          for (alt <- block.alts) {
            fm = followKOfElements(alt.elements, followCtx, k, firstKMap, fm)
          }
          return fm
        case opt: Element.Opt =>
          return followKOfElement(opt.element, followCtx, k, firstKMap, followKMap)
        case star: Element.Star =>
          val inner = firstKOfElement(star.element, k, firstKMap)
          val innerFollow = truncConcatK(k, firstKOfStar(inner, k), followCtx)
          return followKOfElement(star.element, innerFollow, k, firstKMap, followKMap)
        case plus: Element.Plus =>
          val inner = firstKOfElement(plus.element, k, firstKMap)
          val innerFollow = truncConcatK(k, firstKOfStar(inner, k), followCtx)
          return followKOfElement(plus.element, innerFollow, k, firstKMap, followKMap)
        case _ => return followKMap
      }
    }

    def followKOfElements(elements: ISZ[Element], tailFollow: HashSSet[ISZ[String]], k: Z,
                          firstKMap: HashSMap[String, HashSSet[ISZ[String]]],
                          followKMap: HashSMap[String, HashSSet[ISZ[String]]]): HashSMap[String, HashSSet[ISZ[String]]] = {
      var fm = followKMap
      var followCtx = tailFollow
      var i = elements.size - 1
      while (i >= 0) {
        fm = followKOfElement(elements(i), followCtx, k, firstKMap, fm)
        followCtx = truncConcatK(k, firstKOfElement(elements(i), k, firstKMap), followCtx)
        i = i - 1
      }
      return fm
    }

    def computeFollowKSets(k: Z,
                           firstKMap: HashSMap[String, HashSSet[ISZ[String]]]): HashSMap[String, HashSSet[ISZ[String]]] = {
      var followKMap = HashSMap.empty[String, HashSSet[ISZ[String]]]
      for (r <- rules if !r.isLexer) {
        followKMap = followKMap + r.name ~> HashSSet.empty
      }
      var changed: B = T
      while (changed) {
        var totalBefore: Z = 0
        for (entry <- followKMap.entries) {
          totalBefore = totalBefore + entry._2.size
        }
        for (r <- rules if !r.isLexer) {
          for (alt <- r.alts) {
            val rFollow = followKMap.get(r.name).get
            followKMap = followKOfElements(alt.elements, rFollow, k, firstKMap, followKMap)
          }
        }
        var totalAfter: Z = 0
        for (entry <- followKMap.entries) {
          totalAfter = totalAfter + entry._2.size
        }
        changed = totalAfter != totalBefore
      }
      return followKMap
    }

  }

  @datatype class Rule(val name: String,
                       val isLexer: B,
                       val isFragment: B,
                       val isHidden: B,
                       val posOpt: Option[Position],
                       val alts: ISZ[Alt])

  @datatype class Alt(val elements: ISZ[Element])

  @datatype trait Element {
    @pure def posOpt: Option[Position]

    @pure def isSimple: B
  }

  object Element {

    @datatype class Dot(@hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = T
    }

    @datatype class Char(val value: C,
                         @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = T
    }

    @datatype class Str(val value: String,
                        @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = T
    }

    @datatype class Range(val from: Char, val to: Char,
                          @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = T
    }

    @datatype class Ref(val isTerminal: B,
                        val name: String,
                        @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = F
    }

    @datatype class Block(val alts: ISZ[Alt],
                          @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = F
    }

    @datatype class Opt(val element: Element,
                        @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = F
    }

    @datatype class Star(val element: Element,
                         @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = F
    }

    @datatype class Plus(val element: Element,
                         @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = F
    }

    @datatype class Neg(val element: Element,
                        @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = F
    }
  }
}
