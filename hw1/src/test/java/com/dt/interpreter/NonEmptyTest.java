package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class NonEmptyTest extends BaseTest {
   @Test
   public void testEvalEmpty() throws Exception {
      Assert.assertEquals(output("false"), run("print nonEmpty([]);"));
   }

   @Test
   public void testEvalNonEmpty() throws Exception {
      Assert.assertEquals(output("true"), run("print nonEmpty(cons(1, []));"));
   }

   @Test
   public void testEvalNonList() throws Exception {
      expectExit(1, Errors.LIST_VALUE_EXPECTED, "print nonEmpty(1+2);");
   }

   @Test
   public void isLValue() throws Exception {
      Assert.assertTrue(NonEmpty.isLValue(getEmptyEnv(), new Nil()));
      Assert.assertFalse(NonEmpty.isLValue(getEmptyEnv(), new Int(1)));
   }

   @Test
   public void testShow() throws Exception {
      Assert.assertEquals("print nonEmpty([]);\n\n", showSnippet("print nonEmpty([]);"));
   }
}
