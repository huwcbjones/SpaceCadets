package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lib.Types;
import com.huwcbjones.BareBonesInterpreter.Symbol;
import com.huwcbjones.BareBonesInterpreter.Types.Type;

import java.util.ArrayList;

/**
 * Created by hjone on 25/10/2015.
 */
public class SET implements Lexeme {
    private final int _numberOfArgs = 2;
    private Symbol _arg;

    @Override
    public void addArgs(Interpreter interpreter, ArrayList<String> args) throws IllegalArgumentException {
        if (args.size() != _numberOfArgs) {
            throw new IllegalArgumentException();
        }
        String name = (String) args.toArray()[0];
        String value = (String) args.toArray()[1];
        _arg = new Symbol(name, interpreter.getSymbol(value).getValue(), (Type)interpreter.getType(Types.INTEGER));
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }

    @Override
    public Symbol getResult() {
        return _arg;
    }

    @Override
    public String getLexeme() {
        return "SET";
    }

    @Override
    public String getHelp() {
        StringBuilder helpTextBuilder = new StringBuilder();
        helpTextBuilder.append("Usage: Sets a variable to a value.\n");
        helpTextBuilder.append("SET uses the format: SET {VAR} {INT};\n");
        helpTextBuilder.append("Where:\n - {VAR} is the variable to set.\n");
        helpTextBuilder.append(" - {INT} is the non-zero integer value to set.\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean returnsResult() {
        return true;
    }
}
