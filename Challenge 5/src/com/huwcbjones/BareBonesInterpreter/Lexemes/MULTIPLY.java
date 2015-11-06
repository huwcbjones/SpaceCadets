package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lib.Types;
import com.huwcbjones.BareBonesInterpreter.Symbol;
import com.huwcbjones.BareBonesInterpreter.Types.Type;

import java.util.ArrayList;

/**
 * Created by Huw on 25/10/2015.
 */
public class MULTIPLY implements Lexeme {
    private final int _numberOfArgs = 3;
    private final String _lexeme = "MULTIPLY";
    private Symbol _arg1;
    private Symbol _arg2;
    private Symbol _result;


    @Override
    public Symbol getResult() {
        return _result;
    }

    @Override
    public String getLexeme() {
        return _lexeme;
    }

    @Override
    public String getHelp() {
        StringBuilder helpTextBuilder = new StringBuilder();
        helpTextBuilder.append("Usage: Multiplies two integers and stores the result in variable.\n");
        helpTextBuilder.append("       The destination variable does not need to be cleared.\n");
        helpTextBuilder.append("MULTIPLY uses the format: MULTIPLY {ARG1} {ARG2} {DEST};\n");
        helpTextBuilder.append("Where:\n - {ARG1}/{ARG2} are variables/integers.\n");
        helpTextBuilder.append(" - {DEST} is the where the result will be stored.\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() throws Exception {
        _result.setValue((int) _arg1.getValue() * (int) _arg2.getValue());
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
        _arg1 = interpreter.getSymbol((String) args.toArray()[0]);
        _arg2 = interpreter.getSymbol((String) args.toArray()[1]);
        try {
            _result = interpreter.getSymbol((String) args.toArray()[2]);
        } catch (Exception ex) {
            Type type = (Type) interpreter.getType(Types.INTEGER);
            _result = new Symbol((String) args.toArray()[2], 0, type);
        }
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }
}
