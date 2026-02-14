import org.sireum.justification._
val x: Z = Z.read
val y: Z = x + 1
deduce {
  "[10, 11]". y == x + 1  by Premise
}
val z: Z = x - 1
deduce {
  1. y == x + 1  by Premise
  2. z == x - 1  by Premise
  3. x == y - 1  by Algebra 1
  4. z == y - 1 - 1  by Subst_< 3 2
  5. z == y - 2  by Algebra 4
}
assert(z == y - 2)