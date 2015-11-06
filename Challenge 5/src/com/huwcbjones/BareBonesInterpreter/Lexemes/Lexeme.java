package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Symbol;

import java.util.ArrayList;

/**
 *
 * Lexeme Interface
 *
 * @author Huw Jones
 * @since 23/10/2015
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
