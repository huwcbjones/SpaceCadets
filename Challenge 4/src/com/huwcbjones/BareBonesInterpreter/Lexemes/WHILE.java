package com.huwcbjones.BareBonesInterpreter.Lexemes;

import com.huwcbjones.BareBonesInterpreter.Interpreter;
import com.huwcbjones.BareBonesInterpreter.Lexer;
import com.huwcbjones.BareBonesInterpreter.Symbol;

import java.util.ArrayList;

/**
 * Created by hjone on 23/10/2015.
 */
public class WHILE implements Lexeme {
    private final int _numberOfArgs = 3;
    private final String _lexeme = "WHILE";
    private Interpreter _interpreter;
    private String _lexerName;
    private Lexer _lexer;
    private Symbol _symbol;
    private Symbol _condition;
    private boolean _isConditionInverted = false;


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
        helpTextBuilder.append("Usage: Repeat a section of code until the condition evaluates to false.\n");
        helpTextBuilder.append("WHILE uses the format: WHILE {VAR} {not} {int} DO;\n");
        helpTextBuilder.append(" [.... CODE TO EXECUTE ....]\n");
        helpTextBuilder.append("END;\n");
        helpTextBuilder.append("Where:\n - {VAR} is the variable to compare.\n");
        helpTextBuilder.append(" - {not} is an optional modifier.\n");
        helpTextBuilder.append(" - {int} Represents a non-negative integer (natural number).\n");
        return helpTextBuilder.toString();
    }

    @Override
    public void execute() {
        while (_evaluateCondition()) {
            _interpreter.Parse(_lexer);
            _lexer.reset();
        }
        _interpreter.removeSymbol(_condition.getName());
        _interpreter.removeSymbol(_lexerName);
    }

    private boolean _evaluateCondition() {
        int condition = (int) (_condition.getValue());
        int comparison = (int) (_symbol.getValue());
        if (_isConditionInverted) {
            return condition != comparison;
        } else {
            return condition == comparison;
        }
    }


    @Override
    public boolean returnsResult() {
        return false;
    }

    @Override
    public void addArgs(Interpreter interpreter, ArrayList<String> args) throws IllegalArgumentException {
        // +1 because we want new lexer arg too
        if (args.size() != _numberOfArgs) {
            throw new IllegalArgumentException();
        }
        _interpreter = interpreter;

        try {
            String symbolName = (String) args.toArray()[0];
            String conditionName = (String) args.toArray()[1];
            _lexerName = (String) args.toArray()[2];

            _symbol = interpreter.getSymbol(symbolName);
            _condition = interpreter.getSymbol(conditionName);
            Symbol lexerSymbol = interpreter.getSymbol(_lexerName);
            _lexer = (Lexer) (lexerSymbol.getValue());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Variable was not initialised before passed to WHILE.");
        }
    }

    public void setConditionInverted(boolean isInverted) {
        _isConditionInverted = isInverted;
    }

    @Override
    public int getNumberOfArgs() {
        return _numberOfArgs;
    }
}
