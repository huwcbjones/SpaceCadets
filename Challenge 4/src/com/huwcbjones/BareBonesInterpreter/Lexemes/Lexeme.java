package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Symbol;

import java.util.ArrayList;

/**
 * Created by hjone on 23/10/2015.
 */
public interface Lexeme {

    void addArgs(Interpreter interpreter, ArrayList<String> args) throws IllegalArgumentException;
    int getNumberOfArgs();
    Symbol getResult();
    String getLexeme();
    String getHelp();
    void execute() throws Exception;
    boolean returnsResult();
}
