package com.compiler;

import org.junit.Test;

public class AssignTest extends BaseTest{

  @Test
  public void testAssign() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl IntType i ()\n" +
        "   VarDecl IntType j ()\n" +
        "   VarDecl (ObjType A) a (NewObj A ())\n" +
        "   Assign i 2\n" +
        "   Assign j i\n" +
        "   Print j\n" +
        "   Assign (Field a i)  10\n" +
        "   Print (Field a i)\n" +
        " ClassDecl A \n" +
        "  VarDecl IntType i ()";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_A (sz=0): \n" +
        "\n" +
        "_main ()\n" +
        "(a, i, j)\n" +
        "{\n" +
        "Begin:\n" +
        " i = 2\n" +
        " j = i\n" +
        " call _printInt(j)\n" +
        " t1 = call _malloc(12)\n" +
        " [t1]:P = _class_A\n" +
        " a = t1\n" +
        " 8[a]:I = 10\n" +
        " t2 = 8[a]:I\n" +
        " call _printInt(t2)" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
