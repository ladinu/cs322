package tst;

// Testing class variable initialization.
// (should print 2 5 3)
class Test {
  public static void main(String[] x) {
    A a = new A();
    a.setk(5);
    System.out.println(a.j);
    System.out.println(a.k);
    System.out.println(a.l);
  }
}

class A {
  int i = 2;
  int j = i;
  int k = 3;
  //  int l = this.k;
  int l = k;
  
  public void setk(int i) {
    //    this.k = i;
    k = i;
  }
}
