package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class HeadTest extends BaseTest {
   @Test
   public void testEvalNonEmptyList() throws Exception {
      Assert.assertEquals(output("1"), run("print head(cons(1, []));"));
   }

   @Test
   public void testEvalEmptyList() throws Exception {
      expectExit(1, Errors.NON_EMPTY_LIST_EXPECTED, "print head([]);");
   }

   @Test
   public void testEvalOtherExpr() throws Exception {
      expectExit(1, Errors.LIST_VALUE_EXPECTED, "print head(1);");
   }

   @Test
   public void testShow() throws Exception {
      Assert.assertEquals("print head(cons(1, []));\n\n", showSnippet("print head(cons(1, []));"));
   }
}
