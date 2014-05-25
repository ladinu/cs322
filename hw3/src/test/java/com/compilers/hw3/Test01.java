package com.compilers.hw3;

import org.junit.Test;

public class Test01 extends BaseTest {

  @Test
  public void test1() throws Exception {
    testF("/example0.ir", "/test02.s.ref");
  }
}