import org.sireum.justification._
import org.sireum.justification.natded.prop._
import org.sireum.justification.natded.pred._
def @spec f(n: Z): Z
def @fact fFacts = (
  f(0) == 1,
  ∀ x: Z => (x > 0) -> (f(x) == f(x - 1) * x)
)
def factorialRec(n: Z): Z @[
  requires n >= 0
  ensures Res == f(n)
] = {
  var r: Z = 1
  if n != 0 {
    deduce {
      1. n != 0  by Premise
      2. n >= 0  by Premise
      3. n - 1 >= 0  by Algebra 1 2
    }
    r = factorialRec(n - 1)
    deduce {
      "[47, 15]". r == f(n - 1)  by Premise
    }
    r = r * n
    deduce {
      1. Old(r) == f(n - 1)  by Premise
      2. r == Old(r) * n  by Premise
      3. n != 0  by Premise
      4. n >= 0  by Premise
      5. r == f(n - 1) * n  by Subst_< 1 2
      6. ∀ x: Z => (x > 0) -> (f(x) == f(x - 1) * x)  by ClaimOf(fFacts _)
      7. (n > 0) -> (f(n) == f(n - 1) * n)  by AllE[Z] 6
      8. n > 0  by Algebra 3 4
      9. f(n) == f(n - 1) * n  by ImplyE 7 8
      10. r == f(n)  by Algebra 5 9
    }
  } else {
    deduce {
      1. !(n != 0)  by Premise
      2. r == 1  by Premise
      3. f(0) == 1  by ClaimOf(fFacts _)
      4. r == f(n)  by Algebra 1 2 3
    }
  }
  return r
}