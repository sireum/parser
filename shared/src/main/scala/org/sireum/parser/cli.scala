// #Sireum
/*
 Copyright (c) 2017-2022, Robby, Kansas State University
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
import org.sireum.cli.CliOpt._

object cli {

  val parserGenTool: Tool = Tool(
    name = "gen",
    command = "gen",
    description = "Parser generator",
    header = "Sireum Parser Generator",
    usage = "<option>* <file.g>",
    usageDescOpt = None(),
    opts = ISZ(
      Opt(name = "memoize", longKey = "memoize", shortKey = Some('z'),
        tpe = Type.Flag(F),
        description = "Use memoization in the generated parser"),
      Opt(name = "mode", longKey = "mode", shortKey = Some('m'),
        tpe = Type.Choice(name = "ParserGenMode", sep = None(), elements = ISZ("slang", "antlr3")),
        description = "Grammar input parsing/lexing engines"),
      Opt(name = "name", longKey = "name", shortKey = Some('n'),
        tpe = Type.Str(sep = None(), default = None()),
        description = "Type simple name for the generated parser/lexer suffix (default: the grammar file name without extension)"),
      Opt(name = "license", longKey = "license", shortKey = Some('l'),
        tpe = Type.Path(multiple = F, default = None()), description = "License file to be inserted in the file header"),
      Opt(name = "outputDir", longKey = "output-dir", shortKey = Some('o'),
        tpe = Type.Path(multiple = F, default = Some(".")), description = "Output directory for the generated transformer Slang files"),
      Opt(name = "packageName", longKey = "package", shortKey = Some('p'),
        tpe = Type.Str(sep = Some('.'), default = None()),
        description = "Package name for the generated parser/lexer"),
    ),
    groups = ISZ()
  )

  val group: Group = Group(
    name = "parser",
    description = "Parser tools",
    header = "Sireum Parser Tools",
    unlisted = F,
    subs = ISZ(parserGenTool)
  )
}
