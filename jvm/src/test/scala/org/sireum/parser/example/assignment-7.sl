import org.sireum.justification._
import org.sireum.justification.natded.prop._
var dimes: Z = Z.read
var money: Z = dimes * 10
assert(money == dimes * 10)
deduce {
  "[12, 11]". money == dimes * 10  by Premise
}
dimes = dimes + 1
deduce {
  1. dimes == Old(dimes) + 1  by Premise
  2. money == Old(dimes) * 10  by Premise
  3. Old(dimes) == dimes - 1  by Algebra 1
  4. money == (dimes - 1) * 10  by Subst_< 3 2
}
money = money + 10
deduce {
  1. Old(money) == (dimes - 1) * 10  by Premise
  2. money == Old(money) + 10  by Premise
  3. Old(money) == money - 10  by Algebra 2
  4. money - 10 == (dimes - 1) * 10  by Subst_< 3 1
  5. money == (dimes - 1) * 10 + 10  by Algebra 4
  6. money == dimes * 10  by Algebra 5
}
assert(money == dimes * 10)