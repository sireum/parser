import org.sireum.justification._
val x: Z = Z.prompt("Enter x: ")
val y: Z = Z.prompt("Enter y (< x): ")
assume(x > y)
deduce {
  "[12, 11]". x > y  by Premise
}
val max: Z = x
deduce {
  1. max == x  by Premise
  2. x > y  by Premise
  3. max >= x  by Algebra 1
  4. max >= y  by Algebra 3 2
}
assert(max >= x)
assert(max >= y)