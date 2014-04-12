package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class HeadTest extends BaseTest {
   @Test
   public void testEvalNonEmptyList() throws Exception {
      Head h = new Head(new Cons(new Int(1), new Nil()));
      Assert.assertEquals(h.eval(getEmptyEnv()).show(), "1");
   }

   @Test
   public void testEvalEmptyList() throws Exception {
      Head h = new Head(new Nil());
      exit.expectSystemExitWithStatus(1);
      h.eval(getEmptyEnv());
   }

   @Test
   public void testEvalOtherExpr() throws Exception {
      Head h = new Head(new Int(4));
      exit.expectSystemExitWithStatus(1);
      h.eval(getEmptyEnv());
   }

   @Test
   public void testShow() throws Exception {
      Head h = new Head(new Nil());
      Assert.assertEquals("head ([])", h.show());
   }
}
