//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";

//#line 2 "grammer.y"
package Sintactico;

import Lexico.AnalizadorLexico;
import java.util.Scanner;
import Tools.Logger;
import java.util.ArrayList;
import java.io.File;
import Tools.TablaSimbolos;
//#line 25 "Parser.java"

public class Parser {

  boolean yydebug; // do I want debug output?
  int yynerrs; // number of errors so far
  int yyerrflag; // was there an error?
  int yychar; // the current working character

  // ########## MESSAGES ##########
  // ###############################################################
  // method: debug
  // ###############################################################
  void debug(String msg) {
    if (yydebug)
      System.out.println(msg);
  }

  // ########## STATE STACK ##########
  final static int YYSTACKSIZE = 500; // maximum stack size
  int statestk[] = new int[YYSTACKSIZE]; // state stack
  int stateptr;
  int stateptrmax; // highest index of stackptr
  int statemax; // state when highest index reached
  // ###############################################################
  // methods: state stack push,pop,drop,peek
  // ###############################################################

  final void state_push(int state) {
    try {
      stateptr++;
      statestk[stateptr] = state;
    } catch (ArrayIndexOutOfBoundsException e) {
      int oldsize = statestk.length;
      int newsize = oldsize * 2;
      int[] newstack = new int[newsize];
      System.arraycopy(statestk, 0, newstack, 0, oldsize);
      statestk = newstack;
      statestk[stateptr] = state;
    }
  }

  final int state_pop() {
    return statestk[stateptr--];
  }

  final void state_drop(int cnt) {
    stateptr -= cnt;
  }

  final int state_peek(int relative) {
    return statestk[stateptr - relative];
  }

  // ###############################################################
  // method: init_stacks : allocate and prepare stacks
  // ###############################################################
  final boolean init_stacks() {
    stateptr = -1;
    val_init();
    return true;
  }

  // ###############################################################
  // method: dump_stacks : show n levels of the stacks
  // ###############################################################
  void dump_stacks(int count) {
    int i;
    System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
    for (i = 0; i < count; i++)
      System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
    System.out.println("======================");
  }

  // ########## SEMANTIC VALUES ##########
  // public class ParserVal is defined in ParserVal.java

  String yytext;// user variable to return contextual strings
  ParserVal yyval; // used to return semantic vals from action routines
  ParserVal yylval;// the 'lval' (result) I got from yylex()
  ParserVal valstk[];
  int valptr;

  // ###############################################################
  // methods: value stack push,pop,drop,peek.
  // ###############################################################
  void val_init() {
    valstk = new ParserVal[YYSTACKSIZE];
    yyval = new ParserVal();
    yylval = new ParserVal();
    valptr = -1;
  }

  void val_push(ParserVal val) {
    if (valptr >= YYSTACKSIZE)
      return;
    valstk[++valptr] = val;
  }

  ParserVal val_pop() {
    if (valptr < 0)
      return new ParserVal();
    return valstk[valptr--];
  }

  void val_drop(int cnt) {
    int ptr;
    ptr = valptr - cnt;
    if (ptr < 0)
      return;
    valptr = ptr;
  }

  ParserVal val_peek(int relative) {
    int ptr;
    ptr = valptr - relative;
    if (ptr < 0)
      return new ParserVal();
    return valstk[ptr];
  }

  final ParserVal dup_yyval(ParserVal val) {
    ParserVal dup = new ParserVal();
    dup.ival = val.ival;
    dup.dval = val.dval;
    dup.sval = val.sval;
    dup.obj = val.obj;
    return dup;
  }

