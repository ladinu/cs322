package com.compiler;

import org.junit.Assert;

public class BaseTest {
  public void println(Object input) {
    System.out.println(input);
  }

  public void tst(String mjAst, String expectedIr) throws Exception {
    IRGen irGen = new IRGen();
    Assert.assertEquals(irGen.generate(mjAst), expectedIr);
  }
}
