package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class EmptyListTest {
    @Test
    public void testShow() throws Exception {
        Assert.assertEquals("[]", new EmptyList().show());
    }
}
