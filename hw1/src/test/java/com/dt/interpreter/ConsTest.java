package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class ConsTest extends BaseTest {
   @Test
   public void testEval() throws Exception {
      Cons cons = new Cons(new Int(1), new Nil());
      Assert.assertTrue(cons.eval(getEmptyEnv()) instanceof NonEmptyList);
      Assert.assertEquals("[1]", cons.eval(getEmptyEnv()).show());
   }

   @Test
   public void testShow() throws Exception {
      Cons cons = new Cons(new Int(1), new Nil());
      Assert.assertEquals("cons (1, [])", cons.show());
   }

   @Test
   public void testCheckIfLValue() throws Exception {
      Cons cons = new Cons(new Int(1), new Nil());
      exit.expectSystemExitWithStatus(1);
      cons.checkIfLValue(getEmptyEnv(), new Int(4));
   }
}
