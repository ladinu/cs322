package tst;

// test array
// (should print 4 3)
class Test {
  public static void main(String[] x) {
    A a = new A();
    System.out.println(a.go());
  }
}

class A {
  public int go() {
    int[] b;
    b = new int[2];
    b[0] = 3;
    b[1] = 4;
    System.out.println(b[1]);
    return b[0];
  }
}
