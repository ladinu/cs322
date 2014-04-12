package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class NonEmptyTest extends BaseTest {
   @Test
   public void testEvalEmpty() throws Exception {
      NonEmpty nonEmpty = new NonEmpty(new Nil());
      Value val = nonEmpty.eval(getEmptyEnv());

      Assert.assertTrue(val instanceof BValue);
      Assert.assertFalse(val.asBool());
   }

   @Test
   public void testEvalNonEmpty() throws Exception {
      Cons nonEmptyList = new Cons(new Int(1), new Nil());
      NonEmpty nonEmpty = new NonEmpty(nonEmptyList);
      Value val = nonEmpty.eval(getEmptyEnv());

      Assert.assertTrue(val instanceof BValue);
      Assert.assertTrue(val.asBool());
   }

   @Test
   public void testEvalNonList() throws Exception {
      exit.expectSystemExitWithStatus(1);
      NonEmpty nonEmpty = new NonEmpty(new Int(3));
      nonEmpty.eval(getEmptyEnv());
   }

   @Test
   public void isLValue() throws Exception {
      Assert.assertTrue(NonEmpty.isLValue(getEmptyEnv(), new Nil()));
      Assert.assertFalse(NonEmpty.isLValue(getEmptyEnv(), new Int(1)));
   }

   @Test
   public void testShow() throws Exception {
      NonEmpty nonEmpty = new NonEmpty(new Nil());
      Assert.assertEquals("nonEmpty ([])", nonEmpty.show());
   }
}
