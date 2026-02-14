import org.sireum.justification._
def mult(m: Z, n: Z): Z @[
  requires m >= 0 &
    n >= 0
  ensures Res == m * n
] = {
  var r: Z = 0
  var i: Z = 0
  println("Before loop: m = ", m, ", n = ,", n, ", i = ", i, ", r = ", r)
  deduce {
    1. r == 0  by Premise
    2. i == 0  by Premise
    3. r == m * i  by Algebra 1 2
  }
  while i != n @[
    modifies r, i
    inv r == m * i
  ] {
    println("Loop pre: m = ", m, ", n = ", n, ", i = ", i, ", r = ", r)
    r = r + m
    deduce {
      1. r == Old(r) + m  by Premise
      2. Old(r) == m * i  by Premise
      3. r == m * i + m  by Subst_< 2 1
    }
    i = i + 1
    deduce {
      1. i == Old(i) + 1  by Premise
      2. r == m * Old(i) + m  by Premise
      3. r == m * i  by Algebra 1 2
    }
    println("Loop post: m = ", m, ", n = ", n, ", i = ", i, ", r = ", r)
  }
  println("After loop: m = ", m, ", n = ", n, ", i = ", i, ", r = ", r)
  deduce {
    1. !(i != n)  by Premise
    2. r == m * i  by Premise
    3. r == m * n  by Algebra 1 2
  }
  return r
}