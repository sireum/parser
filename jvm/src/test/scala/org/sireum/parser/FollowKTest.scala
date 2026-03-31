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

import org.scalatest.freespec.AnyFreeSpec
import org.sireum._

object FollowKTest {

  import GrammarAst._

  val grammarPath: Os.Path = Os.path(Os.env("SIREUM_HOME").get) / "slang" / "parser" / "shared" / "src" / "main" / "resources" / "SlangLl2.g"

  def tRef(name: Predef.String): Element.Ref = Element.Ref(T, name, None())
  def ntRef(name: Predef.String): Element.Ref = Element.Ref(F, name, None())
  def alt(es: Element*): Alt = Alt(ISZ(es: _*))
  def parserRule(name: Predef.String, alts: Alt*): Rule =
    Rule(name, isLexer = F, isFragment = F, isHidden = F, isSynthetic = F, posOpt = None(), alts = ISZ(alts: _*))
  def lexRule(name: Predef.String): Rule =
    Rule(name, isLexer = T, isFragment = F, isHidden = F, isSynthetic = F, posOpt = None(), alts = ISZ(alt(Element.Str(name, None()))))

  // Minimal grammar reproducing the EOF propagation bug:
  //   file: program EOF ;
  //   program: imprt? ;
  //   imprt: IMPORT ID importIdSuffix? ;
  //   importIdSuffix: DOT choice ;
  //   choice: UNDERSCORE | ID ;
  val minimalGrammar: Grammar = Grammar(
    id = "Test",
    options = ISZ(("k", "2")),
    pheaderOpt = None(),
    lheaderOpt = None(),
    rules = ISZ(
      parserRule("file", alt(ntRef("program"), tRef("EOF"))),
      parserRule("program", alt(Element.Opt(ntRef("imprt"), None()))),
      parserRule("imprt", alt(tRef("IMPORT"), tRef("ID"), Element.Opt(ntRef("importIdSuffix"), None()))),
      parserRule("importIdSuffix", alt(tRef("DOT"), ntRef("choice"))),
      parserRule("choice", alt(tRef("UNDERSCORE")), alt(tRef("ID"))),
      lexRule("EOF"), lexRule("IMPORT"), lexRule("ID"),
      lexRule("DOT"), lexRule("UNDERSCORE")
    )
  )

  lazy val slangLl2Ast: Grammar = {
    val content = grammarPath.read
    val reporter = message.Reporter.create
    val parseTree = SireumGrammarParser.parse(Some("SlangLl2.g"), content, reporter)
    assert(!reporter.hasError, s"Grammar lex/parse failed: ${reporter.messages}")
    val g = GrammarAstBuilder(parseTree.get).build(reporter)
    assert(!reporter.hasError, s"Grammar AST build failed: ${reporter.messages}")
    g
  }
}

class FollowKTest extends AnyFreeSpec {

  import FollowKTest._

  "FOLLOW_K seeding propagates EOF" in {
    val g = minimalGrammar
    val gn = g.normalize
    println("=== Normalized rules ===")
    for (r <- gn.rules if !r.isLexer) {
      println(s"  ${r.name}: ${r.alts}")
    }
    val firstKMap = gn.computeFirstKSets(Z(2))
    val followKMap = gn.computeFollowKSets(Z(2), firstKMap)
    println("=== FOLLOW_K sets ===")
    for (e <- followKMap.entries) {
      println(s"  FOLLOW_K(${e._1}) = ${e._2}")
    }
    val choiceFollow = followKMap.get("choice").get
    var hasEof = false
    for (seq <- choiceFollow.elements) {
      for (tok <- seq) {
        if (tok.toString == "EOF") hasEof = true
      }
    }
    assert(hasEof, s"choice FOLLOW_K missing EOF. Got: $choiceFollow")
  }

  "parsing table includes EOF lookahead for choice" in {
    val g = minimalGrammar
    val gn = g.normalize
    val table = gn.computeParsingTable
    println("=== Parsing table for choice ===")
    for (e <- table.entries if e._1.toString == "choice") {
      for (cell <- e._2.entries) {
        println(s"  ${cell._1} -> ${cell._2}")
      }
    }
    val choiceTable = table.get("choice").get
    var hasUndEof = false
    for (e <- choiceTable.entries) {
      if (e._1.size == 2 && e._1(0).toString == "UNDERSCORE" && e._1(1).toString == "EOF") {
        hasUndEof = true
      }
    }
    assert(hasUndEof, s"choice table missing (UNDERSCORE, EOF). Got: ${choiceTable}")
  }

  "SlangLl2 grammar: importIdSuffix FOLLOW_K includes EOF" in {
    val gn = slangLl2Ast.normalize
    val firstKMap = gn.computeFirstKSets(Z(2))
    val followKMap = gn.computeFollowKSets(Z(2), firstKMap)

    println("=== SlangLl2 FOLLOW_K (import-related) ===")
    for (e <- followKMap.entries) {
      if (e._1.toString.contains("import") || e._1.toString == "file" || e._1.toString == "program") {
        println(s"  FOLLOW_K(${e._1}) = ${e._2}")
      }
    }

    // importIdSuffix should have EOF in its follow set
    val isFollow = followKMap.get("importIdSuffix").get
    var hasEof = false
    for (seq <- isFollow.elements) {
      for (tok <- seq) {
        if (tok.toString == "EOF") hasEof = true
      }
    }
    assert(hasEof, s"importIdSuffix FOLLOW_K missing EOF. Got: $isFollow")
  }

  "SlangLl2 grammar: predictive table has (UNDERSCORE, EOF) for importIdSuffix choice" in {
    val gn = slangLl2Ast.normalize
    val table = gn.computeParsingTable
    var found = false
    for (e <- table.entries) {
      val name = e._1.toString
      if (name.startsWith("importIdSuffix$")) {
        println(s"=== $name ===")
        for (cell <- e._2.entries) {
          println(s"  ${cell._1} -> ${cell._2}")
          if (cell._1.size == 2 && cell._1(0).toString == "UNDERSCORE" && cell._1(1).toString == "EOF") {
            found = true
          }
        }
      }
    }
    assert(found, "No (UNDERSCORE, EOF) entry found in any importIdSuffix$ rule")
  }
}
