import org.sireum.justification._
val x: Z = 0
deduce {
  "[8, 11]". x == 0  by Premise
}
val y: Z = 2 + x
deduce {
  1. y == 2 + x  by Premise
  2. x == 0  by Premise
}
val z: Z = 2 + x
deduce {
  1. y == 2 + x  by Premise
  2. z == 2 + x  by Premise
  3. z == y  by Subst_> 1 2
}
assert(z == y)