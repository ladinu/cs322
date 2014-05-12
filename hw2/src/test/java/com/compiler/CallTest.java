package com.compiler;

import org.junit.Test;

public class CallTest extends BaseTest{

  @Test
  public void testCall() throws Exception {
    String ast = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   VarDecl (ObjType Body) b (NewObj Body ())\n" +
        "   CallStmt b go ()\n" +
        " ClassDecl Body \n" +
        "  MethodDecl void go ()\n" +
        "   Print \"Go!\"\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "data _class_Body (sz=8): _Body_go\n" +
        "\n" +
        "_main ()\n" +
        "(b)\n" +
        "{\n" +
        "Begin:\n" +
        " t1 = call _malloc(8)\n" +
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
        " call _printStr(\"Go!\")\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    tst(ast, ir);
  }
}
