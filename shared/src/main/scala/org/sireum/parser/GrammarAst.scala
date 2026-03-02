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
import org.sireum.S32._
import org.sireum.U32._
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

    /** Collects all [[Element.Str]] appearing in grammatical rules, in order of occurrence.
      *
      * @return the string literal elements found in parser rules
      */
    @memoize def collectStrs: ISZ[Element.Str] = {
      var r = ISZ[Element.Str]()
      var seen = HashSet.empty[String]
      def collectElement(e: Element): Unit = {
        e match {
          case e: Element.Str =>
            if (!seen.contains(e.value)) {
              seen = seen + e.value
              r = r :+ e
            }
          case e: Element.Opt => collectElement(e.element)
          case e: Element.Star => collectElement(e.element)
          case e: Element.Plus => collectElement(e.element)
          case e: Element.Block =>
            for (alt <- e.alts) {
              for (elem <- alt.elements) {
                collectElement(elem)
              }
            }
          case _: Element.Neg => halt("Infeasible")
          case _: Element.Ref =>
          case _: Element.Char =>
          case _: Element.Dot =>
          case _: Element.Range =>
        }
      }
      for (rule <- rules if !rule.isLexer) {
        for (alt <- rule.alts) {
          for (e <- alt.elements) {
            collectElement(e)
          }
        }
      }
      return r
    }

    @memoize def collectChars: ISZ[Element.Char] = {
      var r = ISZ[Element.Char]()
      var seen = HashSet.empty[C]
      def collectElement(e: Element): Unit = {
        e match {
          case e: Element.Char =>
            if (!seen.contains(e.value)) {
              seen = seen + e.value
              r = r :+ e
            }
          case e: Element.Opt => collectElement(e.element)
          case e: Element.Star => collectElement(e.element)
          case e: Element.Plus => collectElement(e.element)
          case e: Element.Block =>
            for (alt <- e.alts) {
              for (elem <- alt.elements) {
                collectElement(elem)
              }
            }
          case _: Element.Neg => halt("Infeasible")
          case _: Element.Ref =>
          case _: Element.Str =>
          case _: Element.Dot =>
          case _: Element.Range =>
        }
      }
      for (rule <- rules if !rule.isLexer) {
        for (alt <- rule.alts) {
          for (e <- alt.elements) {
            collectElement(e)
          }
        }
      }
      return r
    }

    /** Checks that no lexical rule references a grammatical rule.
      *
      * @return a map from each offending lexer rule name to the set of
      *         grammatical rule names it references. An empty map means
      *         the grammar is well-formed in this regard.
      */
    @memoize def checkLexerRefs: HashSMap[String, ISZ[String]] = {
      var parserRules = HashSSet.empty[String]
      for (r <- rules if !r.isLexer) {
        parserRules = parserRules + r.name
      }

      def checkElement(e: Element): ISZ[String] = {
        e match {
          case ref: Element.Ref =>
            if (parserRules.contains(ref.name)) {
              return ISZ(ref.name)
            }
            return ISZ()
          case block: Element.Block =>
            var result = ISZ[String]()
            for (alt <- block.alts) {
              for (e2 <- alt.elements) {
                result = result ++ checkElement(e2)
              }
            }
            return result
          case opt: Element.Opt => return checkElement(opt.element)
          case star: Element.Star => return checkElement(star.element)
          case plus: Element.Plus => return checkElement(plus.element)
          case neg: Element.Neg => return checkElement(neg.element)
          case _ => return ISZ()
        }
      }

      var result = HashSMap.empty[String, ISZ[String]]
      for (r <- rules if r.isLexer) {
        var bad = ISZ[String]()
        for (alt <- r.alts) {
          for (e <- alt.elements) {
            bad = bad ++ checkElement(e)
          }
        }
        if (bad.nonEmpty) {
          result = result + r.name ~> bad
        }
      }
      return result
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
        case _: Element.Char => return (F, empty)
        case s: Element.Str => return (F, empty + s"'${s.value}'")
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
        case c: Element.Char =>
          val s = conversions.String.fromCis(ISZ(c.value))
          return empty + ISZ(s"'$s'")
        case s: Element.Str => return empty + ISZ(s"'${s.value}'")
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

    /** Transforms the grammar into a normal form where every composite
      * sub-expression is associated with a named rule:
      *
      *  - Fragment lexer rules are inlined into all referencing lexer
      *    rules (transitively) and then removed from the grammar.
      *  - Every inline `Block` (alternative list) is extracted into a
      *    fresh rule and replaced by a `Ref` to that rule.
      *  - `Opt`, `Star`, and `Plus` elements are guaranteed to wrap a
      *    `Ref`; non-`Ref` operands are extracted into fresh rules.
      *  - In a multi-alternative rule each `Alt` contains exactly one
      *    element which is a `Ref`. Only a single-alternative rule may
      *    have an `Alt` with multiple elements.
      *  - Non-fragment lexer rules are left unchanged.
      *
      * Introduced rules are named `parentRule$N` where N is a
      * globally-unique counter, and are appended after the original
      * rules.
      *
      * @return an equivalent grammar in normal form
      */
    def normalize: Grammar = {

      def normalizeRule(r: Rule, counter: Z, introduced: ISZ[Rule]): (Rule, Z, ISZ[Rule]) = {
        if (r.alts.size > 1) {
          var newAlts = ISZ[Alt]()
          var c = counter
          var nr = introduced
          for (alt <- r.alts) {
            var isSingleRef: B = F
            if (alt.elements.size == 1) {
              alt.elements(0) match {
                case _: Element.Ref => isSingleRef = T
                case _ =>
              }
            }
            if (isSingleRef) {
              newAlts = newAlts :+ alt
            } else {
              val name: String = st"${r.name}$$$c".render
              c = c + 1
              var normElements = ISZ[Element]()
              for (e <- alt.elements) {
                val p = normalizeElement(e, name, c, nr)
                normElements = normElements :+ p._1
                c = p._2
                nr = p._3
              }
              nr = nr :+ Rule(name = name, isLexer = F, isFragment = F, isHidden = F, isSynthetic = T, posOpt = r.posOpt, alts = ISZ(Alt(normElements)))
              newAlts = newAlts :+ Alt(ISZ(Element.Ref(F, name, r.posOpt)))
            }
          }
          return (Rule(name = r.name, isLexer = r.isLexer, isFragment = r.isFragment, isHidden = r.isHidden, isSynthetic = r.isSynthetic, posOpt = r.posOpt, alts = newAlts), c, nr)
        } else if (r.alts.size == 1) {
          var normElements = ISZ[Element]()
          var c = counter
          var nr = introduced
          for (e <- r.alts(0).elements) {
            val p = normalizeElement(e, r.name, c, nr)
            normElements = normElements :+ p._1
            c = p._2
            nr = p._3
          }
          return (Rule(name = r.name, isLexer = r.isLexer, isFragment = r.isFragment, isHidden = r.isHidden, isSynthetic = r.isSynthetic, posOpt = r.posOpt, alts = ISZ(Alt(normElements))), c, nr)
        } else {
          return (r, counter, introduced)
        }
      }

      def normalizeElement(e: Element, baseName: String, counter: Z,
                           introduced: ISZ[Rule]): (Element, Z, ISZ[Rule]) = {
        e match {
          case block: Element.Block =>
            val name: String = st"${baseName}$$$counter".render
            val c = counter + 1
            val tempRule = Rule(name = name, isLexer = F, isFragment = F, isHidden = F, isSynthetic = T, posOpt = block.posOpt, alts = block.alts)
            val p = normalizeRule(tempRule, c, introduced)
            return (Element.Ref(F, name, block.posOpt), p._2, p._3 :+ p._1)
          case opt: Element.Opt =>
            val p = ensureRef(opt.element, baseName, counter, introduced)
            val innerRef = p._1
            val c1 = p._2
            val nr1 = p._3
            val optName: String = st"${baseName}$$$c1".render
            val c2 = c1 + 1
            val matchAlt = Alt(ISZ(innerRef))
            val skipAlt = Alt(ISZ())
            val optRule = Rule(name = optName, isLexer = F, isFragment = F, isHidden = F, isSynthetic = T, posOpt = opt.posOpt, alts = ISZ(matchAlt, skipAlt))
            val op = normalizeRule(optRule, c2, nr1)
            return (Element.Ref(F, optName, opt.posOpt), op._2, op._3 :+ op._1)
          case star: Element.Star =>
            val p = ensureRef(star.element, baseName, counter, introduced)
            val innerRef = p._1
            val c1 = p._2
            val nr1 = p._3
            val starName: String = st"${baseName}$$$c1".render
            val c2 = c1 + 1
            val continueAlt = Alt(ISZ(innerRef, Element.Ref(F, starName, star.posOpt)))
            val stopAlt = Alt(ISZ())
            val starRule = Rule(name = starName, isLexer = F, isFragment = F, isHidden = F, isSynthetic = T, posOpt = star.posOpt, alts = ISZ(continueAlt, stopAlt))
            val sp = normalizeRule(starRule, c2, nr1)
            return (Element.Ref(F, starName, star.posOpt), sp._2, sp._3 :+ sp._1)
          case plus: Element.Plus =>
            val p = ensureRef(plus.element, baseName, counter, introduced)
            val innerRef = p._1
            val c1 = p._2
            val nr1 = p._3
            val starName: String = st"${baseName}$$$c1".render
            val c2 = c1 + 1
            val continueAlt = Alt(ISZ(innerRef, Element.Ref(F, starName, plus.posOpt)))
            val stopAlt = Alt(ISZ())
            val starRule = Rule(name = starName, isLexer = F, isFragment = F, isHidden = F, isSynthetic = T, posOpt = plus.posOpt, alts = ISZ(continueAlt, stopAlt))
            val sp = normalizeRule(starRule, c2, nr1)
            val plusName: String = st"${baseName}$$${sp._2}".render
            val c3 = sp._2 + 1
            val plusRule = Rule(name = plusName, isLexer = F, isFragment = F, isHidden = F, isSynthetic = T, posOpt = plus.posOpt, alts = ISZ(Alt(ISZ(innerRef, Element.Ref(F, starName, plus.posOpt)))))
            return (Element.Ref(F, plusName, plus.posOpt), c3, sp._3 :+ sp._1 :+ plusRule)
          case _ =>
            return (e, counter, introduced)
        }
      }

      def ensureRef(e: Element, baseName: String, counter: Z,
                    introduced: ISZ[Rule]): (Element, Z, ISZ[Rule]) = {
        e match {
          case _: Element.Ref =>
            return (e, counter, introduced)
          case block: Element.Block =>
            val name: String = st"${baseName}$$$counter".render
            val c = counter + 1
            val tempRule = Rule(name = name, isLexer = F, isFragment = F, isHidden = F, isSynthetic = T, posOpt = block.posOpt, alts = block.alts)
            val p = normalizeRule(tempRule, c, introduced)
            return (Element.Ref(F, name, block.posOpt), p._2, p._3 :+ p._1)
          case _ =>
            val name: String = st"${baseName}$$$counter".render
            val c = counter + 1
            val p = normalizeElement(e, name, c, introduced)
            val newRule = Rule(name = name, isLexer = F, isFragment = F, isHidden = F, isSynthetic = T, posOpt = e.posOpt, alts = ISZ(Alt(ISZ(p._1))))
            return (Element.Ref(F, name, e.posOpt), p._2, p._3 :+ newRule)
        }
      }

      // Phase 1: inline fragment lexer rules into other lexer rules
      var fragmentMap = HashSMap.empty[String, Rule]
      for (r <- rules if r.isLexer && r.isFragment) {
        fragmentMap = fragmentMap + r.name ~> r
      }

      def inlineFragElement(e: Element): Element = {
        e match {
          case ref: Element.Ref =>
            fragmentMap.get(ref.name) match {
              case Some(frag) =>
                if (frag.alts.size == 1 && frag.alts(0).elements.size == 1) {
                  return inlineFragElement(frag.alts(0).elements(0))
                } else {
                  return Element.Block(inlineFragAlts(frag.alts), ref.posOpt)
                }
              case _ => return e
            }
          case block: Element.Block =>
            return Element.Block(inlineFragAlts(block.alts), block.posOpt)
          case opt: Element.Opt =>
            return Element.Opt(inlineFragElement(opt.element), opt.posOpt)
          case star: Element.Star =>
            return Element.Star(inlineFragElement(star.element), star.posOpt)
          case plus: Element.Plus =>
            return Element.Plus(inlineFragElement(plus.element), plus.posOpt)
          case neg: Element.Neg =>
            return Element.Neg(inlineFragElement(neg.element), neg.posOpt)
          case _ => return e
        }
      }

      def inlineFragAlts(alts: ISZ[Alt]): ISZ[Alt] = {
        return for (alt <- alts) yield Alt(for (e <- alt.elements) yield inlineFragElement(e))
      }

      var inlinedRules = ISZ[Rule]()
      for (r <- rules) {
        if (r.isLexer && r.isFragment) {
          // skip — fragments have been inlined
        } else if (r.isLexer) {
          inlinedRules = inlinedRules :+ Rule(name = r.name, isLexer = r.isLexer, isFragment = r.isFragment, isHidden = r.isHidden, isSynthetic = r.isSynthetic, posOpt = r.posOpt, alts = inlineFragAlts(r.alts))
        } else {
          inlinedRules = inlinedRules :+ r
        }
      }

      // Phase 2: normalize parser rules
      var counter: Z = 0
      var introduced = ISZ[Rule]()
      var resultRules = ISZ[Rule]()
      for (r <- inlinedRules) {
        if (r.isLexer) {
          resultRules = resultRules :+ r
        } else {
          val p = normalizeRule(r, counter, introduced)
          resultRules = resultRules :+ p._1
          counter = p._2
          introduced = p._3
        }
      }
      return Grammar(id, options, pheaderOpt, lheaderOpt, resultRules ++ introduced)
    }

    /** Transforms the grammar into a form optimized for human readability
      * when pretty-printed. The resulting grammar is semantically
      * equivalent but may no longer be LL(k) for a fixed k. Lexer rules
      * are left unchanged.
      *
      * The transformation performs two passes:
      *
      *  1. '''Inlining''' — parser rules that are referenced exactly once
      *     or that have a single alternative with at most two elements are
      *     eliminated by splicing their bodies into every use site. This
      *     collapses helper rules (e.g. those introduced by `normalize`)
      *     so that each named rule captures a self-contained grammatical
      *     concept rather than an implementation artifact.
      *
      *  2. '''Distribution''' — inline `Block` alternatives within an
      *     `Alt` are distributed over the surrounding elements, turning
      *     `a (b | c) d` into `a b d | a c d`. This flattens nested
      *     choices into top-level alternatives, making the full set of
      *     accepted sequences immediately visible.
      *
      * Together, these passes produce a grammar where every rule reads
      * as a flat enumeration of its accepted element sequences, without
      * auxiliary indirection or embedded choice operators.
      *
      * @return an equivalent grammar optimized for readability
      */
    def readable: Grammar = {

      def computeRefCounts(rs: ISZ[Rule]): HashSMap[String, Z] = {

        def countElement(e: Element, counts: HashSMap[String, Z]): HashSMap[String, Z] = {
          var cs = counts
          e match {
            case ref: Element.Ref =>
              cs.get(ref.name) match {
                case Some(n) => cs = cs + ref.name ~> (n + 1)
                case _ =>
              }
            case block: Element.Block =>
              for (alt <- block.alts) {
                for (e2 <- alt.elements) {
                  cs = countElement(e2, cs)
                }
              }
            case opt: Element.Opt => cs = countElement(opt.element, cs)
            case star: Element.Star => cs = countElement(star.element, cs)
            case plus: Element.Plus => cs = countElement(plus.element, cs)
            case _ =>
          }
          return cs
        }

        var counts = HashSMap.empty[String, Z]
        for (r <- rs if !r.isLexer) {
          counts = counts + r.name ~> z"0"
        }
        for (r <- rs) {
          for (alt <- r.alts) {
            for (e <- alt.elements) {
              counts = countElement(e, counts)
            }
          }
        }
        return counts
      }

      def inlineElement(e: Element, ruleMap: HashSMap[String, Rule],
                        inlineable: HashSSet[String]): Element = {
        e match {
          case ref: Element.Ref =>
            if (inlineable.contains(ref.name)) {
              ruleMap.get(ref.name) match {
                case Some(r) =>
                  if (r.alts.size == 1 && r.alts(0).elements.size == 1) {
                    return inlineElement(r.alts(0).elements(0), ruleMap, inlineable)
                  } else {
                    return Element.Block(inlineAlts(r.alts, ruleMap, inlineable), ref.posOpt)
                  }
                case _ =>
              }
            }
            return e
          case block: Element.Block =>
            return Element.Block(inlineAlts(block.alts, ruleMap, inlineable), block.posOpt)
          case opt: Element.Opt =>
            return Element.Opt(inlineElement(opt.element, ruleMap, inlineable), opt.posOpt)
          case star: Element.Star =>
            return Element.Star(inlineElement(star.element, ruleMap, inlineable), star.posOpt)
          case plus: Element.Plus =>
            return Element.Plus(inlineElement(plus.element, ruleMap, inlineable), plus.posOpt)
          case _ =>
            return e
        }
      }

      def inlineAlt(alt: Alt, ruleMap: HashSMap[String, Rule],
                    inlineable: HashSSet[String]): Alt = {
        var newElements = ISZ[Element]()
        for (e <- alt.elements) {
          e match {
            case ref: Element.Ref if inlineable.contains(ref.name) =>
              ruleMap.get(ref.name) match {
                case Some(r) =>
                  if (r.alts.size == 1) {
                    for (e2 <- r.alts(0).elements) {
                      newElements = newElements :+ inlineElement(e2, ruleMap, inlineable)
                    }
                  } else {
                    newElements = newElements :+ Element.Block(
                      inlineAlts(r.alts, ruleMap, inlineable), ref.posOpt)
                  }
                case _ =>
                  newElements = newElements :+ e
              }
            case _ =>
              newElements = newElements :+ inlineElement(e, ruleMap, inlineable)
          }
        }
        return Alt(newElements)
      }

      def inlineAlts(alts: ISZ[Alt], ruleMap: HashSMap[String, Rule],
                     inlineable: HashSSet[String]): ISZ[Alt] = {
        var newAlts = ISZ[Alt]()
        for (alt <- alts) {
          if (alt.elements.size == 1) {
            alt.elements(0) match {
              case ref: Element.Ref if inlineable.contains(ref.name) =>
                ruleMap.get(ref.name) match {
                  case Some(r) =>
                    for (a <- r.alts) {
                      newAlts = newAlts :+ inlineAlt(a, ruleMap, inlineable)
                    }
                  case _ =>
                    newAlts = newAlts :+ inlineAlt(alt, ruleMap, inlineable)
                }
              case _ =>
                newAlts = newAlts :+ inlineAlt(alt, ruleMap, inlineable)
            }
          } else {
            newAlts = newAlts :+ inlineAlt(alt, ruleMap, inlineable)
          }
        }
        return newAlts
      }

      def distributeAlt(alt: Alt): ISZ[Alt] = {
        var partials: ISZ[ISZ[Element]] = ISZ(ISZ())
        for (e <- alt.elements) {
          e match {
            case block: Element.Block =>
              var blockAlts = ISZ[Alt]()
              for (ba <- block.alts) {
                blockAlts = blockAlts ++ distributeAlt(ba)
              }
              if (partials.size * blockAlts.size > z"3") {
                partials = for (partial <- partials) yield partial :+ Element.Block(blockAlts, block.posOpt)
              } else {
                var newPartials = ISZ[ISZ[Element]]()
                for (partial <- partials) {
                  for (ba <- blockAlts) {
                    newPartials = newPartials :+ (partial ++ ba.elements)
                  }
                }
                partials = newPartials
              }
            case _ =>
              partials = for (partial <- partials) yield partial :+ e
          }
        }
        return for (partial <- partials) yield Alt(partial)
      }

      def collectElementRefs(e: Element, refs: HashSSet[String]): HashSSet[String] = {
        var r = refs
        e match {
          case ref: Element.Ref => r = r + ref.name
          case block: Element.Block =>
            for (alt <- block.alts) {
              for (elem <- alt.elements) {
                r = collectElementRefs(elem, r)
              }
            }
          case opt: Element.Opt => r = collectElementRefs(opt.element, r)
          case star: Element.Star => r = collectElementRefs(star.element, r)
          case plus: Element.Plus => r = collectElementRefs(plus.element, r)
          case _ =>
        }
        return r
      }

      def canReachSelf(start: String, current: String, ruleMap: HashSMap[String, Rule],
                       candidates: HashSSet[String], visited: HashSSet[String]): B = {
        if (visited.contains(current)) {
          return F
        }
        ruleMap.get(current) match {
          case Some(r) =>
            var refs = HashSSet.empty[String]
            for (alt <- r.alts) {
              for (e <- alt.elements) {
                refs = collectElementRefs(e, refs)
              }
            }
            for (ref <- refs.elements) {
              if (candidates.contains(ref)) {
                if (ref == start) {
                  return T
                }
                if (canReachSelf(start, ref, ruleMap, candidates, visited + current)) {
                  return T
                }
              }
            }
          case _ =>
        }
        return F
      }

      var currentRules = rules
      var changed: B = T
      while (changed) {
        changed = F
        var ruleMap = HashSMap.empty[String, Rule]
        for (r <- currentRules) {
          ruleMap = ruleMap + r.name ~> r
        }
        val refCounts = computeRefCounts(currentRules)
        var inlineable = HashSSet.empty[String]
        for (r <- currentRules if !r.isLexer) {
          val isSmall: B = r.alts.size == 1 && r.alts(0).elements.size <= 2
          refCounts.get(r.name) match {
            case Some(n) if n == 1 || isSmall => inlineable = inlineable + r.name
            case _ =>
          }
        }
        // Exclude inlineable rules that form cycles to prevent stack overflow during inlining
        var safeInlineable = HashSSet.empty[String]
        for (name <- inlineable.elements) {
          if (!canReachSelf(name, name, ruleMap, inlineable, HashSSet.empty[String])) {
            safeInlineable = safeInlineable + name
          }
        }
        inlineable = safeInlineable
        if (inlineable.nonEmpty) {
          changed = T
          var newRules = ISZ[Rule]()
          for (r <- currentRules) {
            if (!r.isLexer && inlineable.contains(r.name)) {
              // skip — will be inlined at use site
            } else if (r.isLexer) {
              newRules = newRules :+ r
            } else {
              val newAlts = inlineAlts(r.alts, ruleMap, inlineable)
              newRules = newRules :+ Rule(name = r.name, isLexer = r.isLexer, isFragment = r.isFragment, isHidden = r.isHidden, isSynthetic = r.isSynthetic, posOpt = r.posOpt, alts = newAlts)
            }
          }
          currentRules = newRules
        }
      }
      var finalRules = ISZ[Rule]()
      for (r <- currentRules) {
        var newAlts = ISZ[Alt]()
        for (alt <- r.alts) {
          newAlts = newAlts ++ distributeAlt(alt)
        }
        finalRules = finalRules :+ Rule(name = r.name, isLexer = r.isLexer, isFragment = r.isFragment, isHidden = r.isHidden, isSynthetic = r.isSynthetic, posOpt = r.posOpt, alts = newAlts)
      }
      // Inline simple lexer rules (all alts are single string/char literals, non-fragment,
      // non-hidden) into parser rules, replacing Ref with the literal element or Block
      var simpleLexerLiterals = HashSMap.empty[String, Element]
      for (r <- finalRules) {
        if (r.isLexer && !r.isFragment && !r.isHidden && r.alts.nonEmpty) {
          var allSimple: B = T
          for (alt <- r.alts if allSimple) {
            if (alt.elements.size == 1) {
              alt.elements(0) match {
                case _: Element.Str =>
                case _: Element.Char =>
                case _ => allSimple = F
              }
            } else {
              allSimple = F
            }
          }
          if (allSimple) {
            if (r.alts.size == 1) {
              simpleLexerLiterals = simpleLexerLiterals + r.name ~> r.alts(0).elements(0)
            } else {
              simpleLexerLiterals = simpleLexerLiterals + r.name ~> Element.Block(r.alts, r.posOpt)
            }
          }
        }
      }

      def inlineLexerElement(e: Element): Element = {
        e match {
          case ref: Element.Ref =>
            simpleLexerLiterals.get(ref.name) match {
              case Some(lit) => return lit
              case _ =>
            }
            return e
          case block: Element.Block =>
            return Element.Block(for (alt <- block.alts) yield Alt(for (elem <- alt.elements) yield inlineLexerElement(elem)), block.posOpt)
          case opt: Element.Opt => return Element.Opt(inlineLexerElement(opt.element), opt.posOpt)
          case star: Element.Star => return Element.Star(inlineLexerElement(star.element), star.posOpt)
          case plus: Element.Plus => return Element.Plus(inlineLexerElement(plus.element), plus.posOpt)
          case _ => return e
        }
      }

      if (simpleLexerLiterals.nonEmpty) {
        var result = ISZ[Rule]()
        for (r <- finalRules) {
          if (r.isLexer && simpleLexerLiterals.contains(r.name)) {
            // skip — inlined into parser rules
          } else if (r.isLexer) {
            result = result :+ r
          } else {
            var newAlts = ISZ[Alt]()
            for (alt <- r.alts) {
              newAlts = newAlts :+ Alt(for (e <- alt.elements) yield inlineLexerElement(e))
            }
            result = result :+ Rule(name = r.name, isLexer = r.isLexer, isFragment = r.isFragment, isHidden = r.isHidden, isSynthetic = r.isSynthetic, posOpt = r.posOpt, alts = newAlts)
          }
        }
        finalRules = result
      }

      finalRules = for (r <- finalRules if !r.isHidden) yield r

      // Extract repeated multi-alternative blocks into synthetic helper rules
      def elementWeight(e: Element): Z = {
        e match {
          case _: Element.Dot => return 1
          case _: Element.Char => return 1
          case _: Element.Str => return 1
          case _: Element.Range => return 1
          case _: Element.Ref => return 1
          case block: Element.Block =>
            var w: Z = 1
            for (alt <- block.alts) {
              for (elem <- alt.elements) {
                w = w + elementWeight(elem)
              }
            }
            return w
          case opt: Element.Opt => return 1 + elementWeight(opt.element)
          case star: Element.Star => return 1 + elementWeight(star.element)
          case plus: Element.Plus => return 1 + elementWeight(plus.element)
          case neg: Element.Neg => return 1 + elementWeight(neg.element)
        }
      }

      var blockCounts = HashSMap.empty[Element.Block, Z]
      var blockOrigins = HashSMap.empty[Element.Block, HashSMap[String, Z]]
      var currentCountRuleName: String = ""

      def countBlockElements(e: Element): Unit = {
        e match {
          case block: Element.Block =>
            val prev: Z = blockCounts.get(block) match {
              case Some(n) => n
              case _ => 0
            }
            blockCounts = blockCounts + block ~> (prev + 1)
            val origins: HashSMap[String, Z] = blockOrigins.get(block) match {
              case Some(m) => m
              case _ => HashSMap.empty[String, Z]
            }
            val originPrev: Z = origins.get(currentCountRuleName) match {
              case Some(n) => n
              case _ => 0
            }
            blockOrigins = blockOrigins + block ~> (origins + currentCountRuleName ~> (originPrev + 1))
            for (alt <- block.alts) {
              for (elem <- alt.elements) {
                countBlockElements(elem)
              }
            }
          case opt: Element.Opt => countBlockElements(opt.element)
          case star: Element.Star => countBlockElements(star.element)
          case plus: Element.Plus => countBlockElements(plus.element)
          case neg: Element.Neg => countBlockElements(neg.element)
          case _ =>
        }
      }

      for (r <- finalRules) {
        currentCountRuleName = r.name
        for (alt <- r.alts) {
          for (e <- alt.elements) {
            countBlockElements(e)
          }
        }
      }

      // Find blocks meeting extraction criteria and determine origin rule names
      var extractable = ISZ[Element.Block]()
      for (entry <- blockCounts.entries) {
        if (entry._2 >= 3 && entry._1.alts.size >= 2 && elementWeight(entry._1) >= 6) {
          extractable = extractable :+ entry._1
        }
      }

      // Determine origin rule (rule with most occurrences) for each block
      var originCounts = HashSMap.empty[String, Z]
      for (block <- extractable) {
        val origins: HashSMap[String, Z] = blockOrigins.get(block) match {
          case Some(m) => m
          case _ => HashSMap.empty[String, Z]
        }
        var bestRule: String = "block"
        var bestCount: Z = 0
        for (entry <- origins.entries) {
          if (entry._2 > bestCount) {
            bestCount = entry._2
            bestRule = entry._1
          }
        }
        val prev: Z = originCounts.get(bestRule) match {
          case Some(n) => n
          case _ => 0
        }
        originCounts = originCounts + bestRule ~> (prev + 1)
      }

      // Assign names: _<rule> if unique origin, _<rule>_<n> if multiple blocks share an origin
      var blockReplacements = HashSMap.empty[Element.Block, String]
      var originIdxMap = HashSMap.empty[String, Z]
      for (block <- extractable) {
        val origins: HashSMap[String, Z] = blockOrigins.get(block) match {
          case Some(m) => m
          case _ => HashSMap.empty[String, Z]
        }
        var bestRule: String = "block"
        var bestCount: Z = 0
        for (entry <- origins.entries) {
          if (entry._2 > bestCount) {
            bestCount = entry._2
            bestRule = entry._1
          }
        }
        val total: Z = originCounts.get(bestRule) match {
          case Some(n) => n
          case _ => 1
        }
        val name: String = if (total == 1) {
          st"_$bestRule".render
        } else {
          val idx: Z = originIdxMap.get(bestRule) match {
            case Some(n) => n
            case _ => 0
          }
          originIdxMap = originIdxMap + bestRule ~> (idx + 1)
          st"_${bestRule}_$idx".render
        }
        blockReplacements = blockReplacements + block ~> name
      }

      if (blockReplacements.nonEmpty) {
        def replaceBlockElement(e: Element): Element = {
          e match {
            case block: Element.Block =>
              blockReplacements.get(block) match {
                case Some(name) => return Element.Ref(isTerminal = F, name = name, posOpt = block.posOpt)
                case _ =>
                  return Element.Block(
                    for (alt <- block.alts) yield Alt(for (elem <- alt.elements) yield replaceBlockElement(elem)),
                    block.posOpt
                  )
              }
            case opt: Element.Opt => return Element.Opt(replaceBlockElement(opt.element), opt.posOpt)
            case star: Element.Star => return Element.Star(replaceBlockElement(star.element), star.posOpt)
            case plus: Element.Plus => return Element.Plus(replaceBlockElement(plus.element), plus.posOpt)
            case neg: Element.Neg => return Element.Neg(replaceBlockElement(neg.element), neg.posOpt)
            case _ => return e
          }
        }

        // Build map from origin rule name to its synthetic rules
        var originSynthetics = HashSMap.empty[String, ISZ[Rule]]
        for (entry <- blockReplacements.entries) {
          val block = entry._1
          val name = entry._2
          val newAlts = for (alt <- block.alts) yield Alt(for (e <- alt.elements) yield replaceBlockElement(e))
          val synRule = Rule(
            name = name, isLexer = F, isFragment = F, isHidden = F,
            isSynthetic = T, posOpt = block.posOpt, alts = newAlts
          )
          // Determine origin rule for this block
          val origins: HashSMap[String, Z] = blockOrigins.get(block) match {
            case Some(m) => m
            case _ => HashSMap.empty[String, Z]
          }
          var bestRule: String = "block"
          var bestCount: Z = 0
          for (oe <- origins.entries) {
            if (oe._2 > bestCount) {
              bestCount = oe._2
              bestRule = oe._1
            }
          }
          val prev: ISZ[Rule] = originSynthetics.get(bestRule) match {
            case Some(rs) => rs
            case _ => ISZ[Rule]()
          }
          originSynthetics = originSynthetics + bestRule ~> (prev :+ synRule)
        }

        // Insert each synthetic rule right after its origin rule
        var result = ISZ[Rule]()
        for (r <- finalRules) {
          val newAlts = for (alt <- r.alts) yield Alt(for (e <- alt.elements) yield replaceBlockElement(e))
          result = result :+ Rule(
            name = r.name, isLexer = r.isLexer, isFragment = r.isFragment, isHidden = r.isHidden,
            isSynthetic = r.isSynthetic, posOpt = r.posOpt, alts = newAlts
          )
          originSynthetics.get(r.name) match {
            case Some(synthetics) => result = result ++ synthetics
            case _ =>
          }
        }
        finalRules = result
      }

      return Grammar(id, options, pheaderOpt, lheaderOpt, finalRules)
    }

    def toNGrammar: Option[NGrammar] = {
      val ng = normalize
      ng.computePredictiveTableOpt match {
        case Some(pt) =>
          // Augment the nameMap with all rule names and literals that may not
          // appear in k-token lookahead sequences but are still referenced in rules
          var nameMap = pt.nameMap
          var nextId: S32 = s32"0"
          for (e <- nameMap.entries) {
            if (e._2 >= nextId) {
              nextId = e._2 + s32"1"
            }
          }

          def ensureName(name: String): Unit = {
            if (!nameMap.contains(name)) {
              nameMap = nameMap + name ~> nextId
              nextId = nextId + s32"1"
            }
          }

          def ensureElementNames(e: Element): Unit = {
            e match {
              case s: Element.Str => ensureName(s"'${s.value}'")
              case c: Element.Char =>
                val s = conversions.String.fromCis(ISZ(c.value))
                ensureName(s"'$s'")
              case ref: Element.Ref => ensureName(ref.name)
              case opt: Element.Opt => ensureElementNames(opt.element)
              case star: Element.Star => ensureElementNames(star.element)
              case plus: Element.Plus => ensureElementNames(plus.element)
              case block: Element.Block =>
                for (alt <- block.alts) {
                  for (elem <- alt.elements) {
                    ensureElementNames(elem)
                  }
                }
              case _ =>
            }
          }

          for (r <- ng.rules) {
            ensureName(r.name)
            for (alt <- r.alts) {
              for (e <- alt.elements) {
                ensureElementNames(e)
              }
            }
          }

          val augPt = PredictiveTable(k = pt.k, nameMap = nameMap, rules = pt.rules)

          def toNElement(e: Element): NElement = {
            e match {
              case s: Element.Str =>
                return NElement.Str(s.value, nameMap.get(s"'${s.value}'").get)
              case c: Element.Char =>
                val s = conversions.String.fromCis(ISZ(c.value))
                return NElement.Str(s, nameMap.get(s"'$s'").get)
              case ref: Element.Ref =>
                return NElement.Ref(ref.isTerminal, ref.name, nameMap.get(ref.name).get)
              case _ => halt(s"Unexpected element in normalized grammar: $e")
            }
          }

          var ruleMapMs = MS.create[S32, NRule](conversions.S32.toZ(nextId), NRule.sentinel)
          for (r <- ng.rules if !r.isLexer) {
            val ruleNum = nameMap.get(r.name).get
            if (r.alts.size > 1) {
              var altNums = IS[S32, S32]()
              for (alt <- r.alts) {
                alt.elements(0) match {
                  case ref: Element.Ref =>
                    val refNum = nameMap.get(ref.name).get
                    altNums = altNums :+ refNum
                    if (ref.isTerminal && ruleMapMs(refNum) == NRule.sentinel) {
                      ruleMapMs(refNum) = NRule.Elements(
                        name = ref.name, num = refNum, isSynthetic = T,
                        elements = ISZ(NElement.Ref(isTerminal = T, ruleName = ref.name, num = refNum)))
                    }
                  case _ => halt("Expected single Ref in alt of multi-alt normalized rule")
                }
              }
              ruleMapMs(ruleNum) = NRule.Alts(name = r.name, num = ruleNum, isSynthetic = r.isSynthetic, alts = altNums)
            } else if (r.alts.size == 1) {
              var nelems = ISZ[NElement]()
              for (e <- r.alts(0).elements) {
                nelems = nelems :+ toNElement(e)
              }
              ruleMapMs(ruleNum) = NRule.Elements(name = r.name, num = ruleNum, isSynthetic = r.isSynthetic, elements = nelems)
            }
          }
          return Some(NGrammar(ruleMapMs.toIS, augPt))
        case _ => return None()
      }
    }

    def prettyPrint: ST = {

      def escChar(c: C): ST = {
        c match {
          case '\n' => return st"\\n"
          case '\r' => return st"\\r"
          case '\t' => return st"\\t"
          case '\b' => return st"\\b"
          case '\f' => return st"\\f"
          case '\'' => return st"\\'"
          case '\\' => return st"\\\\"
          case _ =>
            val n = conversions.C.toU32(c)
            if (n >= u32"0x20" && n <= u32"0x7E") {
              return st"$c"
            } else {
              val cat = ops.COps(c).category
              cat match {
                case ops.COps.Category.Cc =>
                case ops.COps.Category.Cf =>
                case ops.COps.Category.Cn =>
                case ops.COps.Category.Co =>
                case ops.COps.Category.Cs =>
                case ops.COps.Category.Zl =>
                case ops.COps.Category.Zp =>
                case ops.COps.Category.Zs =>
                case _ => return st"$c"
              }
              val c1 = ops.COps.hex2c((c >>> '\u0010') & '\u000F')
              val (c2, c3, c4, c5) = ops.COps(c).toUnicodeHex
              return st"\\u$c1$c2$c3$c4$c5"
            }
        }
      }

      def charLit(c: C): ST = {
        return st"'${escChar(c)}'"
      }

      def strLit(s: String): ST = {
        val cis = conversions.String.toCis(s)
        val parts: ISZ[ST] = for (c <- cis) yield escChar(c)
        return st"'${(parts, "")}'"
      }

      @pure def isSimpleAlt(alt: Alt): B = {
        if (alt.elements.size != 1) {
          return F
        }
        val e = alt.elements(0)
        return e.isInstanceOf[Element.Str] || e.isInstanceOf[Element.Char] || e.isInstanceOf[Element.Dot] || e.isInstanceOf[Element.Range] || e.isInstanceOf[Element.Ref]
      }
      @strictpure def allSimpleAlts(alts: ISZ[Alt]): B = ops.ISZOps(alts).forall(isSimpleAlt _)

      def ppElement(e: Element): ST = {
        e match {
          case _: Element.Dot => return st"."
          case c: Element.Char => return charLit(c.value)
          case s: Element.Str => return strLit(s.value)
          case r: Element.Range => return st"${charLit(r.from.value)}..${charLit(r.to.value)}"
          case ref: Element.Ref => return st"${ref.name}"
          case block: Element.Block =>
            val alts: ISZ[ST] = for (a <- block.alts) yield ppAlt(a)
            if (block.alts.size == 1 || allSimpleAlts(block.alts)) {
              return st"""( ${(alts, " | ")} )"""
            }
            val r =
              st"""(
                  |    ${alts(0)}
                  |  ${(for (alt <- ops.ISZOps(alts).drop(1)) yield st"| $alt", "\n")}
                  |)"""
            return r
          case opt: Element.Opt => return st"${ppElement(opt.element)}?"
          case star: Element.Star => return st"${ppElement(star.element)}*"
          case plus: Element.Plus => return st"${ppElement(plus.element)}+"
          case neg: Element.Neg => return st"~${ppElement(neg.element)}"
        }
      }

      def ppAlt(alt: Alt): ST = {
        val elems: ISZ[ST] = for (e <- alt.elements) yield ppElement(e)
        return st"${(elems, " ")}"
      }

      def ppRule(r: Rule): ST = {
        val alts: ISZ[ST] = for (a <- r.alts) yield ppAlt(a)
        if (r.isLexer) {
          val frag: String = if (r.isFragment) "fragment " else ""
          val hidden: String = if (r.isHidden) "{$$channel=HIDDEN;} " else ""
          val rST: ST =
            if (alts.size == 1 || allSimpleAlts(r.alts)) st"$frag${r.name}: ${(alts, " | ")} $hidden;"
            else
              st"""$frag${r.name}:
                  |    ${alts(0)}
                  |  ${(for (alt <- ops.ISZOps(alts).drop(1)) yield st"| $alt", "\n")} $hidden;"""
          return rST
        } else {
          val rST: ST =
            if (alts.size == 1 || allSimpleAlts(r.alts)) st"${r.name}: ${(alts, " | ")} ;"
            else
              st"""${r.name}:
                  |    ${alts(0)}
                  |  ${(for (alt <- ops.ISZOps(alts).drop(1)) yield st"| $alt", "\n")} ;"""
          return rST
        }
      }

      val kOpt: Option[ST] = {
        var r: Option[ST] = None()
        for (p <- options) {
          if (p._1 == "k") {
            r = Some(
              st"""options {
                  |  k = ${p._2};
                  |}"""
            )
          }
        }
        r
      }
      val rulesST: ISZ[ST] = for (r <- rules) yield ppRule(r)
      return st"""grammar $id;
                 |$kOpt
                 |${(rulesST, "\n\n")}
                 |"""
    }

  }

  /** @param isSynthetic `T` if this rule was synthetically introduced by [[Grammar.normalize]] */
  @datatype class Rule(val name: String,
                       val isLexer: B,
                       val isFragment: B,
                       val isHidden: B,
                       val isSynthetic: B,
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

    /** Complement '~' element. Only appears in lexical rules. */
    @datatype class Neg(val element: Element,
                        @hidden val posOpt: Option[Position]) extends Element {
      @strictpure override def isSimple: B = F
    }
  }
}
