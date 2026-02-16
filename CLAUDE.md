# Parser Module

## LLkParserGenerator

`LLkParserGenerator` (`shared/src/main/scala/org/sireum/parser/LLkParserGenerator.scala`) generates a complete LL(k) parser source file from a `GrammarAst.Grammar`. The generated parser uses `NGrammar` for parsing and `LexerDfas` for lexing.

### Generation Pipeline (`gen`)

```
GrammarAst.Grammar
  → Grammar.normalize         (expand *, +, ? into synthetic rules; inline fragments)
  → normalized.toNGrammar     (build NGrammar with PredictiveTable)
  → collect string/char literals + lexer rules
  → build DFAs for each token (NFA → DFA → minimize)
  → LexerDfas.fromDfas(...)   (package all DFAs into a single LexerDfas)
  → serialize NGrammar + LexerDfas to compact base64 strings
  → emit parser object source code
```

### DFA Construction for Lexer Rules

Each lexer rule and string/char literal gets its own minimized DFA:

1. **NFA construction** (Thompson's construction):
   - `nfaElement` — dispatches on `GrammarAst.Element` variants:
     - `Char` → single-transition NFA for one character
     - `Str` → chain of single-character NFAs concatenated
     - `Range` → single-transition NFA for a character range `(from, to)`
     - `Dot` → single-transition NFA for `(minChar, maxChar)` (any character)
     - `Block` → union of alternatives via `nfaAlts`
     - `Opt` (`?`) → inner NFA with epsilon bypass
     - `Star` (`*`) → inner NFA with epsilon bypass + back-edge
     - `Plus` (`+`) → inner NFA with back-edge (no bypass)
     - `Neg` (`~`) → complement via `nfaNeg`
     - `Ref` → halts (fragments must be inlined before NFA construction)
   - `nfaAlt` — concatenates a sequence of elements
   - `nfaAlts` — union of alternatives with shared initial/accept states

2. **Negation** (`nfaNeg`):
   - Builds DFA for the inner element
   - Completes the DFA with a sink state (fills gaps via `complementRanges`)
   - Complements accepting states
   - Intersects with single-character DFA (only initial→accept transitions from complemented states)

3. **Subset construction** (`nfaToDfa`):
   - Standard NFA-to-DFA conversion via `Nfa.toDfa`
   - Uses `splitIntervals` to split overlapping character-range transitions into disjoint intervals, ensuring each DFA transition covers a non-overlapping range

4. **Minimization** (`minimize`):
   - Partition refinement via `Dfa.minimize`

### Helper: `splitIntervals`

Splits overlapping `(lo, hi, target)` character-range transitions into disjoint intervals. Each resulting interval maps to all NFA states reachable through any overlapping original range. This is critical for correct subset construction when multiple NFA transitions overlap on character ranges.

### Helper: `complementRanges`

Computes the complement of a set of character ranges with respect to `[minChar, maxChar]` (where `maxChar = Dfa.maxChar`, covering the full Unicode range up to U+10FFFF). Used by `nfaNeg` to fill DFA gaps with transitions to the sink state.

### Generated Parser Structure

The generated file (e.g., `SlangLl2Parser.scala`) contains:

```scala
object XxxParser {
  val g: NGrammar = NGrammar.fromCompact("...")         // deserialized at load time
  val lexerDfas: LexerDfas = LexerDfas.fromCompact("...") // deserialized at load time

  def parseRule(uriOpt: Option[String], content: String, ruleName: String,
                reporter: message.Reporter): Option[ParseTree]

  def parse(uriOpt: Option[String], content: String,
            reporter: message.Reporter): Option[ParseTree]
}
```

- `parseRule` — lexes input via `lexerDfas.tokens`, then parses from the given rule name via `g.parse`
- `parse` — convenience method that parses from the grammar's start rule (first rule)
- Large compact strings (>60000 chars) are split into chunk `val`s and concatenated via `st"..."` to stay within JVM's 65535-byte string constant limit

### DFA Info Tuples

`LexerDfas.fromDfas` takes `ISZ[(Dfa[(C, C)], String, Z, B)]` where each tuple is:
- `Dfa[(C, C)]` — the minimized DFA
- `String` — the token name (e.g., `"ID"`, `"'val'"`)
- `Z` — the token type ID from `PredictiveTable.nameMap`
- `B` — `T` if the token is hidden (whitespace/comments, skipped during parsing)
