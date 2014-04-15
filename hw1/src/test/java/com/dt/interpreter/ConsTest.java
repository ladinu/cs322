package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class ConsTest extends BaseTest {
   @Test
   public void testEval() throws Exception {
      Assert.assertEquals(output("[1]"), run("print cons(1, []);"));
   }

   @Test
   public void testShow() throws Exception {
      Assert.assertEquals("print cons(1, []);\n\n", showSnippet("print cons(1, []);"));
   }

   @Test
   public void testCheckIfLValue() throws Exception {
      expectExit(1, Errors.LIST_VALUE_EXPECTED, "print cons(1, 4);");
   }
}
