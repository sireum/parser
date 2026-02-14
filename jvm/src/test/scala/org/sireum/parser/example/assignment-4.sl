import org.sireum.justification._
var hours: Z = 4
deduce {
  "[8, 11]". hours == 4  by Premise
}
val minutes: Z = hours * 60
deduce {
  1. hours == 4  by Premise
  2. minutes == hours * 60  by Premise
  3. minutes == 240  by Algebra 1 2
}
hours = 5
deduce {
  1. hours == 5  by Premise
  2. minutes == 240  by Premise
}