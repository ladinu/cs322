package com.compiler;

import org.junit.Test;

public class GenericTest09 extends BaseTest{

  @Test
  public void testGeneric09() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType Body) b (NewObj Body ())\n" +
        "   VarDecl IntType i (Call b go (1 2 3 ))\n" +
        "   Print i\n" +
        " ClassDecl Body \n" +
        "  MethodDecl IntType go ((Param IntType i) (Param IntType j) (Param IntType k) )\n" +
        "   Return k\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_Body (sz=8): _Body_go\n" +
        "\n" +
        "_main ()\n" +
        "(b, i)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = call _malloc(8)\n" +
        " [t1]:P = _class_Body\n" +
        " b = t1\n" +
        " t2 = [b]:P\n" +
        " t3 = [t2]:P\n" +
        " t4 = call * t3(b, 1, 2, 3)\n" +
        " i = t4\n" +
        " call _printInt(i)\n" +
        " return \n" +
        "End:\n" +
        "}\n" +
        "\n" +
        "_Body_go (obj, i, j, k)\n" +
        "{\n" +
        "Begin:\n" +
        " return k\n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
