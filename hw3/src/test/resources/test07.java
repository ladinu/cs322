package tst;

// test parameter passing
// (should print 6)
class Test {
  public static void main(String[] x) {
    Body b = new Body();
    System.out.println(b.go(1,2,3));
  }
}

class Body {
  public int go(int i, int j, int k) {
    return i+j+k;
  }
}
