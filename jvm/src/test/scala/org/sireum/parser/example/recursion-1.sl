import org.sireum.justification._
def multRec(m: Z, n: Z): Z @[
  requires n >= 0
  ensures Res == m * n
] = {
  var r: Z = 0
  if n != 0 {
    deduce {
      1. n >= 0  by Premise
      2. n != 0  by Premise
      3. n - 1 >= 0  by Algebra 1 2
    }
    r = multRec(m, n - 1)
    deduce {
      "[28, 15]". r == m * (n - 1)  by Premise
    }
    r = m + r
    deduce {
      1. Old(r) == m * (n - 1)  by Premise
      2. r == m + Old(r)  by Premise
      3. r == m + m * (n - 1)  by Subst_< 1 2
      4. r == m * n  by Algebra 3
    }
  } else {
    deduce {
      1. !(n != 0)  by Premise
      2. r == 0  by Premise
      3. r == m * n  by Algebra 1 2
    }
  }
  return r
}