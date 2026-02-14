import org.sireum.justification._
def absValue(x: Z): Z @[
  requires x != 0
  ensures Res > 0
] = {
  var ans: Z = 0
  if x < 0 {
    deduce {
      1. x != 0  by Premise
      2. x < 0  by Premise
    }
    ans = -x
    deduce {
      1. x < 0  by Premise
      2. ans == -x  by Premise
      3. ans + x == 0  by Algebra 2
      4. x < ans + x  by Subst_> 3 1
      5. ans > 0  by Algebra 4
    }
  } else {
    deduce {
      1. x != 0  by Premise
      2. !(x < 0)  by Premise
      3. x >= 0  by Algebra 2
      4. x > 0  by Algebra 1 3
    }
    ans = x
    deduce {
      1. ans == x  by Premise
      2. x > 0  by Premise
      3. ans > 0  by Subst_> 1 2
    }
  }
  deduce {
    "[56, 13]". ans > 0  by Premise
  }
  return ans
}
val n: Z = Z.read
if n != 0 {
  deduce {
    "[64, 13]". n != 0  by Premise
  }
  val m: Z = absValue(n)
  deduce {
    "[68, 13]". m > 0  by Premise
  }
  assert(m > 0)
}