  // #### end semantic value section ####
  public final static short CLASS = 257;
  public final static short INTERFACE = 258;
  public final static short IMPLEMENT = 259;
  public final static short RETURN = 260;
  public final static short IF = 261;
  public final static short ELSE = 262;
  public final static short END_IF = 263;
  public final static short FOR = 264;
  public final static short IN = 265;
  public final static short RANGE = 266;
  public final static short IMPL = 267;
  public final static short PRINT = 268;
  public final static short TOD = 269;
  public final static short EQUAL_OPERATOR = 270;
  public final static short NOT_EQUAL_OPERATOR = 271;
  public final static short GREATER_THAN_OR_EQUAL_OPERATOR = 272;
  public final static short LESS_THAN_OR_EQUAL_OPERATOR = 273;
  public final static short MINUS_ASSIGN = 274;
  public final static short VOID = 275;
  public final static short LONG = 276;
  public final static short UINT = 277;
  public final static short DOUBLE = 278;
  public final static short CADENA = 279;
  public final static short ID = 280;
  public final static short CTE_DOUBLE = 281;
  public final static short CTE_UINT = 282;
  public final static short CTE_LONG = 283;
  public final static short YYERRCODE = 256;
  final static short yylhs[] = { -1,
      0, 1, 1, 2, 2, 2, 2, 3, 3, 7,
      9, 9, 10, 11, 11, 12, 12, 15, 15, 16,
      16, 17, 18, 13, 20, 22, 23, 23, 21, 21,
      24, 26, 8, 27, 27, 4, 29, 29, 30, 30,
      31, 5, 5, 33, 34, 34, 35, 36, 37, 38,
      39, 41, 42, 42, 42, 43, 43, 43, 43, 43,
      19, 44, 44, 44, 45, 45, 45, 45, 45, 46,
      46, 47, 47, 48, 48, 48, 40, 40, 49, 49,
      14, 14, 50, 32, 51, 51, 52, 52, 53, 28,
      25, 25, 25, 25, 55, 55, 54, 54, 56, 56,
      57, 57, 58, 58, 58, 58, 58, 58, 59, 66,
      6, 6, 6, 6, 6, 6, 67, 67, 67, 64,
      68, 68, 65, 60, 60, 61, 61, 62, 62, 69,
      70, 72, 71, 63, 63,
  };
  final static short yylen[] = { 2,
      1, 1, 2, 1, 1, 1, 1, 3, 4, 3,
      1, 2, 1, 1, 1, 3, 2, 1, 3, 1,
      3, 1, 1, 2, 2, 1, 4, 3, 1, 1,
      2, 1, 2, 1, 3, 3, 3, 3, 1, 2,
      2, 5, 6, 3, 1, 2, 1, 2, 1, 3,
      1, 1, 1, 3, 3, 1, 3, 3, 3, 3,
      1, 1, 3, 3, 1, 4, 3, 3, 3, 1,
      1, 1, 3, 1, 1, 1, 1, 1, 5, 4,
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      5, 3, 4, 2, 3, 2, 1, 2, 1, 2,
      1, 1, 1, 1, 1, 1, 1, 1, 2, 2,
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 7, 7, 9, 9, 11, 11, 1,
      1, 1, 1, 3, 1,
  };
  final static short yydefred[] = { 0,
      0, 0, 0, 0, 0, 0, 26, 0, 0, 123,
      0, 0, 2, 4, 5, 6, 7, 115, 0, 0,
      117, 121, 0, 122, 112, 113, 114, 116, 119, 118,
      111, 120, 0, 0, 0, 130, 0, 0, 0, 0,
      0, 88, 87, 89, 0, 94, 102, 0, 82, 81,
      83, 85, 86, 0, 97, 101, 0, 3, 30, 24,
      29, 0, 25, 78, 77, 0, 0, 0, 8, 0,
      0, 36, 0, 71, 74, 75, 76, 0, 0, 0,
      0, 0, 0, 65, 70, 72, 0, 84, 0, 134,
      0, 32, 0, 0, 0, 22, 0, 18, 0, 0,
      92, 98, 109, 0, 50, 90, 0, 34, 0, 0,
      11, 13, 14, 15, 0, 9, 0, 0, 0, 39,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 80, 0, 93, 0,
      0, 0, 28, 0, 0, 0, 17, 10, 12, 0,
      38, 41, 37, 40, 0, 73, 0, 0, 0, 103,
      104, 105, 106, 107, 108, 0, 0, 0, 0, 0,
      0, 0, 0, 67, 68, 69, 0, 0, 0, 42,
      79, 19, 21, 23, 91, 31, 27, 35, 16, 66,
      96, 0, 99, 0, 0, 0, 0, 131, 0, 43,
      0, 0, 45, 47, 95, 100, 0, 124, 0, 125,
      133, 0, 49, 48, 44, 46, 0, 0, 132, 0,
      126, 127, 0, 0, 0, 128, 129,
  };
  final static short yydgoto[] = { 11,
      12, 13, 14, 15, 16, 17, 69, 70, 110, 111,
      112, 113, 18, 48, 97, 98, 99, 183, 92, 19,
      60, 20, 63, 145, 21, 93, 107, 108, 72, 119,
      120, 49, 180, 202, 203, 204, 214, 22, 23, 66,
      79, 80, 81, 82, 83, 84, 85, 86, 24, 50,
      51, 52, 53, 54, 158, 192, 55, 159, 56, 160,
      161, 162, 163, 164, 165, 57, 31, 32, 37, 199,
      212, 220,
  };
  final static short yysindex[] = { 216,
      -232, -222, 24, -200, -176, -180, 0, 65, 294, 0,
      0, 216, 0, 0, 0, 0, 0, 0, -27, -158,
      0, 0, -51, 0, 0, 0, 0, 0, 0, 0,
      0, 0, -114, 19, -40, 0, -125, -124, 99, -33,
      114, 0, 0, 0, 65, 0, 0, -110, 0, 0,
      0, 0, 0, 336, 0, 0, 129, 0, 0, 0,
      0, 106, 0, 0, 0, -40, -103, -146, 0, 52,
      -120, 0, 138, 0, 0, 0, 0, -40, 139, -219,
      -56, 80, 102, 0, 0, 0, -87, 0, 124, 0,
      140, 0, 145, 80, 62, 0, 130, 0, 127, 146,
      0, 0, 0, 135, 0, 0, 132, 0, 148, -109,
      0, 0, 0, 0, -110, 0, 149, -158, -112, 0,
      -29, 153, 157, -40, -40, -40, -40, -40, -40, -40,
      -40, -29, -29, -29, 155, -105, 0, 156, 0, -110,
      -40, 72, 0, -110, 158, -103, 0, 0, 0, 47,
      0, 0, 0, 0, 161, 0, 165, -150, -135, 0,
      0, 0, 0, 0, 0, -56, -56, 80, 80, 80,
      80, 102, 102, 0, 0, 0, -129, 159, -71, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 189, 0, 82, 162, 167, 163, 0, -129, 0,
      -27, -111, 0, 0, 0, 0, -55, 0, -53, 0,
      0, -129, 0, 0, 0, 0, 168, 169, 0, 173,
      0, 0, 157, 174, 175, 0, 0,
  };
  final static short yyrindex[] = { 0,
      0, 0, 0, 0, 0, 631, 0, -49, 0, 0,
      0, 224, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, -59, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 482,
      410, 73, 1, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 13, 0, 0, 182, 0, 56, 0,
      0, 0, 0, 0, 0, 0, 104, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 434, 458, 97, 121, 362,
      386, 25, 49, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0,
  };
  final static short yygindex[] = { 0,
      0, 218, 0, 0, 0, 17, 164, 0, 0, 118,
      0, 0, -12, -15, 116, 92, 91, 0, 96, -155,
      37, -52, 137, 0, -16, 0, 0, 93, 0, 0,
      126, 219, 0, 0, 54, 0, 0, 0, 0, 0,
      -4, 0, 12, -25, 30, -14, 0, -134, 0, 0,
      0, 0, 0, 0, -144, 0, 212, -137, 0, 21,
      22, 23, 27, 28, 29, 0, 0, 0, 0, 0,
      0, 0,
  };
  final static int YYTABLESIZE = 911;
  static short yytable[];
  static {
    yytable();
  }

