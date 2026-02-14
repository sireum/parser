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
import org.sireum.U64._
import org.sireum.message.{Position, Reporter}
import org.sireum.parser.{ParseTree => Tree}
import org.sireum.parser.{GrammarAst => AST}

@datatype class LLkGrammarAstBuilder(tree: ParseTree) {

  val kind: String = "LLkGrammarAstBuilder"

  def build(reporter: Reporter): AST.Grammar = {
    val Tree.Node(trees@ISZ(Tree.Leaf(_), Tree.Node(ISZ(Tree.Leaf(id))), _*)) = tree
    var i = 3
    val options: ISZ[(String, String)] = if (trees(i).ruleName == "optionsSpec") {
      val r = buildOptions(trees(i))
      i = i + 1
      r
    } else {
      ISZ()
    }
    val pheaderOpt: Option[String] = if (trees(i).ruleName == "PHEADER") {
      val Tree.Leaf(pheader) = trees(i)
      i = i + 1
      Some(pheader)
    } else {
      None()
    }
    val lheaderOpt: Option[String] = if (trees(i).ruleName == "LHEADER") {
      val Tree.Leaf(lheader) = trees(i)
      i = i + 1
      Some(lheader)
    } else {
      None()
    }

    var rules = ISZ[AST.Rule]()
    for (j <- i until trees.size - 1) {
      trees(j).ruleName match {
        case string"parserRule" => rules = rules :+ buildParserRule(trees(j), reporter)
        case string"lexerRule" => rules = rules :+ buildLexerRule(trees(j), reporter)
        case _ => halt(s"Infeasible: ${trees(j)}")
      }
    }

    return AST.Grammar(id, options, pheaderOpt, lheaderOpt, rules)
  }

  def buildOptions(tree: Tree): ISZ[(String, String)] = {
    val Tree.Node(trees) = tree
    var r = ISZ[(String, String)]()
    for (i <- 2 until trees.size - 1) {
      val Tree.Node(ISZ(keyId, _, valueNode, _)) = trees(i)
      val key = extractId(keyId)
      val Tree.Node(valueChildren) = valueNode
      val value: String = valueChildren(0) match {
        case n: Tree.Node => extractId(n)
        case l: Tree.Leaf => l.text
      }
      r = r :+ ((key, value))
    }
    return r
  }

  def extractId(tree: Tree): String = {
    tree match {
      case Tree.Node(ISZ(inner)) => return extractId(inner)
      case Tree.Leaf(text) => return text
      case _ => halt(s"Infeasible: $tree")
    }
  }

  @pure def offsetLength(u: U32, z: Z): U64 = {
    return (conversions.U32.toU64(u) << u64"32") | (conversions.Z.toU64(z) & u64"0xFFFFFFFF")
  }

  def buildParserRule(tree: Tree, reporter: Reporter): AST.Rule = {
    val Tree.Node(trees@ISZ(name: Tree.Leaf, _*)) = tree
    var alts = ISZ[AST.Alt]()
    for (i <- 2 until trees.size by 2) {
      alts = alts :+ buildAlt(F, trees(i), reporter)
    }
    return AST.Rule(name = name.text, isLexer = F, isFragment = F, isHidden = F, isSynthetic = F, posOpt = leafPosOpt(name), alts = alts)
  }

