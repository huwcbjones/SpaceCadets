package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lib.Types;
import com.huwcbjones.BareBonesInterpreter.Symbol;
import com.huwcbjones.BareBonesInterpreter.Types.Type;

import java.util.ArrayList;

/**
 * Created by Huw on 25/10/2015.
 */
public class SUBTRACT implements Lexeme {
    private final int _numberOfArgs = 3;
    private final String _lexeme = "SUBTRACT";
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
        helpTextBuilder.append("Usage: Subtracts the first integer from the second and stores the result in another variable.\n");
        helpTextBuilder.append("       The destination variable does not need to be cleared.\n");
        helpTextBuilder.append("       Throws error if result is less than 0.\n");
        helpTextBuilder.append("SUBTRACT uses the format: SUBTRACT {ARG1} {ARG2} {DEST};\n");
        helpTextBuilder.append("Where:\n - {ARG1}/{ARG2} are variables/integers.\n");
        helpTextBuilder.append(" - {DEST} is the where the result will be stored.\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() throws Exception{
        int result = (int)_arg1.getValue() - (int)_arg2.getValue();
        if(result < 0){
            throw new Exception("SUBTRACT produced a result less than 0.");
        }
        _result.setValue(result);
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
            Type type =(Type) interpreter.getType(Types.INTEGER);
            _result = new Symbol((String) args.toArray()[2], 0, type);
        }
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }
}