  static void yytable() {
    yytable = new short[] { 78,
        62, 51, 61, 128, 117, 129, 78, 91, 68, 65,
        78, 51, 153, 215, 94, 148, 59, 179, 118, 193,
        25, 26, 27, 201, 63, 47, 28, 29, 30, 25,
        26, 27, 25, 26, 27, 28, 29, 30, 28, 29,
        30, 62, 198, 62, 62, 62, 201, 33, 64, 207,
        124, 125, 115, 61, 206, 114, 61, 34, 209, 62,
        62, 105, 62, 35, 211, 63, 118, 63, 63, 63,
        47, 61, 56, 122, 25, 26, 27, 219, 224, 36,
        28, 29, 30, 63, 63, 225, 63, 38, 144, 64,
        189, 64, 64, 64, 115, 9, 59, 114, 39, 20,
        168, 169, 170, 171, 40, 140, 155, 64, 64, 109,
        64, 194, 195, 56, 20, 94, 56, 174, 175, 176,
        60, 62, 130, 62, 131, 62, 196, 197, 7, 42,
        43, 44, 56, 88, 56, 166, 167, 59, 134, 87,
        59, 71, 90, 132, 67, 104, 109, 63, 133, 63,
        178, 75, 76, 77, 7, 88, 59, 95, 59, 172,
        173, 60, 7, 7, 60, 7, 42, 43, 44, 96,
        88, 64, 103, 64, 68, 143, 106, 121, 135, 123,
        60, 136, 60, 137, 61, 138, 139, 141, 140, 142,
        146, 147, 151, 156, 177, 56, 185, 56, 187, 181,
        10, 190, 200, 7, 157, 208, 210, 217, 10, 218,
        10, 221, 222, 223, 51, 126, 127, 226, 227, 59,
        84, 59, 64, 1, 51, 110, 33, 149, 73, 58,
        150, 182, 10, 116, 186, 73, 184, 213, 188, 74,
        75, 76, 77, 60, 154, 60, 74, 75, 76, 77,
        74, 75, 76, 77, 152, 216, 89, 62, 62, 10,
        62, 62, 62, 62, 62, 102, 0, 62, 62, 0,
        62, 62, 62, 62, 0, 62, 62, 62, 62, 157,
        62, 63, 63, 0, 63, 63, 63, 63, 63, 191,
        0, 63, 63, 0, 63, 63, 63, 63, 0, 63,
        63, 63, 63, 0, 63, 64, 64, 0, 64, 64,
        64, 64, 64, 205, 0, 64, 64, 0, 64, 64,
        64, 64, 0, 64, 64, 64, 64, 0, 64, 56,
        56, 0, 56, 56, 56, 56, 56, 10, 9, 56,
        56, 0, 56, 56, 56, 56, 0, 56, 56, 56,
        56, 0, 56, 59, 59, 0, 59, 59, 59, 59,
        59, 57, 0, 59, 59, 0, 59, 59, 59, 59,
        0, 59, 59, 59, 59, 0, 59, 60, 60, 10,
        60, 60, 60, 60, 60, 58, 0, 60, 60, 0,
        60, 60, 60, 60, 0, 60, 60, 60, 60, 0,
        60, 0, 57, 0, 0, 57, 0, 0, 0, 53,
        42, 43, 44, 0, 88, 0, 9, 3, 46, 0,
        4, 57, 0, 57, 6, 3, 58, 3, 4, 58,
        4, 0, 6, 54, 6, 0, 8, 0, 0, 0,
        0, 0, 0, 0, 8, 58, 8, 58, 0, 3,
        53, 0, 4, 53, 0, 0, 6, 55, 9, 0,
        101, 0, 0, 0, 0, 0, 0, 0, 8, 0,
        0, 0, 1, 2, 54, 0, 3, 54, 0, 4,
        0, 52, 5, 6, 57, 0, 57, 0, 0, 0,
        7, 0, 0, 0, 0, 8, 0, 0, 55, 0,
        0, 55, 0, 0, 0, 0, 0, 0, 58, 0,
        58, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 52, 0, 0, 52, 0, 0, 0, 0,
        0, 0, 53, 0, 53, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 41, 3, 0, 54, 4, 54, 0,
        0, 6, 0, 0, 0, 0, 0, 0, 7, 42,
        43, 44, 0, 45, 0, 0, 0, 0, 0, 0,
        55, 0, 55, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 100, 3, 0, 0, 4,
        0, 0, 0, 6, 52, 0, 52, 0, 0, 0,
        7, 42, 43, 44, 0, 45, 0, 0, 57, 57,
        0, 57, 57, 57, 57, 57, 0, 0, 57, 57,
        135, 57, 57, 57, 57, 0, 57, 57, 57, 57,
        0, 57, 58, 58, 0, 58, 58, 58, 58, 58,
        0, 0, 58, 58, 0, 58, 58, 58, 58, 0,
        58, 58, 58, 58, 0, 58, 53, 53, 0, 53,
        53, 53, 53, 53, 135, 0, 53, 53, 0, 53,
        53, 0, 0, 0, 53, 53, 53, 53, 0, 53,
        54, 54, 0, 54, 54, 54, 54, 54, 0, 0,
        54, 54, 0, 54, 54, 0, 0, 0, 54, 54,
        54, 54, 0, 54, 55, 55, 0, 55, 55, 55,
        55, 55, 0, 0, 55, 55, 0, 55, 55, 0,
        0, 0, 55, 55, 55, 55, 0, 55, 52, 52,
        0, 52, 52, 52, 52, 52, 0, 0, 52, 52,
        0, 0, 0, 135, 0, 135, 52, 52, 52, 52,
        0, 52, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 135, 135, 0,
        135, 135, 135, 135, 135, 0, 0, 135, 135, 0,
        0, 0, 0, 0, 0, 135, 135, 135, 135, 0,
        135,
    };
  }

