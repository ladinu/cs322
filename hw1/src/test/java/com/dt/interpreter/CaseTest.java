package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class CaseTest extends BaseTest {
   @Test
   public void testExecIfEmpty() throws Exception {
      String snippet = "" +
            "case [] of" +
            "  [] ->" +
            "    print 1;" +
            "  cons(h, t) ->" +
            "    print 0;" +
            "end";
      Assert.assertEquals(output("1"), run(snippet));
   }

   @Test
   public void testExecIfNonEmpty() throws Exception {
      String snippet = "" +
            "case cons(1, []) of" +
            "  [] ->" +
            "    print 1;" +
            "  cons(h, t) ->" +
            "    print 0;" +
            "end";
      Assert.assertEquals(output("0"), run(snippet));
   }

   @Test
   public void testExecOtherExpr() throws Exception {
      String snippet = "" +
            "case (1+1) of" +
            "  [] ->" +
            "    print 1;" +
            "  cons(h, t) ->" +
            "    print 0;" +
            "end";
      expectExit(1, Errors.LIST_VALUE_EXPECTED, snippet);
   }

   @Test
   public void testPrint() throws Exception {
      String snippet = "" +
            "case [] of\n" +
            "  [] ->\n" +
            "    print 1;\n" +
            "  cons(h, t) ->\n" +
            "    print 0;\n" +
            "end\n" +
            "\n";
      Assert.assertEquals(snippet, showSnippet(snippet));
   }
}
