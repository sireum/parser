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

/** A compact predictive parsing table stored as a trie with default-based compression.
  *
  * Converts the sparse flat parsing table (which enumerates every k-token lookahead
  * combination) into a trie where each level branches on one lookahead token.
  * When most branches at a level resolve to the same alternative, that alternative
  * is stored as a default and only exceptions are kept, dramatically reducing size.
  *
  * All rule and token names are mapped to unique integer identifiers via `nameMap`
  * for fast integer-keyed lookup throughout the trie.
  *
  * Usage example:
  * {{{
  * grammar.computePredictiveTable(2) match {
  *   case Some(table) =>
  *     val nm = table.nameMap
  *     val atom = nm.get("atom").get
  *     val char = nm.get("CHAR").get
  *     val dotdot = nm.get("..").get
  *     val pid = nm.get("PID").get
  *     table.predict(atom, ISZ(char, dotdot))  // Some(0) — selects 'range'
  *     table.predict(atom, ISZ(char, pid))     // Some(1) — selects 'terminal'
  *   case _ => // grammar is not LL(k)
  * }
  * }}}
  *
  * @param nameMap maps rule and token names to unique U32 identifiers
  * @param rules   a map from rule U32 ID to its root [[PredictiveNode]]
  */
@datatype class PredictiveTable(val nameMap: HashSMap[String, U32],
                                val rules: HashSMap[U32, PredictiveNode]) {
  /** Predicts which alternative to use for the given rule and lookahead token IDs.
    *
    * @param rule   the rule's U32 ID (from `nameMap`)
    * @param tokens the lookahead token ID sequence (up to k tokens, each from `nameMap`)
    * @return `Some(altIndex)` if the tokens uniquely identify an alternative,
    *         or `None` if the rule is not found or the tokens don't match any entry.
    */
  def predict(rule: U32, tokens: ISZ[U32]): Option[U32] = {
    rules.get(rule) match {
      case Some(node) => return node.predict(tokens, 0)
      case _ => return None()
    }
  }
}

object PredictiveTable {
  /** Builds a [[PredictiveTable]] from the flat parsing table produced by
    * [[GrammarAst.Grammar.computeParsingTableOpt]].
    *
    * @param table the flat parsing table mapping rule names to lookahead-to-alt maps
    * @return a compact trie-based table with U32-keyed lookup
    */
  def build(table: HashSMap[String, HashSMap[ISZ[String], Z]]): PredictiveTable = {
    var nameMap = HashSMap.empty[String, U32]
    var nextId: U32 = u32"0"
    for (entry <- table.entries) {
      if (!nameMap.contains(entry._1)) {
        nameMap = nameMap + entry._1 ~> nextId
        nextId = nextId + u32"1"
      }
      for (cell <- entry._2.entries) {
        for (token <- cell._1) {
          if (!nameMap.contains(token)) {
            nameMap = nameMap + token ~> nextId
            nextId = nextId + u32"1"
          }
        }
      }
    }
    var rules = HashSMap.empty[U32, PredictiveNode]
    for (entry <- table.entries) {
      val ruleId = nameMap.get(entry._1).get
      val idEntries: ISZ[(ISZ[U32], U32)] = for (cell <- entry._2.entries) yield
        (for (token <- cell._1) yield nameMap.get(token).get, conversions.Z.toU32(cell._2))
      rules = rules + ruleId ~> buildNode(idEntries, 0)
    }
    return PredictiveTable(nameMap, rules)
  }

