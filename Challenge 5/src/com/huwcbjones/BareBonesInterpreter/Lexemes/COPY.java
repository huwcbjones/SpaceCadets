package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lib.Types;
import com.huwcbjones.BareBonesInterpreter.Symbol;
import com.huwcbjones.BareBonesInterpreter.Types.Type;

import java.util.ArrayList;

/**
 * Created by Huw on 25/10/2015.
 */
public class COPY implements Lexeme {
    private final int _numberOfArgs = 3;
    private final String _lexeme = "COPY";
    private Symbol _source;
    private Symbol _destination;


    @Override
    public Symbol getResult() {
        return _destination;
    }

    @Override
    public String getLexeme() {
        return _lexeme;
    }

    @Override
    public String getHelp() {
        StringBuilder helpTextBuilder = new StringBuilder();
        helpTextBuilder.append("Usage: Copies a variables value from one to another.\n");
        helpTextBuilder.append("COPY uses the format: COPY {SRC} {DEST};\n");
        helpTextBuilder.append("Where:\n - {SRC} is the source variable.\n");
        helpTextBuilder.append("- {DEST} is the destination variable.\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() throws Exception {
        _destination = new Symbol(_destination.getName(), _source.getValue(), _source.getType());
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
        _source = interpreter.getSymbol((String) args.toArray()[0]);
        _destination = new Symbol(args.toArray()[2].toString(), 0, (Type) interpreter.getType(Types.INTEGER));
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }
}
