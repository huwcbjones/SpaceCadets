package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Symbol;

import java.util.ArrayList;

/**
 * Created by Huw on 25/10/2015.
 */
public class EXIT implements Lexeme {

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
        return "EXIT";
    }

    @Override
    public String getHelp() {
        StringBuilder helpTextBuilder = new StringBuilder();
        helpTextBuilder.append("Usage: Exits the script.\n");
        helpTextBuilder.append("EXIT uses the format: EXIT;\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean returnsResult() {
        return false;
    }
}
