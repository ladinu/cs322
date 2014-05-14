package com.compiler;

import org.junit.Test;

public class GenericTest15 extends BaseTest{

  @Test
  public void testGeneric15() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType B) b (NewObj B ())\n" +
        "   CallStmt b setval (10 20 )\n" +
        "   Print (Field b i) \n" +
        "   Print (Field b j) \n" +
        " ClassDecl A \n" +
        "  VarDecl IntType i ()\n" +
        " ClassDecl B A\n" +
        "  VarDecl IntType j ()\n" +
        "  MethodDecl void setval ((Param IntType u) (Param IntType v) )\n" +
        "   Assign i u\n" +
        "   Assign j v\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_A (sz=0): \n" +
        "data _class_B (sz=8): _B_setval\n" +
        "\n" +
        "_main ()\n" +
        "(b)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = call _malloc(16)\n" +
        " [t1]:P = _class_B\n" +
        " b = t1\n" +
        " t2 = [b]:P\n" +
        " t3 = [t2]:P\n" +
        " call * t3(b, 10, 20)\n" +
        " t4 = 8[b]:I\n" +
        " call _printInt(t4)\n" +
        " t5 = 12[b]:I\n" +
        " call _printInt(t5)\n" +
        " return \n" +
        "End:\n" +
        "}\n" +
        "\n" +
        "_B_setval (obj, u, v)\n" +
        "{\n" +
        "Begin:\n" +
        " 8[obj]:I = u\n" +
        " 12[obj]:I = v\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}