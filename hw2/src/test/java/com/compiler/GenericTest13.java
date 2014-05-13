package com.compiler;

import org.junit.Test;

public class GenericTest13 extends BaseTest{

  @Test
  public void testGeneric13() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType B) b (NewObj B ())\n" +
        "   Assign (Field b i)  10\n" +
        "   Assign (Field b j)  20\n" +
        "   Print (Field b i) \n" +
        "   Print (Field b j) \n" +
        " ClassDecl A \n" +
        "  VarDecl IntType i ()\n" +
        " ClassDecl B A\n" +
        "  VarDecl IntType j ()\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_A (sz=0): \n" +
        "data _class_B (sz=0): \n" +
        "\n" +
        "_main ()\n" +
        "(b)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = call _malloc(16)\n" +
        " [t1]:P = _class_B\n" +
        " b = t1\n" +
        " 8[b]:I = 10\n" +
        " 12[b]:I = 20\n" +
        " t2 = 8[b]:I\n" +
        " call _printInt(t2)\n" +
        " t3 = 12[b]:I\n" +
        " call _printInt(t3)\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}