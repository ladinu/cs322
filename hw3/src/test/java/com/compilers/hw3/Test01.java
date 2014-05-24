package com.compilers.hw3;

import org.junit.Test;

public class Test01 extends BaseTest {

  @Test
  public void test1() throws Exception {
    testF("/test01.ir", "/mac/test01.s.ref");
  }
}