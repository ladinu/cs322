package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class ForTest extends BaseTest {
   @Test
   public void testExec() throws Exception {
      String code = "" +
            "var sum = 0;" +
            "var list = cons(1, cons(2, cons(3, [])));" +
            "for (i in list) {" +
            "  sum = sum + i;" +
            "}" +
            "print sum;";
      Assert.assertEquals(output("6"), run(code));
   }

   @Test
   public void empyList() {
      String snippet = "" +
            "var sum = 0;" +
            "var l = cons(1, []);" +
            "var list = cons(l, cons(l, cons(l, [])));" +
            "for (i in list) {" +
            "  for (j in i) {" +
            "     sum = sum + j;" +
            "  }" +
            "}" +
            "print sum;";
      Assert.assertEquals(output("3"), run(snippet));
   }

   @Test
   public void testPrint() throws Exception {
      For f = new For("var", new Nil(), new Print(new Int(1)));
      String expected = "" +
            "for (var in []) {\n" +
            "  print 1;\n" +
            "}\n";
      f.print(0);
      Assert.assertEquals(expected, log.getLog());
   }
}
