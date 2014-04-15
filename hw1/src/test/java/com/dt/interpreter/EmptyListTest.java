package com.dt.interpreter;

import org.junit.Test;
import org.junit.Assert;

public class EmptyListTest extends BaseTest {
    @Test
    public void testShow() throws Exception {
       Assert.assertEquals("print [];\n\n", showSnippet("print [];"));
    }
}
