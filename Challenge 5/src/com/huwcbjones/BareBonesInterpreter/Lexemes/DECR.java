package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lib.Types;
import com.huwcbjones.BareBonesInterpreter.Symbol;
import com.huwcbjones.BareBonesInterpreter.Types.Type;

import java.util.ArrayList;

import com.huwcbjones.BareBonesInterpreter.Error;

/**
 * Created by Huw on 25/10/2015.
 */
public class DECR implements Lexeme {
    private final int _numberOfArgs = 1;
    private final String _lexeme = "DECR";
    private Symbol _arg;


    @Override
    public Symbol getResult() {
        return null;
    }

    @Override
    public String getLexeme() {
        return _lexeme;
    }

    @Override
    public String getHelp() {
        StringBuilder helpTextBuilder = new StringBuilder();
        helpTextBuilder.append("Usage: Decrements a variable. (Note DECR throws error if variable is < 0)\n");
        helpTextBuilder.append("DECR uses the format: DECR {VAR};\n");
        helpTextBuilder.append("Where:\n - {VAR} is the variable to decrement.\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() throws Exception {
        int argValue = (int) _arg.getValue();
        if (argValue == 0) {
            throw new Exception("DECR cannot decrement variable \"" + _arg.getName() + "\" below 0.");
        }
        argValue--;
        _arg.setValue(argValue);
    }

    @Override
    public boolean returnsResult() {
        return false;
    }

    @Override
    public void addArgs(Interpreter interpreter, ArrayList<String> args) throws IllegalArgumentException {
        if (args.size() != _numberOfArgs) {
            throw new IllegalArgumentException();
        }
        String name = (String) args.toArray()[0];
        try {
            _arg = interpreter.getSymbol(name);
            if (_arg.getType() != interpreter.getType(Types.INTEGER)) {
                throw new IllegalArgumentException("Argument provided was of type \"" + _arg.getType().toString() + "\"");
            }
        } catch (Exception ex) {
            Type type = (Type) interpreter.getType(Types.INTEGER);
            _arg = new Symbol(name, 0, type);
        }
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }
}
