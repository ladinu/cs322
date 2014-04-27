package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class ScopeTest extends BaseTest {
   @Test
   public void caseScope0() {
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
   }

   @Test
   public void caseScope1() {
      String snippet = ""+
            "var l = cons(5, cons(2, []));" +
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
   public void caseScope2() {
      String snippet = ""+
            "var k = 42;" +
            "var l = cons(5, cons(2, []));" +
            "case l of" +
            "  [] ->" +
            "    var junk = 1;" +
            "  cons(h, k) ->" +
            "    k = 0;" +
            "end\n" +
            "print k;";
      Assert.assertEquals(output("42"), run(snippet));
   }

   @Test
   public void forloopScope0() {
      String snippet = "" +
            "var i = 2;" +
            "var l = cons(4, cons(2, []));" +
            "for (i in l) {" +
            "  var k =  i;" +
            "}" +
            "print i;";
      Assert.assertEquals(output("2"), run(snippet));
   }

   @Test
   public void forloopScope1() {
      String snippet = "" +
            "var i = 2;" +
            "var l = cons(4, cons(2, []));" +
            "for (i in l) {" +
            "  var k =  i;" +
            "}" +
            "print k;";
      expectExit(1, "ABORT: Variable k not defined", snippet);
   }

   @Test
   public void forloopScope2() {
      String snippet = "" +
            "var i = 2;" +
            "var l = cons(4, cons(0, []));" +
            "for (i in l) {" +
            "  var k =  i;" +
            "}" +
            "print i;";
      Assert.assertEquals(output("2"), run(snippet));
   }
}
