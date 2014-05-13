package com.compiler;

import org.junit.Test;

public class GenericTest12 extends BaseTest{

  @Test
  public void testGeneric12() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType Body) b (NewObj Body ())\n" +
        "   Print (Call b go ())\n" +
        " ClassDecl Body \n" +
        "  MethodDecl IntType echo ((Param IntType i) )\n" +
        "   Return i\n" +
        "  MethodDecl IntType go ()\n" +
        "   Return (Call This echo (10 ))\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_Body (sz=16): _Body_echo, _Body_go\n" +
        "\n" +
        "_main ()\n" +
        "(b)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = call _malloc(8)\n" +
        " [t1]:P = _class_Body\n" +
        " b = t1\n" +
        " t2 = [b]:P\n" +
        " t3 = 8[t2]:P\n" +
        " t4 = call * t3(b)\n" +
        " call _printInt(t4)\n" +
        " return \n" +
        "End:\n" +
        "}\n" +
        "\n" +
        "_Body_echo (obj, i)\n" +
        "{\n" +
        "Begin:\n" +
        " return i\n" +
        "End:\n" +
        "}\n" +
        "\n" +
        "_Body_go (obj)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = [obj]:P\n" +
        " t2 = [t1]:P\n" +
        " t3 = call * t2(obj, 10)\n" +
        " return t3\n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
