package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class ForTest extends BaseTest {
   @Test
   public void testExec() throws Exception {
      String code = "" +
            "var list = cons(1, cons(2, cons(3, [])));" +
            "for (i in list) {" +
            "  print i * 2;" +
            "}";
      System.out.println(Snippet.run(code));
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
