package com.compiler;

import com.compiler.ast.Ast;
import com.compiler.ast.astParser;
import org.junit.Assert;

import java.io.StringReader;

public class BaseTest {
  public void println(Object input) {
    System.out.println(input);
  }

  public void tst(String mjAst, String expectedIr) throws Exception {
    IRGen irGen = new IRGen();
    Assert.assertEquals("output did not match", expectedIr, irGen.generate(mjAst));
  }

  public Ast.Program getProg(String ast) throws Exception {
    return new astParser(new StringReader(ast)).Program();
  }
}
