package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringReader;


public class ReverseTest extends BaseTest{
   private String length = "" +
         "procedure getlength(list, ref l) {" +
         "  var length = 0;" +
         "  for (i in list) {" +
         "     length = length + 1;" +
         "  }" +
         "  l = length;" +
         "}";

   //      Parser p = new Parser(new StringReader(snippet));
//      try {
//         p.prog().run();
//      } catch (Exception e) {
//         System.out.println(e.toString());
//      }
   @Test
   public void reverse() {
      String snippet = "" +
            "procedure reverse(list, ref out) {" +
            "  for (i in list) {" +
            "     out = cons(i, out);" +
            "  }" +
            "}" +
            "var l = cons(1, cons(2, cons(3, [])));" +
            "var o = [];" +
            "reverse(l, o);" +
            "print o;";
       Assert.assertEquals(output("[3, 2, 1]"), run(snippet));
   }

   @Test
   public void count() {
      String snippet = "" +
            "procedure count(val, list, ref num) {" +
            "  num = 0;" +
            "  for (i in list) {" +
            "     if (i == val) {" +
            "        num = num + 1;" +
            "     } else { var junk = 0;}" +
            "  }" +
            "}" +
            "" +
            "var n = 0;" +
            "count(4, cons(4, cons(4, cons(4, cons(1, cons(2, []))))), n);" +
            "print n;";
      Assert.assertEquals(output("3"), run(snippet));
   }

   @Test
   public void position() {
      String snippet = "" +
            "procedure position(val, list, ref pos) {" +
            "  pos = 0;" +
            "  var notFound = (1==1);" +
            "  var index = 1;" +
            "  for (i in list) {" +
            "     if (notFound) {" +
            "        if (i == val) {" +
            "           pos = index;" +
            "           notFound = (1==0);" +
            "        } else {" +
            "           index = index + 1;" +
            "        }" +
            "     } else {" +
            "        var junk = 0;" +
            "     }" +
            "  }" +
            "}" +
            "" +
            "var pos = 10;" +
            "position(3, cons(1, cons(3, cons(2, []))), pos);" +
            "print pos;";

      Assert.assertEquals(output("2"), run(snippet));
   }
}
