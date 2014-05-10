package com.compiler;

import org.junit.Test;

public class Test0 extends BaseTest {

  @Test
  public void testGen() throws Exception {
    String mj = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   Print \"Hello World!\"\n";

    String ir = "" +
        "# IR Program" +
        "" +
        "_main ()" +
        "{" +
        "Begin:" +
        " call _printStr(\"Hello World!\")" +
        " return " +
        "End:" +
        "}";

    tst(mj, ir);
  }
}