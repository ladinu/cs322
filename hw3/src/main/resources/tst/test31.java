package tst;

// static vs dynamic binding
// dynamic: (should print 2 2)
// static:  (should print 1 1)
class Test {
  public static void main(String[] x) {
    A a = new B();
    B b = new B();
    int i = a.g(2);
    int j = b.g(2);
    System.out.println(i);
    System.out.println(j);
  }
}

class A {
  int x=1;
  public int f(int i) { return i-1; }
  public int g(int i) { return f(i); }
}

class B extends A {
  int x=2;
  public int f(int i) { return i; }
}
