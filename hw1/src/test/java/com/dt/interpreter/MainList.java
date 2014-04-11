package com.dt.interpreter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MainList extends TestCase {
    public MainList(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(MainList.class);
    }

    public void testEmptyList() {
        assertEquals(new EmptyList().show(), "[]");
    }

    public void testNonEmptyList() {
        LValue l0 = new EmptyList();
        LValue l1 = new NonEmptyList(new IValue(42), l0);
        LValue l2 = new NonEmptyList(new BValue(true), l1);
        LValue l3 = new NonEmptyList(new FValue(null, "x", new Var("x")), l2);
        LValue l4 = l0;
        for (int i = 10; i > 0; i--) {
            l4 = new NonEmptyList(new IValue(i), l4);
        }

        assertEquals("[42]", l1.show());
        assertEquals("[true, 42]", l2.show());
        assertEquals("[<function>, true, 42]", l3.show());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", l4.show());
    }
}
