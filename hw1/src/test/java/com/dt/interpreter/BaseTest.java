package com.dt.interpreter;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.Assertion;
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

   // Helper method to format expected output
   public String output(String expectedOutput) {
      return "Output: " + expectedOutput + "\n";
   }

   // Given a DT snippet, run and get the output
   public String run(String snippet) {
      log.clear();
      Snippet.run(snippet);
      return log.getLog();
   }

   public void expectExit(Integer exitStatus, final Errors expectedMsg, String snippet) {
      exit.expectSystemExitWithStatus(exitStatus);
      exit.checkAssertionAfterwards(new Assertion() {
         @Override
         public void checkAssertion() throws Exception {
            Assert.assertEquals(expectedMsg.toString()+"\n", log.getLog());
         }
      });
      Snippet.run(snippet);
   }

   public String showSnippet(String snippet) {
      log.clear();
      Snippet.show(snippet);
      return log.getLog();
   }
}

