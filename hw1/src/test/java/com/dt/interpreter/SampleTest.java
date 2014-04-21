package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class SampleTest extends BaseTest {
   @Test
   public void main() {
      String snippet = "" +
            "    var t = 1;" +
            "    var i = 1;" +
            "    while ((i < 11)) {" +
            "      t = (t * i);" +
            "      i = (i + 1);" +
            "    }" +
            "    print t;";
      Assert.assertEquals(output("3628800"), run(snippet));
   }

   @Test
   public void mainDT() {
      String snippet = "" +
            " var i = (0 == 0);" +
            "    if (i) {" +
            "      i = (41 + 1);" +
            "    } else {" +
            "      print i;" +
            "    }" +
            "    print i;";
      Assert.assertEquals(output("42"), run(snippet));
   }

   @Test
   public void mainFCF() {
      String snippet = "" +
            "    procedure adder(n, ref f) {" +
            "      f = (\\m -> (n + m));" +
            "    }" +
            "    var double = (\\x -> (x + x));" +
            "    var inc = (\\x -> (x + 1));" +
            "    var comp = (\\f -> (\\g -> (\\x -> (f @ (g @ x)))));" +
            "    var toOdd = ((comp @ inc) @ double);" +
            "    print (toOdd @ 3);" +
            "    print (toOdd @ 9);" +
            "    adder(4, inc);" +
            "    print (inc @ 1);" +
            "    print (inc @ 0);";
      Assert.assertEquals(outputs("7 & 19 & 5 & 4"), run(snippet));
   }

   @Test
   public void mainNest() {
      String snippet = "" +
            "    var i = 0;" +
            "    if ((i == 0)) {" +
            "      var i = 1;" +
            "      print i;" +
            "    } else {" +
            "      i = 3;" +
            "    }" +
            "    print i;";
      Assert.assertEquals(outputs("1 & 0"), run(snippet));
   }

   @Test
   public void mainProc() {
      String snippet = "" +
            "    procedure gauss(limit) {" +
            "      var t = 0;" +
            "      var i = 0;" +
            "      while ((i < limit)) {" +
            "        i = (i + 1);" +
            "        t = (t + i);" +
            "      }" +
            "      print t;" +
            "    }" +
            "    procedure double(x) {" +
            "      print (x + x);" +
            "    }" +
            "    procedure sum(n, a) {" +
            "      if ((0 < n)) {" +
            "        sum((n - 1), (a + n));" +
            "      } else {" +
            "        print a;" +
            "      }" +
            "    }" +
            "    var n = 97207;" +
            "    gauss(5);" +
            "    double(21);" +
            "    gauss(10);" +
            "    sum(10, 0);" +
            "    print n;";
      Assert.assertEquals(outputs("15 & 42 & 55 & 55 & 97207"), run(snippet));
   }

   @Test
   public void mainRef() {
      String snippet = "" +
            "    procedure byref(ref x, ref y) {" +
            "      redirect(x);" +
            "      redirect(y);" +
            "      print (x + y);" +
            "    }" +
            "    procedure redirect(ref x) {" +
            "      inc(x);" +
            "    }" +
            "    procedure inc(ref y) {" +
            "      y = (y + 1);" +
            "    }" +
            "    var z = 25;" +
            "    byref(z, 23);" +
            "    print z;";
      Assert.assertEquals(outputs("50 & 26"), run(snippet));
   }
}