  static short yycheck[];
  static {
    yycheck();
  }

  static void yycheck() {
    yycheck = new short[] { 40,
        0, 61, 19, 60, 125, 62, 40, 41, 123, 61,
        40, 61, 125, 125, 40, 125, 44, 123, 71, 157,
        0, 0, 0, 179, 0, 9, 0, 0, 0, 9,
        9, 9, 12, 12, 12, 9, 9, 9, 12, 12,
        12, 41, 177, 43, 44, 45, 202, 280, 0, 194,
        270, 271, 68, 41, 192, 68, 44, 280, 196, 59,
        60, 66, 62, 40, 199, 41, 119, 43, 44, 45,
        54, 59, 0, 78, 54, 54, 54, 212, 223, 280,
        54, 54, 54, 59, 60, 223, 62, 264, 104, 41,
        44, 43, 44, 45, 110, 123, 0, 110, 279, 44,
        126, 127, 128, 129, 40, 59, 121, 59, 60, 256,
        62, 262, 263, 41, 59, 141, 44, 132, 133, 134,
        0, 280, 43, 123, 45, 125, 262, 263, 275, 276,
        277, 278, 60, 280, 62, 124, 125, 41, 37, 265,
        44, 123, 44, 42, 259, 40, 256, 123, 47, 125,
        256, 281, 282, 283, 275, 280, 60, 44, 62, 130,
        131, 41, 275, 275, 44, 275, 276, 277, 278, 280,
        280, 123, 44, 125, 123, 41, 280, 40, 266, 41,
        60, 58, 62, 44, 201, 41, 125, 61, 59, 44,
        59, 44, 44, 41, 40, 123, 125, 125, 41, 44,
        44, 41, 44, 275, 123, 44, 44, 263, 44, 263,
        44, 44, 44, 41, 274, 272, 273, 44, 44, 123,
        280, 125, 274, 0, 274, 44, 123, 110, 269, 12,
        115, 140, 44, 70, 144, 269, 141, 201, 146, 280,
        281, 282, 283, 123, 119, 125, 280, 281, 282, 283,
        280, 281, 282, 283, 118, 202, 38, 257, 258, 44,
        260, 261, 262, 263, 264, 54, -1, 267, 268, -1,
        270, 271, 272, 273, -1, 275, 276, 277, 278, 123,
        280, 257, 258, -1, 260, 261, 262, 263, 264, 125,
        -1, 267, 268, -1, 270, 271, 272, 273, -1, 275,
        276, 277, 278, -1, 280, 257, 258, -1, 260, 261,
        262, 263, 264, 125, -1, 267, 268, -1, 270, 271,
        272, 273, -1, 275, 276, 277, 278, -1, 280, 257,
        258, -1, 260, 261, 262, 263, 264, 44, 123, 267,
        268, -1, 270, 271, 272, 273, -1, 275, 276, 277,
        278, -1, 280, 257, 258, -1, 260, 261, 262, 263,
        264, 0, -1, 267, 268, -1, 270, 271, 272, 273,
        -1, 275, 276, 277, 278, -1, 280, 257, 258, 44,
        260, 261, 262, 263, 264, 0, -1, 267, 268, -1,
        270, 271, 272, 273, -1, 275, 276, 277, 278, -1,
        280, -1, 41, -1, -1, 44, -1, -1, -1, 0,
        276, 277, 278, -1, 280, -1, 123, 261, 125, -1,
        264, 60, -1, 62, 268, 261, 41, 261, 264, 44,
        264, -1, 268, 0, 268, -1, 280, -1, -1, -1,
        -1, -1, -1, -1, 280, 60, 280, 62, -1, 261,
        41, -1, 264, 44, -1, -1, 268, 0, 123, -1,
        125, -1, -1, -1, -1, -1, -1, -1, 280, -1,
        -1, -1, 257, 258, 41, -1, 261, 44, -1, 264,
        -1, 0, 267, 268, 123, -1, 125, -1, -1, -1,
        275, -1, -1, -1, -1, 280, -1, -1, 41, -1,
        -1, 44, -1, -1, -1, -1, -1, -1, 123, -1,
        125, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, 41, -1, -1, 44, -1, -1, -1, -1,
        -1, -1, 123, -1, 125, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, 260, 261, -1, 123, 264, 125, -1,
        -1, 268, -1, -1, -1, -1, -1, -1, 275, 276,
        277, 278, -1, 280, -1, -1, -1, -1, -1, -1,
        123, -1, 125, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, 260, 261, -1, -1, 264,
        -1, -1, -1, 268, 123, -1, 125, -1, -1, -1,
        275, 276, 277, 278, -1, 280, -1, -1, 257, 258,
        -1, 260, 261, 262, 263, 264, -1, -1, 267, 268,
        0, 270, 271, 272, 273, -1, 275, 276, 277, 278,
        -1, 280, 257, 258, -1, 260, 261, 262, 263, 264,
        -1, -1, 267, 268, -1, 270, 271, 272, 273, -1,
        275, 276, 277, 278, -1, 280, 257, 258, -1, 260,
        261, 262, 263, 264, 44, -1, 267, 268, -1, 270,
        271, -1, -1, -1, 275, 276, 277, 278, -1, 280,
        257, 258, -1, 260, 261, 262, 263, 264, -1, -1,
        267, 268, -1, 270, 271, -1, -1, -1, 275, 276,
        277, 278, -1, 280, 257, 258, -1, 260, 261, 262,
        263, 264, -1, -1, 267, 268, -1, 270, 271, -1,
        -1, -1, 275, 276, 277, 278, -1, 280, 257, 258,
        -1, 260, 261, 262, 263, 264, -1, -1, 267, 268,
        -1, -1, -1, 123, -1, 125, 275, 276, 277, 278,
        -1, 280, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, 257, 258, -1,
        260, 261, 262, 263, 264, -1, -1, 267, 268, -1,
        -1, -1, -1, -1, -1, 275, 276, 277, 278, -1,
        280,
    };
  }

