package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class TailTest extends BaseTest {
   @Test
   public void testEvalNonEmptyList() throws Exception {
      Assert.assertEquals(output("[2]"), run("print tail(cons(1, cons(2, [])));"));
   }

   @Test
   public void testEvalEmptyList() throws Exception {
      expectExit(1, Errors.NON_EMPTY_LIST_EXPECTED, "print tail([]);");
   }

   @Test
   public void testEvalOtherExpr() throws Exception {
      expectExit(1, Errors.LIST_VALUE_EXPECTED, "print tail(1);");
   }

   @Test
   public void testShow() throws Exception {
      Tail t = new Tail(new Nil());
      Assert.assertEquals("tail([])", t.show());
   }
}
