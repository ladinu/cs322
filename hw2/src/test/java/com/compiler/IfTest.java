package com.compiler;

import org.junit.Test;

public class IfTest extends BaseTest{

  @Test
  public void testReturn() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   If true\n" +
        "    Print 1\n" +
        "   Else\n" +
        "    Print 2\n" +
        "   If true\n" +
        "     Print 1\n" +
        "   Print 1\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "_main ()\n" +
        "{\n" +
        "Begin:\n" +
        " if true == false goto L0\n" +
        " call _printInt(1)\n" +
        " goto L1\n" +
        "L0:\n" +
        " call _printInt(2)\n" +
        "L1:\n" +
        " if true == false goto L2\n" +
        " call _printInt(1)\n" +
        "L2:\n" +
        " call _printInt(1)\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
