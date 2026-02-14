import org.sireum.justification._
val x: Z = 1
val y: Z = 5
deduce {
  1. x == 1  by Premise
  2. y == 5  by Premise
}
val q: Z = x + y
deduce {
  1. q == x + y  by Premise
  2. x == 1  by Premise
  3. y == 5  by Premise
  4. q == 1 + y  by Subst_< 2 1
  5. q == 1 + 5  by Subst_< 3 4
  6. q > 5  by Algebra 5
}
assert(q > 5)
deduce {
  1. q > 5  by Premise
  2. x == 1  by Premise
}