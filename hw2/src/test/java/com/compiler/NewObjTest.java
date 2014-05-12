package com.compiler;

import org.junit.Test;

public class NewObjTest extends BaseTest{

  @Test
  public void testNewObj() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType A) a (NewObj A ())\n" +
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
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
