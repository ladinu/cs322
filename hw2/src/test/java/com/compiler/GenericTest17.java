package com.compiler;

import org.junit.Test;

public class GenericTest17 extends BaseTest{

  @Test
  public void testGeneric17() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType B) b (NewObj B ())\n" +
        "   Assign (Field b i)  10\n" +
        "   Assign (Field b b)  true\n" +
        "   If (Field b b) \n" +
        "    CallStmt b go ()\n" +
        " ClassDecl A \n" +
        "  VarDecl IntType i ()\n" +
        "  VarDecl BoolType b ()\n" +
        " ClassDecl B A\n" +
        "  MethodDecl IntType go ()\n" +
        "   Print i\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_A (sz=0): \n" +
        "data _class_B (sz=8): _B_go\n" +
        "\n" +
        "_main ()\n" +
        "(b)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = call _malloc(13)\n" +
        " [t1]:P = _class_B\n" +
        " b = t1\n" +
        " 8[b]:I = 10\n" +
        " 12[b]:B = true\n" +
        " t2 = 12[b]:B\n" +
        " if t2 == false goto L0\n" +
        " t3 = [b]:P\n" +
        " t4 = [t3]:P\n" +
        " call * t4(b)\n" +
        "L0:\n" +
        " return \n" +
        "End:\n" +
        "}\n" +
        "\n" +
        "_B_go (obj)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = 8[obj]:I\n" +
        " call _printInt(t1)\n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}