  def buildNode(entries: ISZ[(ISZ[U32], U32)], depth: Z): PredictiveNode = {
    if (entries.size <= 1) {
      return PredictiveNode.Leaf(entries(0)._2)
    }
    var allSame: B = T
    val firstAlt = entries(0)._2
    for (e <- entries) {
      if (e._2 != firstAlt) {
        allSame = F
      }
    }
    if (allSame) {
      return PredictiveNode.Leaf(firstAlt)
    }
    var groups = HashSMap.empty[U32, ISZ[(ISZ[U32], U32)]]
    for (e <- entries) {
      if (depth < e._1.size) {
        val tokenId = e._1(depth)
        val existing: ISZ[(ISZ[U32], U32)] = groups.get(tokenId) match {
          case Some(v) => v
          case _ => ISZ()
        }
        groups = groups + tokenId ~> (existing :+ e)
      }
    }
    var children = HashSMap.empty[U32, PredictiveNode]
    for (g <- groups.entries) {
      children = children + g._1 ~> buildNode(g._2, depth + 1)
    }
    var altCounts = HashSMap.empty[U32, Z]
    for (c <- children.entries) {
      c._2 match {
        case leaf: PredictiveNode.Leaf =>
          val count: Z = altCounts.get(leaf.alt) match {
            case Some(n) => n
            case _ => 0
          }
          altCounts = altCounts + leaf.alt ~> (count + 1)
        case _ =>
      }
    }
    var maxAltOpt: Option[U32] = None()
    var maxCount: Z = 0
    for (ac <- altCounts.entries) {
      if (ac._2 > maxCount) {
        maxCount = ac._2
        maxAltOpt = Some(ac._1)
      }
    }
    if (maxCount > 1) {
      val maxAlt = maxAltOpt.get
      var filtered = HashSMap.empty[U32, PredictiveNode]
      for (c <- children.entries) {
        var isDefault: B = F
        c._2 match {
          case leaf: PredictiveNode.Leaf =>
            if (leaf.alt == maxAlt) {
              isDefault = T
            }
          case _ =>
        }
        if (!isDefault) {
          filtered = filtered + c._1 ~> c._2
        }
      }
      return PredictiveNode.Branch(filtered, Some(PredictiveNode.Leaf(maxAlt)))
    } else {
      return PredictiveNode.Branch(children, None())
    }
  }
}

/** A node in the predictive parsing trie.
  *
  * Each node represents a decision point during LL(k) prediction.
  * A [[PredictiveNode.Leaf]] terminates the decision with a resolved alternative,
  * while a [[PredictiveNode.Branch]] inspects the next lookahead token to continue.
  */
@datatype trait PredictiveNode {
  /** Predicts the alternative index by consuming lookahead token IDs starting at position `i`.
    *
    * @param tokens the full lookahead token ID sequence (each ID from [[PredictiveTable.nameMap]])
    * @param i      the current position in the token sequence to inspect
    * @return `Some(altIndex)` if prediction succeeds, or `None` if the tokens
    *         are exhausted before reaching a decision or no matching entry is found.
    */
  def predict(tokens: ISZ[U32], i: Z): Option[U32]
}

object PredictiveNode {
  /** A terminal decision node that always returns the resolved alternative.
    *
    * @param alt the 0-based alternative index into the rule's alternatives
    */
  @datatype class Leaf(val alt: U32) extends PredictiveNode {
    override def predict(tokens: ISZ[U32], i: Z): Option[U32] = {
      return Some(alt)
    }
  }

  /** A branching decision node that inspects the next lookahead token ID.
    *
    * Looks up `tokens(i)` in `entries`; if not found, falls back to `defaultOpt`.
    * The default captures the most common alternative at this level, so only
    * exceptional token IDs need explicit entries.
    *
    * @param entries    explicit token-ID-to-node mappings for non-default alternatives
    * @param defaultOpt fallback node used when the token ID is not in `entries`
    */
  @datatype class Branch(val entries: HashSMap[U32, PredictiveNode],
                         val defaultOpt: Option[PredictiveNode]) extends PredictiveNode {
    override def predict(tokens: ISZ[U32], i: Z): Option[U32] = {
      if (i >= tokens.size) {
        return None()
      }
      entries.get(tokens(i)) match {
        case Some(node) => return node.predict(tokens, i + 1)
        case _ =>
          defaultOpt match {
            case Some(node) => return node.predict(tokens, i + 1)
            case _ => return None()
          }
      }
    }
  }
}
