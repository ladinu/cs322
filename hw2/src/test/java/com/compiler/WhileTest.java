package com.compiler;

import org.junit.Test;

public class WhileTest extends BaseTest{

  @Test
  public void testWhile() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl BoolType flag true\n" +
        "   While flag\n" +
        "    Assign flag false\n" +
        "   Print \"done\"\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "_main ()\n" +
        "(flag)\n" +
        "{\n" +
        "Begin:\n" +
        " flag = true\n" +
        "L0:\n" +
        " if flag == false goto L1\n" +
        " flag = false\n" +
        " goto L0\n" +
        "L1:\n" +
        " call _printStr(\"done\")\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
