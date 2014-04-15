package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

public class NilTest extends BaseTest {
   @Test
   public void testEval() throws Exception {
      Assert.assertEquals(output("[]"), run("print [];"));
   }

   @Test
   public void testShow() throws Exception {
      Assert.assertEquals("print [];\n\n", showSnippet("print [];"));
   }

}
