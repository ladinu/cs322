package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class NonEmptyListTest {
    @Test
    public void testShow() throws Exception {
        LValue l0 = new EmptyList();
        LValue l1 = new NonEmptyList(new IValue(42), l0);
        LValue l2 = new NonEmptyList(new BValue(true), l1);
        LValue l3 = new NonEmptyList(new FValue(null, "x", new Var("x")), l2);
        LValue l4 = l0;
        for (int i = 10; i > 0; i--) {
            l4 = new NonEmptyList(new IValue(i), l4);
        }

        Assert.assertEquals("[42]", l1.show());
        Assert.assertEquals("[true, 42]", l2.show());
        Assert.assertEquals("[<function>, true, 42]", l3.show());
        Assert.assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", l4.show());
    }

   @Test
   public void testGetHead() throws Exception {
      NonEmptyList list = new NonEmptyList(new IValue(1), new EmptyList());
      Assert.assertEquals("1", list.getHead().show());
   }

   @Test
   public void testGetTail() throws Exception {
      NonEmptyList list = new NonEmptyList(new IValue(1), new EmptyList());
      Assert.assertEquals("[]", list.getTail().show());
   }

   @Test
   public void testGetArrayList() throws Exception {
      Value ival = new IValue(1);
      Value bval = new BValue(true);
      NonEmptyList list = new NonEmptyList(ival, new EmptyList());
      list = new NonEmptyList(bval, list);

      ArrayList<Value> expected = new ArrayList<Value>();
      expected.add(bval);
      expected.add(ival);

      ArrayList<Value> actual = list.getArrayList();
      Assert.assertTrue(expected.equals(list.getArrayList()));
   }

   @Test
   public void testIsNonEmptyList() {
      NonEmptyList l = getList();
      Assert.assertTrue("expecting to be non empty list", l.isNonEmptyList());
   }

   @Test
   public void testIsEmptyList() {
      NonEmptyList l = getList();
      Assert.assertFalse("expecting to be non empty list", l.isEmptyList());
   }

   @Test
   public void testGetNonEmptyList() {
      NonEmptyList l = getList();
      Assert.assertTrue(l.getNonEmptyList() == l);
   }

   private NonEmptyList getList() {
      return new NonEmptyList(new IValue(1), new EmptyList());
   }
}
