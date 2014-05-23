package com.compilers.hw3;

import org.junit.Test;
import org.junit.Assert;

public class Test01 extends BaseTest {

  @Test
  public void test1() throws Exception {
    String ir = "" +
        "# IR Program\n" +
        "\n" +
        "_main ()\n" +
        "{\n" +
        "Begin:\n" +
        " call _printStr(\"123\")\n" +
        " call _printInt(123)\n" +
        " call _print()\n" +
        " call _printBool(true)\n" +
        " return \n" +
        "End:\n" +
        "}\n";

    String s = "" +
        "\t.text\n" +
        "    # _main ()\n" +
        "# Allocation map\n" +
        "\t.p2align 4,0x90\n" +
        "\t.globl _main\n" +
        "_main:\n" +
        "\tsubq $8,%rsp\n" +
        "    # 0.  Begin:\n" +
        "F0_Begin:\n" +
        "    # 1.   call _printStr(\"123\")\n" +
        "\tleaq _S0(%rip),%rdi\n" +
        "\tcall _printStr\n" +
        "    # 2.   call _printInt(123)\n" +
        "\tmovq $123,%rdi\n" +
        "\tcall _printInt\n" +
        "    # 3.   call _print()\n" +
        "\tcall _print\n" +
        "    # 4.   call _printBool(true)\n" +
        "\tmovq $1,%rdi\n" +
        "\tcall _printBool\n" +
        "    # 5.   return \n" +
        "\taddq $8,%rsp\n" +
        "\tret\n" +
        "    # 6.  End:\n" +
        "F0_End:\n" +
        "_S0:\n" +
        "\t.asciz \"123\"\n";

    test(ir, s);
    test(ir, s);
  }
}