package com.compiler;

import org.junit.Test;

public class AssignTest extends BaseTest{

  @Test
  public void testReturn() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl IntType i ()\n" +
        "   VarDecl IntType j ()\n" +
        "   Assign i 2\n" +
        "   Assign j i\n" +
        "   Print j\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "_main ()\n" +
        "(i, j)\n" +
        "{\n" +
        "Begin:\n" +
        " i = 2\n" +
        " j = i\n" +
        " call _printInt(j)\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
