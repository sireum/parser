import org.sireum.justification._
val hours: Z = Z.prompt("Type an int > 2: ")
assume(hours > 2)
deduce {
  1. hours > 2  by Premise
  "[13, 6]". hours > 2  by Premise
}