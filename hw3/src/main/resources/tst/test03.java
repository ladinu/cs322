package tst;

// test simple arith
// (should print 1, 7)
class Test {
  public static void main(String[] x) {
    boolean b = (1>2) || (3<4) && !false;
    int i = 2 + 2 * 4 - 9 / 3;
    System.out.println(b);
    System.out.println(i);
  }
}
