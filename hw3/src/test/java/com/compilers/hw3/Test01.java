package com.compilers.hw3;

import org.junit.Test;

public class Test01 extends BaseTest {

  @Test
  public void test1() throws Exception {
    testF("/test04.ir", "/test03.s.ref");
  }
}