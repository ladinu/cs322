package com.compiler;

import org.junit.Test;

public class ReturnTest extends BaseTest{

  @Test
  public void testReturn() throws Exception {
    String mj = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   Print ()" +
        " ClassDecl Body \n" +
        "  MethodDecl IntType go ((Param IntType i) (Param IntType j) (Param IntType k) )" +
        "   Return 1\n" +
        "  MethodDecl BoolType g1 ((Param IntType i) (Param IntType j) (Param IntType k) )" +
        "   Return false\n" +
        "  MethodDecl BoolType g2 ((Param IntType i) (Param IntType j) (Param IntType k) )" +
        "   Return true\n" +
        "  MethodDecl IntType g3 ((Param IntType i) (Param IntType j) (Param IntType k) )" +
        "   Return 1\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_Body (sz=8): _Body_go\n" +
        "\n" +
        "_main ()\n" +
        "{\n" +
        "Begin:\n" +
        " call _print()\n" +
        " return \n" +
        "End:\n" +
        "}\n" +
        "\n" +
        "" +
        "_Body_go (obj, i, j, k)\n" +
        "{\n" +
        "Begin:\n" +
        " return 1\n" +
        "End:\n" +
        "}\n";
    tst(mj, ir);
  }
}
