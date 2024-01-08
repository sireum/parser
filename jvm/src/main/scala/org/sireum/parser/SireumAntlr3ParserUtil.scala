/*
 Copyright (c) 2017-2024, Robby, Kansas State University
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

import org.antlr.runtime.{ANTLRStringStream, CommonTokenStream, RecognitionException}
import org.sireum._

object SireumAntlr3ParserUtil {

  def parseGrammar(uriOpt: Option[String],
                   input: String,
                   reporter: message.Reporter): Option[ParseTree] = {
    val docInfo = message.DocInfo.create(uriOpt, input)

    val lex = new SireumAntlr3Lexer(new ANTLRStringStream(input.value)) {
      override def displayRecognitionError(tokenNames: Array[Predef.String], e: RecognitionException): Unit = {
        val msg = getErrorMessage(e, tokenNames)
        val line = e.line
        val column = U32(e.charPositionInLine)
        val offsetLength = (conversions.U32.toU64(docInfo.lineOffsets(line - 1) + column) << U64(32)) | U64(1)
        reporter.error(Some(message.PosInfo(docInfo, offsetLength)), "SireumAntlr3Lexer", msg)
      }
    }
    val cts = new CommonTokenStream(lex)
    val r = new SireumAntlr3Parser(cts) {
      override def displayRecognitionError(tokenNames: Array[Predef.String], e: RecognitionException): Unit = {
        val msg = getErrorMessage(e, tokenNames)
        val line = e.line
        val column = U32(e.charPositionInLine)
        val offsetLength = (conversions.U32.toU64(docInfo.lineOffsets(line - 1) + column) << U64(32)) | U64(e.token.getText.length)
        reporter.error(Some(message.PosInfo(docInfo, offsetLength)), "SireumAntlr3Parser", msg)
      }
    }
    r.setTreeAdaptor(new Antlr3Util.Adaptor(SireumAntlr3Parser.tokenNames, docInfo))
    val tree = r.grammarDef().tree.asInstanceOf[ParseTree]
    return Some(tree)
  }

}
