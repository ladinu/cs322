package com.compiler;

import org.junit.Test;

public class GenericTest10 extends BaseTest{

  @Test
  public void testGeneric10() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType Body) b (NewObj Body ())\n" +
        "   CallStmt b go ()\n" +
        " ClassDecl Body \n" +
        "  VarDecl IntType i ()\n" +
        "  MethodDecl void go ()\n" +
        "   Assign i 10\n" +
        "   Print i\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_Body (sz=8): _Body_go\n" +
        "\n" +
        "_main ()\n" +
        "(b)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = call _malloc(12)\n" +
        " [t1]:P = _class_Body\n" +
        " b = t1\n" +
        " t2 = [b]:P\n" +
        " t3 = [t2]:P\n" +
        " call * t3(b)\n" +
        " return \n" +
        "End:\n" +
        "}\n" +
        "\n" +
        "_Body_go (obj)\n" +
        "{\n" +
        "Begin:\n" +
        " 8[obj]:I = 10\n" +
        " t1 = 8[obj]:I\n" +
        " call _printInt(t1)\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
