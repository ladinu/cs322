/* Generated By:JavaCC: Do not edit this line. irParserConstants.java */
package com.compilers.hw3;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface irParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int COMMENT = 5;
  /** RegularExpression Id. */
  int LINENUM = 6;
  /** RegularExpression Id. */
  int kwData = 7;
  /** RegularExpression Id. */
  int kwCall = 8;
  /** RegularExpression Id. */
  int kwGoto = 9;
  /** RegularExpression Id. */
  int kwIf = 10;
  /** RegularExpression Id. */
  int kwReturn = 11;
  /** RegularExpression Id. */
  int kwSize = 12;
  /** RegularExpression Id. */
  int Digit = 13;
  /** RegularExpression Id. */
  int Letter = 14;
  /** RegularExpression Id. */
  int IntLit = 15;
  /** RegularExpression Id. */
  int BoolLit = 16;
  /** RegularExpression Id. */
  int StrLit = 17;
  /** RegularExpression Id. */
  int IntType = 18;
  /** RegularExpression Id. */
  int BoolType = 19;
  /** RegularExpression Id. */
  int PtrType = 20;
  /** RegularExpression Id. */
  int Temp = 21;
  /** RegularExpression Id. */
  int Id = 22;
  /** RegularExpression Id. */
  int Global = 23;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\r\"",
    "\"\\f\"",
    "<COMMENT>",
    "<LINENUM>",
    "\"data\"",
    "\"call\"",
    "\"goto\"",
    "\"if\"",
    "\"return\"",
    "\"sz=\"",
    "<Digit>",
    "<Letter>",
    "<IntLit>",
    "<BoolLit>",
    "<StrLit>",
    "\":I\"",
    "\":B\"",
    "\":P\"",
    "<Temp>",
    "<Id>",
    "<Global>",
    "\"\\n\"",
    "\"(\"",
    "\")\"",
    "\":\"",
    "\",\"",
    "\"{\"",
    "\"}\"",
    "\"=\"",
    "\"*\"",
    "\"+\"",
    "\"-\"",
    "\"/\"",
    "\"&&\"",
    "\"||\"",
    "\"==\"",
    "\"!=\"",
    "\"<\"",
    "\"<=\"",
    "\">\"",
    "\">=\"",
    "\"!\"",
    "\"[\"",
    "\"]\"",
  };

}