  final static short YYFINAL = 11;
  final static short YYMAXTOKEN = 283;
  final static String yyname[] = {
      "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, "'%'", null, null, "'('", "')'", "'*'", "'+'",
      "','", "'-'", null, "'/'", null, null, null, null, null, null, null, null, null, null, "':'",
      "';'", "'<'", "'='", "'>'", null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, "'{'", null, "'}'", null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
      null, null, null, null, null, null, null, null, "CLASS", "INTERFACE", "IMPLEMENT",
      "RETURN", "IF", "ELSE", "END_IF", "FOR", "IN", "RANGE", "IMPL", "PRINT", "TOD",
      "EQUAL_OPERATOR", "NOT_EQUAL_OPERATOR", "GREATER_THAN_OR_EQUAL_OPERATOR",
      "LESS_THAN_OR_EQUAL_OPERATOR", "MINUS_ASSIGN", "VOID", "LONG", "UINT", "DOUBLE",
      "CADENA", "ID", "CTE_DOUBLE", "CTE_UINT", "CTE_LONG",
  };
  final static String yyrule[] = {
      "$accept : program",
      "program : type_declarations",
      "type_declarations : type_declaration",
      "type_declarations : type_declarations type_declaration",
      "type_declaration : class_declaration",
      "type_declaration : interface_declaration",
      "type_declaration : implement_for_declaration",
      "type_declaration : statement",
      "class_declaration : CLASS ID class_body",
      "class_declaration : CLASS ID interfaces class_body",
      "class_body : '{' class_body_declarations '}'",
      "class_body_declarations : class_body_declaration",
      "class_body_declarations : class_body_declarations class_body_declaration",
      "class_body_declaration : class_member_declaration",
      "class_member_declaration : field_declaration",
      "class_member_declaration : method_declaration",
      "field_declaration : type variable_declarators ','",
      "field_declaration : error ','",
      "variable_declarators : variable_declarator",
      "variable_declarators : variable_declarators ';' variable_declarator",
      "variable_declarator : variable_declarator_id",
      "variable_declarator : variable_declarator_id '=' variable_initializer",
      "variable_declarator_id : ID",
      "variable_initializer : arithmetic_operation",
      "method_declaration : method_header method_body",
      "method_header : result_type method_declarator",
      "result_type : VOID",
      "method_declarator : ID '(' formal_parameter ')'",
      "method_declarator : ID '(' ')'",
      "method_body : block",
      "method_body : ','",
      "formal_parameter : type variable_declarator_id",
      "real_parameter : arithmetic_operation",
      "interfaces : IMPLEMENT interface_type_list",
      "interface_type_list : type_name",
      "interface_type_list : interface_type_list ';' type_name",
      "interface_declaration : INTERFACE ID interface_body",
      "interface_body : '{' interface_member_declaration '}'",
      "interface_body : '{' '}' ','",
      "interface_member_declaration : interface_method_declaration",
      "interface_member_declaration : interface_member_declaration interface_method_declaration",
      "interface_method_declaration : result_type method_declarator",
      "implement_for_declaration : IMPL FOR reference_type ':' implement_for_body",
      "implement_for_declaration : IMPL FOR reference_type ':' error ','",
      "implement_for_body : '{' implement_for_body_declarations '}'",
      "implement_for_body_declarations : implement_for_body_declaration",
      "implement_for_body_declarations : implement_for_body_declarations implement_for_body_declaration",
      "implement_for_body_declaration : implement_for_method_declaration",
      "implement_for_method_declaration : method_header implement_for_method_body",
      "implement_for_method_body : method_body",
      "assignment : left_hand_side assignment_operator expression",
      "left_hand_side : ID",
      "expression : equality_expression",
      "equality_expression : relational_expression",
      "equality_expression : equality_expression EQUAL_OPERATOR relational_expression",
      "equality_expression : equality_expression NOT_EQUAL_OPERATOR relational_expression",
      "relational_expression : additive_expression",
      "relational_expression : relational_expression '<' additive_expression",
      "relational_expression : relational_expression '>' additive_expression",
      "relational_expression : relational_expression GREATER_THAN_OR_EQUAL_OPERATOR additive_expression",
      "relational_expression : relational_expression LESS_THAN_OR_EQUAL_OPERATOR additive_expression",
      "arithmetic_operation : additive_expression",
      "additive_expression : multiplicative_expression",
      "additive_expression : additive_expression '+' multiplicative_expression",
      "additive_expression : additive_expression '-' multiplicative_expression",
      "multiplicative_expression : unary_expression",
      "multiplicative_expression : TOD '(' unary_expression ')'",
      "multiplicative_expression : multiplicative_expression '*' unary_expression",
      "multiplicative_expression : multiplicative_expression '/' unary_expression",
      "multiplicative_expression : multiplicative_expression '%' unary_expression",
      "unary_expression : term",
      "unary_expression : ID",
      "term : factor",
      "term : '(' expression ')'",
      "factor : CTE_DOUBLE",
      "factor : CTE_UINT",
      "factor : CTE_LONG",
      "assignment_operator : '='",
      "assignment_operator : MINUS_ASSIGN",
      "method_invocation : ID '(' real_parameter ')' ','",
      "method_invocation : ID '(' ')' ','",
      "type : primitive_type",
      "type : reference_type",
      "primitive_type : numeric_type",
      "reference_type : ID",
      "numeric_type : integral_type",
      "numeric_type : floating_type",
      "integral_type : UINT",
      "integral_type : LONG",
      "floating_type : DOUBLE",
      "type_name : ID",
      "block : '{' block_statements RETURN ',' '}'",
      "block : '{' block_statements '}'",
      "block : '{' RETURN ',' '}'",
      "block : '{' '}'",
      "executable_block : '{' executable_block_statements '}'",
      "executable_block : '{' '}'",
      "block_statements : block_statement",
      "block_statements : block_statements block_statement",
      "executable_block_statements : executable_statament",
      "executable_block_statements : executable_block_statements executable_statament",
      "block_statement : local_variable_declaration_statement",
      "block_statement : statement",
      "executable_statament : if_then_statement",
      "executable_statament : if_then_else_statement",
      "executable_statament : for_in_range_statement",
      "executable_statament : print_statement",
      "executable_statament : expression_statement",
      "executable_statament : empty_statement",
      "local_variable_declaration_statement : local_variable_declaration ','",
      "local_variable_declaration : type variable_declarators",
      "statement : statement_without_trailing_substatement",
      "statement : if_then_statement",
      "statement : if_then_else_statement",
      "statement : for_in_range_statement",
      "statement : method_declaration",
      "statement : print_statement",
      "statement_without_trailing_substatement : block",
      "statement_without_trailing_substatement : empty_statement",
      "statement_without_trailing_substatement : expression_statement",
      "expression_statement : statement_expression",
      "statement_expression : assignment",
      "statement_expression : method_invocation",
      "empty_statement : ','",
      "if_then_statement : IF '(' expression ')' executable_block END_IF ','",
      "if_then_statement : IF '(' expression ')' executable_statament END_IF ','",
      "if_then_else_statement : IF '(' expression ')' executable_block ELSE executable_block END_IF ','",
      "if_then_else_statement : IF '(' expression ')' executable_statament ELSE executable_statament END_IF ','",
      "for_in_range_statement : FOR for_variable IN RANGE '(' for_init for_end for_update ')' executable_block ','",
      "for_in_range_statement : FOR for_variable IN RANGE '(' for_init for_end for_update ')' executable_statament ','",
      "for_variable : ID",
      "for_init : factor",
      "for_update : factor",
      "for_end : factor",
      "print_statement : PRINT CADENA ','",
      "print_statement : PRINT",
  };

