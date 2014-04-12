package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class NilTest extends BaseTest {
   @Test
   public void testEval() throws Exception {
      Nil nil = new Nil();
      Value val = nil.eval(getEmptyEnv());
      Assert.assertTrue(val instanceof EmptyList);
   }

   @Test
   public void testShow() throws Exception {
      Assert.assertEquals("[]", new Nil().show());
   }

}