  def buildLexerRule(tree: ParseTree, reporter: Reporter): AST.Rule = {
    val Tree.Node(trees) = tree
    var isFragment = F
    var isHidden = F
    var i = 0
    if (trees(i).ruleName == "'fragment'") {
      isFragment = T
      i = i + 1
    }
    val name@Tree.Leaf(_) = trees(i)
    i = i + 2
    var alts = ISZ[AST.Alt]()
    alts = alts :+ buildAlt(T, trees(i), reporter)
    i = i + 1
    if (trees(i).ruleName == "channel") {
      def checkChannel(j: Z): Unit = {
        val Tree.Node(ISZ(_, _, _, lid: Tree.Leaf, _*)) = trees(j)
        if (lid.text != "HIDDEN") {
          reporter.error(leafPosOpt(lid), kind, s"Only HIDDEN channel is supported")
        }
      }
      checkChannel(i)
      isHidden = T
      i = i + 2
      for (j <- i until trees.size - 1 by 3) {
        alts = alts :+ buildAlt(T, trees(j), reporter)
        checkChannel(j + 1)
      }
    } else {
      for (j <- i + 1 until trees.size - 1 by 2) {
        alts = alts :+ buildAlt(T, trees(j), reporter)
      }
    }
    return AST.Rule(name = name.text, isLexer = T, isFragment = isFragment, isHidden = isHidden, isSynthetic = F, posOpt = leafPosOpt(name), alts = alts)
  }

  def buildAlt(isLexer: B, tree: ParseTree, reporter: Reporter): AST.Alt = {
    val Tree.Node(trees) = tree
    var elements = ISZ[AST.Element]()
    for (i <- 0 until trees.size) {
      elements = elements :+ buildElement(isLexer, trees(i), reporter)
    }
    return AST.Alt(elements)
  }

  def buildElement(isLexer: B, tree: ParseTree, reporter: Reporter): AST.Element = {
    val Tree.Node(trees) = tree
    var r: AST.Element = trees(0).ruleName match {
      case string"atom" => buildAtom(isLexer, trees(0), reporter)
      case string"block" => buildBlock(isLexer, trees(0), reporter)
    }
    if (trees.size == 2) {
      val op@Tree.Leaf(_) = trees(1)
      op.text match {
        case string"?" => r = AST.Element.Opt(r, leafPosOpt(op))
        case string"*" => r = AST.Element.Star(r, leafPosOpt(op))
        case string"+" => r = AST.Element.Plus(r, leafPosOpt(op))
        case _ => halt(s"Infeasible: ${trees(1)}")
      }
    }
    return r
  }

  def buildAtom(isLexer: B, tree: ParseTree, reporter: Reporter): AST.Element = {
    val Tree.Node(ISZ(t)) = tree
    t.ruleName match {
      case string"range" => return buildRange(isLexer, t, reporter)
      case string"terminal" => return buildTerminal(isLexer, t, reporter)
      case string"not" => return buildNot(isLexer, t, reporter)
      case string"PID" =>
        return AST.Element.Ref(F, t.asInstanceOf[Tree.Leaf].text, posOpt(t))
    }
  }

  def buildTerminal(isLexer: B, tree: ParseTree, reporter: Reporter): AST.Element = {
    val Tree.Node(ISZ(leaf: Tree.Leaf)) = tree
    leaf.ruleName match {
      case string"LID" => return AST.Element.Ref(T, leaf.text, leafPosOpt(leaf))
      case string"CHAR" => return buildCHAR(leaf)
      case string"STRING" => return buildSTRING(leaf, reporter)
      case string"'.'" =>
        val pOpt = leafPosOpt(leaf)
        if (!isLexer) {
          reporter.error(pOpt, kind, s"Any char '.' specs cannot appear inside parser rules")
        }
        return AST.Element.Dot(pOpt)
      case _ => halt(s"Infeasible: $tree")
    }
  }

  def buildRange(isLexer: B, tree: ParseTree, reporter: Reporter): AST.Element = {
    val Tree.Node(trees) = tree
    val c1 = trees(0)
    val c2 = trees(2)
    val pOpt = posOpts(posOpt(c1), posOpt(c2))
    if (!isLexer) {
      reporter.error(pOpt, kind, s"Range '..' specs cannot appear inside parser rules")
    }
    return AST.Element.Range(buildCHAR(c1), buildCHAR(c2), pOpt)
  }

