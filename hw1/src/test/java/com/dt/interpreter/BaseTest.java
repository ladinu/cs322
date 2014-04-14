package com.dt.interpreter;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

public abstract class BaseTest {
   @Rule
   public final ExpectedSystemExit exit = ExpectedSystemExit.none();

   @Rule
   public final StandardOutputStreamLog log = new StandardOutputStreamLog();

   public Env getEmptyEnv() {
      return  new ValEnv("null", null, null);
   }
}

