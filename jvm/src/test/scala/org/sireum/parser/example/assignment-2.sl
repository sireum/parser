import org.sireum.justification._
val hours: Z = Z.prompt("Type an int > 2: ")
assume(hours > 2)
deduce {
  "[12, 6]". hours > 2  by Premise
}
val minutes: Z = hours * 60
deduce {
  1. hours > 2  by Premise
  2. minutes == hours * 60  by Premise
  3. minutes > 120  by Algebra 1 2
}
assert(minutes > 120)