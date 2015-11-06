package com.huwcbjones.BareBonesInterpreter;

import com.huwcbjones.BareBonesInterpreter.Lexemes.*;
import com.huwcbjones.BareBonesInterpreter.Lib.Tokens;
import com.huwcbjones.BareBonesInterpreter.Lib.Types;
import com.huwcbjones.BareBonesInterpreter.Tokens.*;
import com.huwcbjones.BareBonesInterpreter.Types.*;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Huw on 23/10/2015.
 */
public class Interpreter {
    private static final String[] _lexemesAvailable = {"EXIT", "CLEAR", "INCR", "DECR", "WHILE", "COPY", "PRNT", "SET", "ADD", "SUBTRACT", "MULTIPLY"};
    private static final String[] _typesAvailable = {"Integer", "Null"};
    private Token _currentToken;
    private Lexer _lexer;
    private HashMap<String, Object> _symbols;
    private HashMap<String, Object> _lexemes;
    private HashMap<String, Object> _types;

    public Interpreter() {
        _init();
        _lexer = new Lexer();
    }

    public Interpreter(String text) {
        text = text.replaceAll("\r\n", "\n");
        text = text.replaceAll("\r", "\n");

        _init();
        _lexer = new Lexer(text);
        try {
            Parse();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private void _init() {
        // Instantiate HashMaps
        this._lexemes = new HashMap<String, Object>();
        this._symbols = new HashMap<String, Object>();
        this._types = new HashMap<String, Object>();

        _loadTypes();
        _loadLexemes();
    }

    private void _loadTypes() {
        // Load lexemes
        ClassLoader classLoader = Interpreter.class.getClassLoader();
        Class newType;
        for (String type : _typesAvailable) {
            try {
                // Load class
                newType = classLoader.loadClass(Interpreter.class.getPackage().getName() + ".Types." + type);
                // Create new instance of lexeme
                Object typeObj = newType.newInstance();

                // Check lexeme implements Lexeme, means we can call methods from interface later to get info
                if (!(typeObj instanceof Type)) {
                    Error.Fatal("Failed to load Type({" + type + "}). Class does not implement Type");
                }

                // Put lexeme into array
                this._types.put(type, typeObj);
            } catch (Exception ex) {
                Error.Fatal("Failed to load Type({" + type + "}). ", ex);
            }
        }

    }

    private void _loadLexemes() {
        ClassLoader classLoader = Interpreter.class.getClassLoader();
        Class newLexeme;
        for (String lexeme : _lexemesAvailable) {
            try {
                // Load class
                newLexeme = classLoader.loadClass(Interpreter.class.getPackage().getName() + ".Lexemes." + lexeme);
                // Create new instance of lexeme
                Object lexemeObj = newLexeme.newInstance();

                // Check lexeme implements Lexeme, means we can call methods from interface later to get info
                if (!(lexemeObj instanceof Lexeme)) {
                    Error.Fatal("Failed to load Lexeme({" + lexeme + "}). Class does not implement Lexeme.");
                }

                // Put lexeme into array
                this._lexemes.put(lexeme, lexemeObj);
            } catch (Exception ex) {
                Error.Fatal("Failed to load Lexeme({" + lexeme + "}). ", ex);
            }
        }
    }

    private void Parse() {
        Parse(_lexer, false);
    }

    private void Parse(boolean quitOn_EOF) {
        Parse(_lexer, quitOn_EOF);
    }

    public void Parse(Lexer lexer) {
        Parse(lexer, false);
    }

    public void Parse(Lexer lexer, boolean quitOn_EOF) {
        try {
            // Get current lexeme
            Lexeme lexeme = (Lexeme) _getLexeme(lexer);

            // Check if we should exit
            if (lexeme.getLexeme().equals("EXIT")) {
                if (!quitOn_EOF) {
                    return;
                } else {
                    System.exit(0);
                }
            }

            // Instantiate arg list
            ArrayList<String> arguments = new ArrayList<String>();
            String whileLoopID = "";

            // Hacky while loop implementation
            if (lexeme.getLexeme().equals("WHILE")) {
                WHILE whileLexeme = (WHILE) lexeme;

                whileLoopID = "WHILE_LOOP@" + ((java.lang.Integer) lexeme.hashCode()).toString();
                // Add symbol to check
                arguments.add(_getSymbol(lexer));

                Token not;
                Token condition;
                not = lexer.getNextToken();
                if (not.getType().equals("CHAR") && not.getValue().equals("not")) {
                    whileLexeme.setConditionInverted(true);
                    condition = lexer.getNextToken();
                } else {
                    condition = not;
                }

                if (condition.getType().equals(Types.INTEGER)) {
                    Error.Fatal("Condition for WHILE was not an Integer.", lexer);
                }
                Symbol conditionSymbol = new Symbol(whileLoopID + "_condition", condition.getValue(), (Type) this.getType(Types.INTEGER));
                _symbols.put(conditionSymbol.getName(), conditionSymbol);
                arguments.add(conditionSymbol.getName());

                Token startLoop = lexer.getNextToken();
                if (!startLoop.getType().equals(Tokens.CHAR) || !startLoop.getValue().equals("do")) {
                    Error.Fatal("Syntax error. Condition for WHILE was not terminated.", lexer);
                }

                // Check the line has been terminated properly
                Token terminator = lexer.getNextToken();
                if (!terminator.getType().equals(Tokens.SEMICOLON)) {
                    Error.Fatal("Statement not terminated properly.", lexer);
                } else {
                    // It has, so we can skip over this token
                    //lexer.advance();
                }

                // List of tokens in while loop
                ArrayList<Token> whileLoop = new ArrayList<Token>();

                int whileLevelsDeep = 0;
                // Get current token
                Token token = lexer.getNextToken();

                // Loop while token isn't "END" lexeme
                while (!token.getValue().toString().toUpperCase().equals("END")
                        || whileLevelsDeep != 0
                        ) {
                    // Add token to ArrayList
                    if (token.getValue().toString().toUpperCase().equals("WHILE")) {
                        whileLevelsDeep++;
                    }
                    if (token.getValue().toString().toUpperCase().equals("END")) {
                        whileLevelsDeep--;
                    }
                    whileLoop.add(token);
                    // Get next token
                    token = lexer.getNextToken(true);
                }

                // Don't quite know why I have to do this, but I think it's because the lexer is still on the ;, and
                // doesn't trigger the EOF token.
                lexer.advance();

                // Loop over tokens and
                StringBuilder loopBuilder = new StringBuilder();
                for (Token newToken : whileLoop) {
                    loopBuilder.append((newToken.getValue().toString()));
                    loopBuilder.append(' ');
                }

                // Put lexer for while loop on stack
                Lexer whileLoopLexer = new Lexer(loopBuilder.toString());
                whileLoopLexer.setLineOffset(lexer.getLineNumber());
                Symbol whileLoopLexerSymbol = new Symbol(whileLoopID + "_lexer", whileLoopLexer, (Type) getType(Types.NULL));
                _symbols.put(whileLoopID, whileLoopLexerSymbol);

                // Make sure the while lexeme can find it
                arguments.add(whileLoopID);
                lexer.advance();
            } else {
                // For the number of arguments the lexeme requires, add them
                for (int i = 1; i <= lexeme.getNumberOfArgs(); i++) {
                    arguments.add(_getSymbol(lexer));
                }

                // Check the line has been terminated properly
                Token terminator = lexer.getNextToken();
                if (!terminator.getType().equals(Tokens.SEMICOLON)) {
                    Error.Fatal("Statement not terminated properly.", lexer);
                } else {
                    // It has, so we can skip over this token
                    //lexer.advance();
                }
            }

            // Add arguments to lexeme
            lexeme.addArgs(this, arguments);

            try {
                lexeme.execute();
            } catch (Exception ex) {
                Error.Fatal(ex.getMessage(), lexer);
            }

            if (lexeme.returnsResult()) {
                Symbol result = lexeme.getResult();
                _symbols.put(result.getName(), result);
            }
            Parse(lexer);
            if (!whileLoopID.equals("")) {
                this.removeSymbol(whileLoopID);
            }
        } catch (Exception ex) {
            Error.Fatal(ex.getMessage(), lexer, ex);
        }
    }

    public Object getType(String type) throws IndexOutOfBoundsException {
        try {
            if (_types.containsKey(type)) {
                return _types.get(type);
            } else {
                throw new IndexOutOfBoundsException("Type({" + type + "}) was not found.");
            }
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    public void removeSymbol(String key) {
        if (_symbols.containsKey(key)) {
            _symbols.remove(key);
        }
    }

    public Symbol getSymbol(String key) throws IndexOutOfBoundsException {
        try {
            if (_symbols.containsKey(key)) {
                return (Symbol) _symbols.get(key);
            } else {
                throw new IndexOutOfBoundsException("Symbol({" + key + "}) was not found.");
            }
        } catch (IndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    public Set getLexemes() {
        return this._lexemes.keySet();
    }

    public Object getLexeme(String lexeme) throws Exception {
        if (_lexemes.containsKey(lexeme.toUpperCase())) {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class newLexeme = classLoader.loadClass(Interpreter.class.getPackage().getName() + ".Lexemes." + lexeme.toUpperCase());
            // Create new instance of lexeme
            return newLexeme.newInstance();

        }
        throw new IndexOutOfBoundsException();
    }

    private Object _getLexeme(Lexer lexer) throws Exception {
        Token token = lexer.getNextToken();
        try {
            if (_lexemes.containsKey(token.getValue().toString().toUpperCase())) {
                ClassLoader classLoader = this.getClass().getClassLoader();
                Class newLexeme = classLoader.loadClass(Interpreter.class.getPackage().getName() + ".Lexemes." + token.getValue().toString().toUpperCase());
                // Create new instance of lexeme
                return newLexeme.newInstance();

                //return _lexemes.get(token.getValue().toString().toUpperCase());
            }

            // Override to call exit on EOF input
            if (token.getValue() == Tokens.EOF
                    && _lexemes.containsKey("EXIT")
                    ) {
                return _lexemes.get("EXIT");
            }

        } catch (Exception ex) {
            Error.Fatal(ex.getMessage());
        }
        throw new Exception("Invalid lexeme. Lexeme({" + token.getValue() + "}) not found.");
    }

    private String _getSymbol() {
        return _getSymbol(_lexer);
    }

    private String _getSymbol(Lexer lexer) {
        try {
            Token token = lexer.getNextToken();
            switch (token.getType()) {
                case Tokens.CHAR:
                    return token.getValue().toString();
                case Tokens.INTEGER:
                    Symbol newSymbol = new Symbol(((Integer) token.hashCode()).toString(), token.getValue(), (Type) this.getType(Types.INTEGER));
                    _symbols.put(((Integer) token.hashCode()).toString(), newSymbol);
                    return ((Integer) token.hashCode()).toString();
                default:
                    return "";
            }
        } catch (Exception ex) {
            Error.Fatal(ex.getMessage());
        }
        return "";
    }

    public void executeLine(String text) {
        _lexer.setText(text);
        try {
            Parse(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        _lexer.reset();
    }


}
