package tst;

// test boolean exprs
// (should print 1)
class Test {
  public static void main(String[] x) {
    A a = new A();
    System.out.println(a.go());
  }
}

class A {
  public int go() {
    boolean a;
    boolean b;
    boolean c;
    int x;
    a = true;
    b = !a;
    c = a && b || a;
    if (c)
      x = 1;
    else
      x = 0;
    return x;
  }
}
