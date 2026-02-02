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
                          val rules: ISZ[Rule])

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
