package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lib.Types;
import com.huwcbjones.BareBonesInterpreter.Symbol;
import com.huwcbjones.BareBonesInterpreter.Types.Type;

import java.lang.IllegalArgumentException;

import java.util.ArrayList;

/**
 * Created by hjone on 23/10/2015.
 */
public class CLEAR implements Lexeme {
    private final int _numberOfArgs = 1;
    private final String _lexeme = "CLEAR";
    private Symbol _arg;


    @Override
    public Symbol getResult() {
        return _arg;
    }

    @Override
    public String getLexeme() {
        return _lexeme;
    }

    @Override
    public String getHelp() {
        StringBuilder helpTextBuilder = new StringBuilder();
        helpTextBuilder.append("Usage: Sets a variable to 0.\n");
        helpTextBuilder.append("CLEAR uses the format: CLEAR {VAR};\n");
        helpTextBuilder.append("Where:\n - {VAR} is the variable to clear.\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() throws Exception {
        _arg.setValue(0);
    }

    @Override
    public boolean returnsResult() {
        return true;
    }

    @Override
    public void addArgs(Interpreter interpreter, ArrayList<String> args) throws IllegalArgumentException {
        if (args.size() != _numberOfArgs) {
            throw new IllegalArgumentException();
        }
        String name = (String) args.toArray()[0];
        try {
            _arg = interpreter.getSymbol(name);
        } catch (Exception ex) {
            Type type =(Type) interpreter.getType(Types.INTEGER);
            _arg = new Symbol(name, 0, type);
        }
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }

}