  def buildNot(isLexer: B, tree: ParseTree, reporter: Reporter): AST.Element = {
    val Tree.Node(ISZ(not: Tree.Leaf, e)) = tree
    val pOpt = posOpt(not)
    if (!isLexer) {
      reporter.error(pOpt, kind, "Complement '~' specs cannot appear inside parser rules")
    }
    e.ruleName match {
      case string"CHAR" => return AST.Element.Neg(buildCHAR(e), pOpt)
      case string"STRING" => return AST.Element.Neg(buildSTRING(e, reporter), pOpt)
      case string"block" => return AST.Element.Neg(buildBlock(isLexer, e, reporter), pOpt)
      case _ => halt(s"Infeasible: $tree")
    }
  }

  @pure def cis2C(cis: ISZ[C], i: Z): (C, Z) = {
    cis(i) match {
      case c"\\" =>
        cis(i + 1) match {
          case c"n" => return ('\n', 2)
          case c"r" => return ('\r', 2)
          case c"t" => return ('\t', 2)
          case c"b" => return ('\b', 2)
          case c"f" => return ('\f', 2)
          case c"'" => return ('\'', 2)
          case c"\"" => return ('"', 2)
          case c"\\" => return ('\\', 2)
          case c"u" => return (ops.COps.fromUnicodeHex(ISZ(
            cis(i + 2), cis(i + 3), cis(i + 4), cis(i + 5))).get, 6)
        }
      case c => return (c, 1)
    }
  }

  def buildCHAR(tree: ParseTree): AST.Element.Char = {
    val leaf@Tree.Leaf(_) = tree
    val cis = conversions.String.toCis(leaf.text)
    return AST.Element.Char(cis2C(cis, 1)._1, leafPosOpt(leaf))
  }

  def buildSTRING(tree: ParseTree, reporter: Reporter): AST.Element = {
    val leaf@Tree.Leaf(_) = tree
    val cis = conversions.String.toCis(leaf.text)
    val pOpt = leafPosOpt(leaf)
    def error(): Unit = {
      reporter.error(pOpt, kind, s"Could not recognize full unicode escape '${leaf.text}'")
    }
    cis match {
      case ISZ('\\', '\\', 'u', '{', '0', 'x', _*) if cis(cis.size - 1) == '}' =>
        U32(ops.StringOps.substring(cis, 6, cis.size - 1)) match {
          case Some(n) if n <= u32"0x10FFFF" => return AST.Element.Char(conversions.U32.toC(n), pOpt)
          case _ => error()
        }
      case ISZ('\\', '\\', 'u', '{', _*) if cis(cis.size - 1) == '}' =>
        U32(s"0x${ops.StringOps.substring(cis, 4, cis.size - 1)}") match {
          case Some(n) if n <= u32"0x10FFFF" => return AST.Element.Char(conversions.U32.toC(n), pOpt)
          case _ => error()
        }
      case _ =>
    }
    var cs = ISZ[C]()
    val last = cis.size - 1
    var i: Z = 1
    while (i < last) {
      val p = cis2C(cis, i)
      cs = cs :+ p._1
      i = i + p._2
    }
    return AST.Element.Str(conversions.String.fromCis(cs), pOpt)
  }

  def buildBlock(isLexer: B, tree: ParseTree, reporter: Reporter): AST.Element.Block = {
    val Tree.Node(trees) = tree
    var alts = ISZ[AST.Alt]()
    for (i <- 1 to trees.size - 1 by 2) {
      alts = alts :+ buildAlt(isLexer, trees(i), reporter)
    }
    return AST.Element.Block(alts, posOpt(tree))
  }

  @pure def posOpt(tree: Tree): Option[Position] = {
    tree match {
      case tree: Tree.Node => return posOpts(posOpt(tree.children(0)), posOpt(tree.children(tree.children.size - 1)))
      case tree: Tree.Leaf => return leafPosOpt(tree)
    }
  }

  @pure def posOpts(posOpt1: Option[Position], posOpt2: Option[Position]): Option[Position] = {
    return Some(posOpt1.get.to(posOpt2.get))
  }

  @pure def leafPosOpt(leaf: Tree.Leaf): Option[Position] = {
    return leaf.posOpt
  }
}
