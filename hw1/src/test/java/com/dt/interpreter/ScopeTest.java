package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class ScopeTest extends BaseTest {
   @Test
   public void caseScope() {
      String snippet = "" +
            "var l = cons(5, cons(2, []));" +
            "case l of" +
            "  [] ->" +
            "    var junk = 1;" +
            "  cons(h, t) ->" +
            "    var junk = 1;" +
            "end\n" +
            "print junk;";
      expectExit(1, "ABORT: Variable junk not defined", snippet);

      snippet = "var l = cons(5, cons(2, []));" +
            "case l of" +
            "  [] ->" +
            "    var junk = 1;" +
            "  cons(h, k) ->" +
            "    var junk = 1;" +
            "end\n" +
            "print k;";
      expectExit(1, "ABORT: Variable k not defined", snippet);
   }

   @Test
   public void forloopScope() {
      String snippet = "" +
            "var i = 2;" +
            "var l = cons(4, cons(2, []));" +
            "for (i in l) {" +
            "  var k =  i;" +
            "}" +
            "print i;";
      Assert.assertEquals(output("2"), run(snippet));

      snippet = "" +
            "var i = 2;" +
            "var l = cons(4, cons(2, []));" +
            "for (i in l) {" +
            "  var k =  i;" +
            "}" +
            "print k;";
      expectExit(1, "ABORT: Variable k not defined", snippet);
   }
}