  // #line 342 "grammer.y"

  private static AnalizadorLexico aLexico;

  // This method is the one where BYACC/J expects to obtain its input tokens.
  // Wrap any file/string scanning code you have in this function. This method
  // should return <0 if there is an error, and 0 when it encounters the end of
  // input. See the examples to clarify what we mean.
  int yylex() {
    int token = aLexico.generateToken();
    yyval = new ParserVal(token); // genera la referencia a la tabla de simbolos?

    return token;
  }

  // This method is expected by BYACC/J, and is used to provide error messages to
  // be directed to the channels the user desires.
  void yyerror(String msg) {
    System.out.println(msg);
  }

  private static ArrayList<String> listFilesInDirectory(String path) {
    // Obtén el directorio actual
    File element = new File(System.getProperty("user.dir") + "/" + path);
    ArrayList<String> out = new ArrayList<>();

    // Verifica si es un directorio o archivo válido
    if (element.isDirectory() || element.isFile()) {
      // Lista de archivos y directorios en el directorio actual
      File[] filesAndDirs = element.listFiles();

      // Itera a través de los archivos y directorios
      int i = 0;
      for (File fileOrDir : filesAndDirs) {
        String name = fileOrDir.getName();
        System.out.println("[" + i + "]" + ": " + name);
        out.add(name);
        i++;
      }
    } else {
      System.err.println("No es un directorio válido.");
    }

    return out;
  }

