import org.sireum.justification._
def add1(x: Z): Z @[
  requires x > 0 &
    x < 100
  ensures Res == x + 1
] = {
  val y: Z = x + 1
  return y
}
val a: Z = Z.read
if a > 0 &
     a < 100 {
  deduce {
    "[19, 13]". a > 0 &
      a < 100  by Premise
  }
  val b: Z = add1(a)
  deduce {
    "[23, 13]". b == a + 1  by Premise
  }
  assert(b == a + 1)
}