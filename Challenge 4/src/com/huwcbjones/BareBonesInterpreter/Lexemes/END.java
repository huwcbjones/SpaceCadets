package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Symbol;

import java.util.ArrayList;

/**
 * Created by Huw on 25/10/2015.
 */
public class END implements Lexeme {
    @Override
    public void addArgs(Interpreter interpreter, ArrayList<String> args) throws IllegalArgumentException {

    }

    @Override
    public int getNumberOfArgs() {
        return 0;
    }

    @Override
    public Symbol getResult() {
        return null;
    }

    @Override
    public String getLexeme() {
        return null;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean returnsResult() {
        return false;
    }
}
