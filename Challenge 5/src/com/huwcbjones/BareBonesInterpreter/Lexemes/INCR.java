package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lib.Types;
import com.huwcbjones.BareBonesInterpreter.Symbol;
import com.huwcbjones.BareBonesInterpreter.Types.Type;

import java.util.ArrayList;

/**
 *
 * INCR
 *
 * @author Huw Jones
 * @since 25/10/2015
 */
public class INCR implements Lexeme {
    private final int _numberOfArgs = 1;
    private final String _lexeme = "INCR";
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
        helpTextBuilder.append("Usage: Increments a variable.\n");
        helpTextBuilder.append("INCR uses the format: INCR {VAR};\n");
        helpTextBuilder.append("Where:\n - {VAR} is the variable to increment.\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() throws Exception {
        int argValue = (int) _arg.getValue();
        argValue++;
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
        _arg = interpreter.getSymbol(name);
        if (_arg.getType() != interpreter.getType(Types.INTEGER)) {
            throw new IllegalArgumentException("Argument provided was of type \"" + _arg.getType().toString() + "\"");
        }
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }
}
