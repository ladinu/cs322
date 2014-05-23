package com.compilers.hw3;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;


public class BaseTest {
  @Rule
  public final StandardOutputStreamLog log = new StandardOutputStreamLog();

  public void test(String ir, String expected) throws Exception {
    log.clear();
    X86Gen.gen(ir);
    Assert.assertEquals(log.getLog(), expected);
  }
}
