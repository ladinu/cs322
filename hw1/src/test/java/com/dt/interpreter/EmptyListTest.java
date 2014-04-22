package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;
import org.junit.contrib.java.lang.system.Assertion;

public class EmptyListTest extends BaseTest {
    @Test
    public void testShow() throws Exception {
       Assert.assertEquals("print [];\n\n", showSnippet("print [];"));
    }

   @Test
   public void testAsList() {
      EmptyList l = getList();
      Assert.assertTrue(l.asList() == l);
   }

   @Test
   public void testIsNonEmptyList() {
      Assert.assertFalse(getList().isNonEmptyList());
   }

   @Test
   public void testIsEmptyList() {
      Assert.assertTrue(getList().isEmptyList());
   }

   @Test
   public void testGetNonEmptyList() {
      log.clear();
      exit.expectSystemExitWithStatus(1);
      exit.checkAssertionAfterwards(new Assertion() {
         @Override
         public void checkAssertion() throws Exception {
            Assert.assertEquals(Errors.NON_EMPTY_LIST_EXPECTED+"\n", log.getLog());
         }
      });
      getList().getNonEmptyList();
   }

   private EmptyList getList() {
      return new EmptyList();
   }
}
