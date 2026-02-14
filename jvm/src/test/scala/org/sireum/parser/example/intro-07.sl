import org.sireum.justification._
import org.sireum.justification.natded.prop._
val x: Z = 1
deduce {
  "[9, 11]". x == 1  by Premise
}
val y: Z = 2
deduce {
  1. x == 1  by Premise
  2. y == 2  by Premise
  3. x == 1 |
    y == 5  by OrI1 1
}
assert(x == 1 |
   y == 5)