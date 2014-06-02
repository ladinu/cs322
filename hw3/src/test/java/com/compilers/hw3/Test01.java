package com.compilers.hw3;

import org.junit.Test;

public class Test01 extends BaseTest {

  @Test
  public void test1() throws Exception {
//    int num = 3;
//    String ir = String.format("/test%02d.ir", num);
//    String s = String.format("/test%02d.s.ref", num);
//    testF(ir, s);
    for(int i = 1; i < 49; i++) {
      justRun(i);
    }
  }
}