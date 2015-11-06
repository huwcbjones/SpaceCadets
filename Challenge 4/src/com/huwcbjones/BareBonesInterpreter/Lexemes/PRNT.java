package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Symbol;

import java.util.ArrayList;

/**
 * Created by hjone on 25/10/2015.
 */
public class PRNT implements Lexeme {
    private final int _numberOfArgs = 1;
    private Symbol _output;

    @Override
    public void addArgs(Interpreter interpreter, ArrayList<String> args) throws IllegalArgumentException {
        if (args.size() != _numberOfArgs) {
            throw new IllegalArgumentException();
        }
        _output = interpreter.getSymbol((String) args.toArray()[0]);
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }

    @Override
    public Symbol getResult() {
        return null;
    }

    @Override
    public String getLexeme() {
        return "PRNT";
    }

    @Override
    public String getHelp() {
        StringBuilder helpTextBuilder = new StringBuilder();
        helpTextBuilder.append("Usage: Prints a variable to console.\n");
        helpTextBuilder.append("PRNT uses the format: PRNT {VAR};\n");
        helpTextBuilder.append("Where:\n - {VAR} is the variable to print.\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() throws Exception {
        System.out.println(_output.getValue().toString());
    }

    @Override
    public boolean returnsResult() {
        return false;
    }
}
