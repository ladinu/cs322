package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;


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
}
