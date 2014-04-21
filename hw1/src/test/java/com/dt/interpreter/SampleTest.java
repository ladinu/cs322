package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class SampleTest extends BaseTest {
   @Test
   public void main() {
      String snippet = "" +
            "    var t = 1;\n" +
            "    var i = 1;\n" +
            "    while ((i < 11)) {\n" +
            "      t = (t * i);\n" +
            "      i = (i + 1);\n" +
            "    }\n" +
            "    print t;\n";
      Assert.assertEquals(output("3628800"), run(snippet));
   }

   @Test
   public void mainDT() {
      String snippet = "" +
            " var i = (0 == 0);\n" +
            "    if (i) {\n" +
            "      i = (41 + 1);\n" +
            "    } else {\n" +
            "      print i;\n" +
            "    }\n" +
            "    print i;";
      Assert.assertEquals(output("42"), run(snippet));
   }

   @Test
   public void mainFCF() {
      String snippet = "" +
            "    procedure adder(n, ref f) {\n" +
            "      f = (\\m -> (n + m));\n" +
            "    }\n" +
            "    var double = (\\x -> (x + x));\n" +
            "    var inc = (\\x -> (x + 1));\n" +
            "    var comp = (\\f -> (\\g -> (\\x -> (f @ (g @ x)))));\n" +
            "    var toOdd = ((comp @ inc) @ double);\n" +
            "    print (toOdd @ 3);\n" +
            "    print (toOdd @ 9);\n" +
            "    adder(4, inc);\n" +
            "    print (inc @ 1);\n" +
            "    print (inc @ 0);\n";

      Assert.assertEquals(outputs("7 & 19 & 5 & 4"), run(snippet));
   }

   @Test
   public void mainNest() {
      String snippet = "" +
            "    var i = 0;\n" +
            "    if ((i == 0)) {\n" +
            "      var i = 1;\n" +
            "      print i;\n" +
            "    } else {\n" +
            "      i = 3;\n" +
            "    }\n" +
            "    print i;\n";
      Assert.assertEquals(outputs("1 & 0"), run(snippet));
   }

   @Test
   public void mainProc() {
      String snippet = "" +
            "    procedure gauss(limit) {\n" +
            "      var t = 0;\n" +
            "      var i = 0;\n" +
            "      while ((i < limit)) {\n" +
            "        i = (i + 1);\n" +
            "        t = (t + i);\n" +
            "      }\n" +
            "      print t;\n" +
            "    }\n" +
            "    procedure double(x) {\n" +
            "      print (x + x);\n" +
            "    }\n" +
            "    procedure sum(n, a) {\n" +
            "      if ((0 < n)) {\n" +
            "        sum((n - 1), (a + n));\n" +
            "      } else {\n" +
            "        print a;\n" +
            "      }\n" +
            "    }\n" +
            "    var n = 97207;\n" +
            "    gauss(5);\n" +
            "    double(21);\n" +
            "    gauss(10);\n" +
            "    sum(10, 0);\n" +
            "    print n;\n";
      Assert.assertEquals(outputs("15 & 42 & 55 & 55 & 97207"), run(snippet));
   }

   @Test
   public void mainRef() {
      String snippet = "" +
            "    procedure byref(ref x, ref y) {\n" +
            "      redirect(x);\n" +
            "      redirect(y);\n" +
            "      print (x + y);\n" +
            "    }\n" +
            "    procedure redirect(ref x) {\n" +
            "      inc(x);\n" +
            "    }\n" +
            "    procedure inc(ref y) {\n" +
            "      y = (y + 1);\n" +
            "    }\n" +
            "    var z = 25;\n" +
            "    byref(z, 23);\n" +
            "    print z;\n";
      Assert.assertEquals(outputs("50 & 26"), run(snippet));
   }
}

