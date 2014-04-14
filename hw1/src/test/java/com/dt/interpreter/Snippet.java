package com.dt.interpreter;

import java.io.StringReader;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

public class Snippet {
   public static final StandardOutputStreamLog log = new StandardOutputStreamLog();

   public static String run(String snippet) {
      Parser p = new Parser(new StringReader(snippet));
      try {
         Program prog = p.prog();
         prog.run();
         return log.getLog();
      } catch (ParseException e) {
         return "ERROR: " + e.toString();
      }
   }

   public static String show(String snippet) {
      Parser p = new Parser(new StringReader(snippet));
      try {
         Program prog = p.prog();
         prog.print();
         return log.getLog();
      } catch (ParseException e) {
         return "ERROR: " + e.toString();
      }
   }
}
