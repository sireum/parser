import org.sireum.justification._
import org.sireum.justification.natded.prop._
var interest: Z = 0
var interestRate: Z = 10
def calcInterest(principal: Z): Z @[
  requires interestRate >= 0 &
    principal >= 0
  modifies interest
  ensures interest == principal * interestRate
] = {
  interest = principal * interestRate
  return interest
}
var myMoney: Z = 500
deduce {
  1. myMoney == 500  by Premise
  2. myMoney >= 0  by Algebra 1
}
assert(myMoney >= 0)
deduce {
  1. interestRate == 10  by Premise
  2. interestRate >= 0  by Algebra 1
}
assert(interestRate >= 0)
deduce {
  1. interestRate >= 0  by Premise
  2. myMoney >= 0  by Premise
  3. interestRate >= 0 &
    myMoney >= 0  by AndI 1 2
}
calcInterest(myMoney)
deduce {
  "[51, 11]". interest == myMoney * interestRate  by Premise
}
assert(interest == myMoney * interestRate)