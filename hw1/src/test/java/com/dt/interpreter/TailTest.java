package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class TailTest extends BaseTest {
   @Test
   public void testEvalNonEmptyList() throws Exception {
      Cons cons = new Cons(new Int(2), new Nil());
      cons = new Cons(new Int(1), cons);
      Tail t = new Tail(cons);
      Assert.assertEquals("[2]", t.eval(getEmptyEnv()).show());
   }

   @Test
   public void testEvalEmptyList() throws Exception {
      Tail t = new Tail(new Nil());
      exit.expectSystemExitWithStatus(1);
      t.eval(getEmptyEnv());
   }

   @Test
   public void testEvalOtherExpr() throws Exception {
      Tail t = new Tail(new Int(4));
      exit.expectSystemExitWithStatus(1);
      t.eval(getEmptyEnv());
   }

   @Test
   public void testShow() throws Exception {
      Tail t = new Tail(new Nil());
      Assert.assertEquals("tail ([])", t.show());
   }
}
