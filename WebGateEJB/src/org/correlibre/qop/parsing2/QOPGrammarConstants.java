/* Generated By:JavaCC: Do not edit this line. QOPGrammarConstants.java */
package org.correlibre.qop.parsing2;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface QOPGrammarConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int LP = 5;
  /** RegularExpression Id. */
  int RP = 6;
  /** RegularExpression Id. */
  int EQ = 7;
  /** RegularExpression Id. */
  int NEQ = 8;
  /** RegularExpression Id. */
  int LT = 9;
  /** RegularExpression Id. */
  int LTE = 10;
  /** RegularExpression Id. */
  int GT = 11;
  /** RegularExpression Id. */
  int GTE = 12;
  /** RegularExpression Id. */
  int AND = 13;
  /** RegularExpression Id. */
  int OR = 14;
  /** RegularExpression Id. */
  int PLUS = 15;
  /** RegularExpression Id. */
  int MINUS = 16;
  /** RegularExpression Id. */
  int TIMES = 17;
  /** RegularExpression Id. */
  int DIVIDE = 18;
  /** RegularExpression Id. */
  int QUOTED_STRING = 19;
  /** RegularExpression Id. */
  int KEYSTRING = 20;
  /** RegularExpression Id. */
  int NUMBER = 21;
  /** RegularExpression Id. */
  int BOOL = 22;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "\"\\n\"",
    "\"(\"",
    "\")\"",
    "\"=\"",
    "\"!=\"",
    "\"<\"",
    "\"<=\"",
    "\">\"",
    "\">=\"",
    "\"&&\"",
    "\"||\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "<QUOTED_STRING>",
    "<KEYSTRING>",
    "<NUMBER>",
    "<BOOL>",
  };

}