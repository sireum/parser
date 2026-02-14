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
assert(y == 2 + x)
assert(x == 0)