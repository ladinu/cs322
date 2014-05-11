package com.compiler;

import org.junit.Test;

public class PrintTest extends BaseTest {

  @Test
  public void testStrlit() throws Exception {
    String mj = "" +
        "# AST Program\n" +
        " ClassDecl Test \n" +
        "  MethodDecl void main ()\n" +
        "   Print \"Hello World!\"\n" +
        "   Print 1\n" +
        "   Print true\n" +
        "   Print false\n";

    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "_main ()\n" +
        "{\n" +
        "Begin:\n" +
        " call _printStr(\"Hello World!\")\n" +
        " call _printInt(1)\n" +
        " call _printBool(true)\n" +
        " call _printBool(false)\n" +
        " return \n" +
        "End:\n" +
        "}\n";
    tst(mj, ir);
  }
}