  private static String generatePath() {
    ArrayList<String> directories = listFilesInDirectory("sample_programs");
    String path = "";

    if (!directories.isEmpty()) {
      Scanner scanner = new Scanner(System.in);
      int indice = -1;

      while (indice < 0) {
        System.out.print("Ingrese el numero de carpeta a acceder: ");
        String input = scanner.nextLine();
        indice = Integer.parseInt(input);
        if (indice < directories.size() || indice >= 0) {
          path = directories.get(indice);
          directories = listFilesInDirectory("sample_programs" + "/" + path);
        } else {
          System.out.println("El indice no es correcto, ingrese nuevamente...");
          indice = -1;
        }

      }

      if (!directories.isEmpty()) {
        indice = -1;

        while (indice < 0) {

          System.out.print("Ingrese el numero de archivo binario a compilar: ");
          String input = scanner.nextLine();
          indice = Integer.parseInt(input);

          if (indice < directories.size() || indice >= 0) {
            path += "/" + directories.get(Integer.parseInt(input));
          } else {

            System.out.println("El indice no es correcto, ingrese nuevamente...");
            indice = -1;
          }

        }
      }
      scanner.close();
    }
    return path;
  }

  public static void main(String[] args) {
    System.out.println("Iniciando compilacion... ");

    String input = generatePath();

    aLexico = new AnalizadorLexico(input);

    if (!aLexico.hasReadWell()) {
      return;
    }

    Parser aSintactico = new Parser();
    /* aSintactico.run(); */

    String listaTOKENS = "";
    for (int i = 0; i < 50; i++) {
      listaTOKENS = listaTOKENS + " " + aLexico.generateToken();
      int token = -1;
      while (token != 0) {
        token = aLexico.generateToken();
        listaTOKENS = listaTOKENS + " " + token;
        System.out.println(listaTOKENS);
      }
      System.out.println(TablaSimbolos.tablaSimbolos.toString());
    }

    Logger.logError(1, "Este es un error.");
    Logger.logWarning(2, "Esta es una advertencia.");

    Logger.dumpLog();
    System.out.println(aLexico.getProgram());
  }

  // #line 736 "Parser.java"
  // ###############################################################
  // method: yylexdebug : check lexer state
  // ###############################################################
  void yylexdebug(int state, int ch) {
    String s = null;
    if (ch < 0)
      ch = 0;
    if (ch <= YYMAXTOKEN) // check index bounds
      s = yyname[ch]; // now get it
    if (s == null)
      s = "illegal-symbol";
    debug("state " + state + ", reading " + ch + " (" + s + ")");
  }

  // The following are now global, to aid in error reporting
  int yyn; // next next thing to do
  int yym; //
  int yystate; // current parsing state from state table
  String yys; // current token string

