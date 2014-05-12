package com.compiler;

import org.junit.Test;

public class GenericTest07 extends BaseTest{

  @Test
  public void testGeneric07() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType A) a (NewObj A ())\n" +
        "   Assign (Field a i)  10\n" +
        "   Print (Field a i) \n" +
        " ClassDecl A \n" +
        "  VarDecl IntType i ()\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_A (sz=0): \n" +
        "\n" +
        "_main ()\n" +
        "(a)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = call _malloc(12)\n" +
        " [t1]:P = _class_A\n" +
        " a = t1\n" +
        " 8[a]:I = 10\n" +
        " t2 = 8[a]:I\n" +
        " call _printInt(t2)\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
