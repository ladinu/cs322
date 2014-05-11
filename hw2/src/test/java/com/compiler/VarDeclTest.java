package com.compiler;

import org.junit.Test;

public class VarDeclTest extends BaseTest {

  @Test
  public void testVarDecl() throws Exception {
    String mj = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl BoolType b true\n" +
        "   VarDecl IntType i 2\n" +
        "   Print b\n" +
        "   Print i\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "_main ()\n" +
        "(b, i)\n" +
        "{\n" +
        "Begin:\n" +
        " b = true\n" +
        " i = 2\n" +
        " call _printBool(b)\n" +
        " call _printInt(i)\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(mj, ir);
  }
}
