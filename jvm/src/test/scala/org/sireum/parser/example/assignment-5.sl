import org.sireum.justification._
var x: Z = Z.prompt("Enter an integer > 0: ")
assume(x > 0)
deduce {
  "[10, 11]". x > 0  by Premise
}
x = x + 1
deduce {
  1. x == Old(x) + 1  by Premise
  2. Old(x) > 0  by Premise
  3. Old(x) + 1 > 1  by Algebra 2
  4. x > 1  by Subst_> 1 3
}
assert(x > 1)