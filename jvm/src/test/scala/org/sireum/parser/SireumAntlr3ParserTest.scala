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
import org.sireum.test._


class SireumAntlr3ParserTest extends SireumRcSpec {

  def shouldIgnore(name: Predef.String, isSimplified: Boolean): Boolean = false

  def textResources: scala.collection.SortedMap[scala.Vector[Predef.String], Predef.String] = {
    val m = $internal.RC.text(Vector(
      "../../../../../main/resources/"
    )) { (p, _) => p.last == "SireumAntlr3.g" /*|| p.last == "SlangLl2.g"*/ }

    implicit val ordering: Ordering[Vector[Predef.String]] = m.ordering
    for ((k, v) <- m; pair <- {
      var r = Vector[(Vector[Predef.String], Predef.String)]()
      if (!shouldIgnore(k.last, F)) {
        r = r :+ (k, v)
      }
      r
    }) yield pair
  }

  def check(path: scala.Vector[Predef.String], content: Predef.String): scala.Boolean = {
    val reporter = message.Reporter.create
    val uriOpt = Option.some(st"${(ISZ(path: _*), "/")}".render)
    val tree = SireumAntlr3Parser.parse(uriOpt, content, reporter).get
    val ast = LLkGrammarAstBuilder(tree).build(reporter)
    reporter.printMessages()
    assert(!reporter.hasError)
    val origTree = SireumGrammarParser.parse(uriOpt, content, reporter).get
    val origAst = GrammarAstBuilder(origTree).build(reporter)
    reporter.printMessages()
    assert(!reporter.hasError)
    assert(ast == origAst)
    !reporter.hasIssue
  }

  def decompress(compressed: Array[Byte]): String = {
    import java.io.ByteArrayInputStream
    import java.util.zip.GZIPInputStream
    import scala.util.Try
    Try {
      val inputStream = new GZIPInputStream(new ByteArrayInputStream(compressed))
      scala.io.Source.fromInputStream(inputStream).mkString
    }.toOption.get
  }
}
