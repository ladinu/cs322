package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;
import org.junit.contrib.java.lang.system.Assertion;

public class CaseTest extends BaseTest {

   Stmt ifEmpty = new Print(new Int(1));
   Stmt ifNonEmpty = new Print(new Int(0));
   Program p = new Program(new Print(new Int(1)));

   @Test
   public void testExecIfEmpty() throws Exception {
      Case c = new Case(new Nil(), ifEmpty, "", "", ifNonEmpty);
      c.exec(p, getEmptyEnv());
      Assert.assertEquals("Output: 1\n", log.getLog());
   }

   @Test
   public void testExecIfNonEmpty() throws Exception {
      Cons cons = new Cons(new Int(1), new Nil());
      Case c = new Case(cons, ifEmpty, "", "", ifNonEmpty);

      c.exec(p, getEmptyEnv());
      Assert.assertEquals("Output: 0\n", log.getLog());
   }

   @Test
   public void testExecOtherExpr() throws Exception {
      Case c = new Case(new Int(1), ifEmpty, "", "", ifNonEmpty);
      exit.expectSystemExitWithStatus(1);
      exit.checkAssertionAfterwards(new Assertion() {
         @Override
         public void checkAssertion() throws Exception {
            Assert.assertEquals("ABORT: list value expected\n", log.getLog());
         }
      });
      c.exec(p, getEmptyEnv());
   }

   @Test
   public void testPrint() throws Exception {
      Case c = new Case(new Nil(), ifEmpty, "h", "t", ifNonEmpty);
      String expected = "case [] of\n  [] ->\n    " +
            "print 1;\n  cons(h, t) ->\n    print 0;\n";
      c.print(0);
      Assert.assertEquals(expected, log.getLog());

   }
}
