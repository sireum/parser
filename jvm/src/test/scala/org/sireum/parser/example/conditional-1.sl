import org.sireum.justification._
import org.sireum.justification.natded.prop._
val x: Z = Z.read
val y: Z = Z.read
var max: Z = 0
if x > y {
  deduce {
    "[14, 13]". x > y  by Premise
  }
  max = x
  deduce {
    1. max == x  by Premise
    2. x > y  by Premise
    3. max >= x  by Algebra 1
    4. max > y  by Subst_> 1 2
    5. max >= y  by Algebra 4
  }
} else {
  deduce {
    "[30, 13]". !(x > y)  by Premise
  }
  max = y
  deduce {
    1. max == y  by Premise
    2. !(x > y)  by Premise
    3. max >= y  by Algebra 1
    4. y >= x  by Algebra 2
    5. max >= x  by Subst_> 1 4
  }
}
deduce {
  1. max >= x  by Premise
  2. max >= y  by Premise
  3. max == x |
    max == y  by Premise
  4. max >= x &
    max >= y  by AndI 1 2
  5. max >= x &
    max >= y &
    (max == x |
      max == y)  by AndI 4 3
}
assert(max >= x &
   max >= y &
   (max == x |
     max == y))