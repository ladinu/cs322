package com.compilers.hw3;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

import java.io.File;
import java.io.InputStream;

public class BaseTest {
  @Rule
  public final StandardOutputStreamLog log = new StandardOutputStreamLog();

  @Ignore
  public void testAll() throws Exception {
    for(int i = 1; i < 49; i++) {
      String irPath = String.format("/test%02d.ir", i);
      String sPath  = String.format("/test%02d.s.ref", i);
      testF(irPath, sPath);
    }
  }

  public void testF(String irPath, String sPath) throws Exception {
    test(r(irPath), r(sPath));
  }

  public void test(String ir, String expected) throws Exception {
    log.clear();
    X86Gen.gen(ir);
    Assert.assertEquals(expected, log.getLog());
  }

  public void justRun(int num) throws Exception {
    String irPath = String.format("/test%02d.ir", num);
    String sPath  = String.format("/test%02d.s.ref", num);
    String dPath  = String.format("/Users/ladinu/Desktop/out/test%02d.dot", num);
    String sPath2  = String.format("/Users/ladinu/Desktop/out/s/actual/test%02d.s", num);

    log.clear();
    X86Gen.gen(r(irPath));

    String irCode = log.getLog();
    String sCode = r(sPath);

    String dot = FileUtils.readFileToString(
        new File("/Users/ladinu/Desktop/test.dot"));

    if (!irCode.equals(sCode))
      dPath = dPath + ".err";

    FileUtils.writeStringToFile(new File(dPath), dot);
    FileUtils.writeStringToFile(new File(sPath2), irCode);
  }

  // Read a file and return its content as a string
  public String r(String path) throws Exception {
    InputStream is = getClass().getResourceAsStream(path);
    return org.apache.commons.io.IOUtils.toString(is);
  }
}