  // ###############################################################
  // method: yyparse : parse input and execute indicated items
  // ###############################################################
  int yyparse() {
    boolean doaction;
    init_stacks();
    yynerrs = 0;
    yyerrflag = 0;
    yychar = -1; // impossible char forces a read
    yystate = 0; // initial state
    state_push(yystate); // save it
    val_push(yylval); // save empty value
    while (true) // until parsing is done, either correctly, or w/error
    {
      doaction = true;
      if (yydebug)
        debug("loop");
      // #### NEXT ACTION (from reduction table)
      for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
        if (yydebug)
          debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
        if (yychar < 0) // we want a char?
        {
          yychar = yylex(); // get next token
          if (yydebug)
            debug(" next yychar:" + yychar);
          // #### ERROR CHECK ####
          if (yychar < 0) // it it didn't work/error
          {
            yychar = 0; // change it to default string (no -1!)
            if (yydebug)
              yylexdebug(yystate, yychar);
          }
        } // yychar<0
        yyn = yysindex[yystate]; // get amount to shift by (shift index)
        if ((yyn != 0) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
          if (yydebug)
            debug("state " + yystate + ", shifting to state " + yytable[yyn]);
          // #### NEXT STATE ####
          yystate = yytable[yyn];// we are in a new state
          state_push(yystate); // save it
          val_push(yylval); // push our lval as the input for next rule
          yychar = -1; // since we have 'eaten' a token, say we need another
          if (yyerrflag > 0) // have we recovered an error?
            --yyerrflag; // give ourselves credit
          doaction = false; // but don't process yet
          break; // quit the yyn=0 loop
        }

        yyn = yyrindex[yystate]; // reduce
        if ((yyn != 0) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar) { // we reduced!
          if (yydebug)
            debug("reduce");
          yyn = yytable[yyn];
          doaction = true; // get ready to execute
          break; // drop down to actions
        } else // ERROR RECOVERY
        {
          if (yyerrflag == 0) {
            yyerror("syntax error");
            yynerrs++;
          }
          if (yyerrflag < 3) // low error count?
          {
            yyerrflag = 3;
            while (true) // do until break
            {
              if (stateptr < 0) // check for under & overflow here
              {
                yyerror("stack underflow. aborting..."); // note lower case 's'
                return 1;
              }
              yyn = yysindex[state_peek(0)];
              if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                  yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                if (yydebug)
                  debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                yystate = yytable[yyn];
                state_push(yystate);
                val_push(yylval);
                doaction = false;
                break;
              } else {
                if (yydebug)
                  debug("error recovery discarding state " + state_peek(0) + " ");
                if (stateptr < 0) // check for under & overflow here
                {
                  yyerror("Stack underflow. aborting..."); // capital 'S'
                  return 1;
                }
                state_pop();
                val_pop();
              }
            }
          } else // discard this token
          {
            if (yychar == 0)
              return 1; // yyabort
            if (yydebug) {
              yys = null;
              if (yychar <= YYMAXTOKEN)
                yys = yyname[yychar];
              if (yys == null)
                yys = "illegal-symbol";
              debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
            }
            yychar = -1; // read another
          }
        } // end error recovery
      } // yyn=0 loop
      if (!doaction) // any reason not to proceed?
        continue; // skip action
      yym = yylen[yyn]; // get count of terminals on rhs
      if (yydebug)
        debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
      if (yym > 0) // if count of rhs not 'nil'
        yyval = val_peek(yym - 1); // get current semantic value
      yyval = dup_yyval(yyval); // duplicate yyval if ParserVal is used as semantic value
      switch (yyn) {
        // ########## USER-SUPPLIED ACTIONS ##########
        case 43:
        // #line 131 "grammer.y"
        {
          Logger.logError(0, "Es necesario implementar el cuerpo del metodo.");
        }
          break;
        case 92:
        // #line 252 "grammer.y"
        {
          Logger.logError(0, "Es necesario declarar el retorno del bloque.");
        }
          break;
        case 94:
        // #line 254 "grammer.y"
        {
          Logger.logError(0, "Es necesario declarar el retorno del bloque.");
        }
          break;
        case 135:
        // #line 337 "grammer.y"
        {
          Logger.logError(0, "Se esperaba una cadena.");
        }
          break;
        // #line 901 "Parser.java"
        // ########## END OF USER-SUPPLIED ACTIONS ##########
      }// switch
       // #### Now let's reduce... ####
      if (yydebug)
        debug("reduce");
      state_drop(yym); // we just reduced yylen states
      yystate = state_peek(0); // get new state
      val_drop(yym); // corresponding value drop
      yym = yylhs[yyn]; // select next TERMINAL(on lhs)
      if (yystate == 0 && yym == 0)// done? 'rest' state and at first TERMINAL
      {
        if (yydebug)
          debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
        yystate = YYFINAL; // explicitly say we're done
        state_push(YYFINAL); // and save it
        val_push(yyval); // also save the semantic value of parsing
        if (yychar < 0) // we want another character?
        {
          yychar = yylex(); // get next character
          if (yychar < 0)
            yychar = 0; // clean, if necessary
          if (yydebug)
            yylexdebug(yystate, yychar);
        }
        if (yychar == 0) // Good exit (if lex returns 0 ;-)
          break; // quit the loop--all DONE
      } // if yystate
      else // else not done yet
      { // get next state and push, for next yydefred[]
        yyn = yygindex[yym]; // find out where to go
        if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
          yystate = yytable[yyn]; // get new state
        else
          yystate = yydgoto[yym]; // else go to new defred
        if (yydebug)
          debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate + "");
        state_push(yystate); // going again, so push state & val...
        val_push(yyval); // for next action
      }
    } // main loop
    return 0;// yyaccept!!
  }
  // ## end of method parse() ######################################

  // ## run() --- for Thread #######################################
  /**
   * A default run method, used for operating this parser
   * object in the background. It is intended for extending Thread
   * or implementing Runnable. Turn off with -Jnorun .
   */
  public void run() {
    yyparse();
  }
  // ## end of method run() ########################################

  // ## Constructors ###############################################
  /**
   * Default constructor. Turn off with -Jnoconstruct .
   * 
   */
  public Parser() {
    // nothing to do
  }

  /**
   * Create a parser, setting the debug to true or false.
   * 
   * @param debugMe true for debugging, false for no debug.
   */
  public Parser(boolean debugMe) {
    yydebug = debugMe;
  }
  // ###############################################################

}
// ################### END OF CLASS ##############################
