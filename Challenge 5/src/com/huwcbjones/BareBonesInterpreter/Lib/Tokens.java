package com.huwcbjones.BareBonesInterpreter.Lib;

/**
 * Created by Huw on 23/10/2015.
 */
public class Tokens {
    public static final String UNDEFINED = "UNDEFINED";     // Any token not defined

    public static final String EOL = "EOL";                  // End of Line (for use in whiles)
    public static final String EOF = "EOF";                 // End of File/Input
    public static final String SEMICOLON = "SEMICOLON";     // ;
    public static final String STOP = "STOP";               // .
    public static final String GT = "GT";                   // >
    public static final String GTE = "GTE";                 // >=
    public static final String LT = "LT";                   // <
    public static final String LTE = "LTE";                 // <=
    public static final String HASH = "HASH";               // #
    public static final String PLUS = "PLUS";               // +
    public static final String MINUS = "MINUS";             // -
    public static final String EQUAL = "EQUAL";             // =
    public static final String ASSIGN = "ASSIGN";           // ==

    public static final String INTEGER = "INTEGER"; // Digits
    public static final String CHAR = "CHAR"; // A char


}
