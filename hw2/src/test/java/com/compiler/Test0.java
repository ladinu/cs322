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
        "# IR Program\n" +
        "\n" +
        "_main ()\n" +
        "{\n" +
        "Begin:\n" +
        " call _printStr(\"Hello World!\")\n" +
        " return \n" +
        "End:\n" +
        "}\n";
    tst(mj, ir);
  }
}