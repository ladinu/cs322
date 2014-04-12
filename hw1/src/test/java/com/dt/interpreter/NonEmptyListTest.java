package com.dt.interpreter;

import junit.framework.Assert;
import org.junit.Test;

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
}
