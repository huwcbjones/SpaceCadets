package com.huwcbjones.BareBonesInterpreter;

import com.huwcbjones.BareBonesInterpreter.Tokens.Token;
import com.huwcbjones.BareBonesInterpreter.Lib.Tokens;

import java.text.ParseException;

/**
 * Created by Huw on 23/10/2015.
 */
public class Lexer {

    private String _text;
    private int _pos = 0;
    private int _lineCharPos = 0;
    private int _linePos = 1;
    private int _lineOffset = 0;
    private boolean _isEOF = false;
    private char _currentChar;

    public Lexer() {
        this._isEOF = true;
    }

    public Lexer(String text) {
        this._text = text;
        this._currentChar = _text.charAt(_pos);
    }

    public void reset() {
        this._pos = 0;
        _linePos = 1;
        _lineCharPos = 0;
        if (_text.length() <= _pos) {
            this._isEOF = true;
        } else {
            this._isEOF = false;
            this._currentChar = _text.charAt(_pos);
        }
    }

    public void setText(String text) {
        this._text = text;
        this._isEOF = false;
        this.reset();
    }

    public void setLineOffset(int offset) {
        this._lineOffset = offset;
    }

    public int getLineNumber() {
        return _linePos + _lineOffset;
    }

    public int getLinePosition() {
        return _lineCharPos + 1;
    }

    public void advance() {
        this._pos++;
        this._lineCharPos++;
        if (this._pos > _text.length() - 1) {
            _isEOF = true;
        } else {
            _currentChar = _text.charAt(_pos);
        }
    }

    private void _skipWhitespace() {
        while (!this._isEOF && this._currentChar == ' ') {
            this.advance();
        }
    }

    private void _skipLineBreaks() {
        while (!this._isEOF && this._currentChar == '\n') {
            this._linePos++;
            this._lineCharPos = 0;
            this.advance();
        }
    }

    private int _getInteger() {
        StringBuilder integer = new StringBuilder();
        while (!this._isEOF && Lexer.isDigit(this._currentChar)) {
            integer.append(this._currentChar);
            this.advance();
        }
        return Integer.parseInt(integer.toString());
    }

    private String _getString() {
        StringBuilder str = new StringBuilder();
        while (!this._isEOF && (
                Lexer.isChar(this._currentChar)
                        || Lexer.isDigit(this._currentChar)
                        || this._currentChar == '_'
        )) {
            str.append(this._currentChar);
            this.advance();
        }
        return str.toString().toLowerCase();
    }

    public static boolean isAlpha(char test) {
        return ((Character) test).toString().matches("^[\\dA-z]$");
    }

    public static boolean isDigit(char test) {
        return ((Character) test).toString().matches("^\\d$");
    }

    public static boolean isChar(char test) {
        return ((Character) test).toString().matches("^[A-z]$");
    }

    public Token getNextToken() throws ParseException {
        return getNextToken(false);
    }

    public Token getNextToken(boolean returnEOL) throws ParseException {
        while (!this._isEOF) {
            if (this._currentChar == ' ') {
                this._skipWhitespace();
                continue;
            }
            if (this._currentChar == '\n' && !returnEOL) {
                this._skipLineBreaks();
                continue;
            } else if(this._currentChar == '\n') {
                this.advance();
                return new Token(Tokens.EOL, "\n");
            }
            if (this._currentChar == '=') {
                this.advance();
                if (this._currentChar == '=') {
                    return new Token(Tokens.ASSIGN, "==");
                } else {
                    return new Token(Tokens.EQUAL, "=");
                }
            }
            if (this._currentChar == '>') {
                this.advance();
                if (this._currentChar == '=') {
                    return new Token(Tokens.GTE, ">=");
                } else {
                    return new Token(Tokens.GT, ">");
                }
            }
            if (this._currentChar == '<') {
                this.advance();
                if (this._currentChar == '=') {
                    return new Token(Tokens.LTE, "<=");
                } else {
                    return new Token(Tokens.LT, "<");
                }
            }
            if (Lexer.isDigit(this._currentChar)) {
                return new Token(Tokens.INTEGER, this._getInteger());
            }
            if (Lexer.isChar(this._currentChar)) {
                return new Token(Tokens.CHAR, this._getString());
            }
            if (this._currentChar == ';') {
                this.advance();
                return new Token(Tokens.SEMICOLON, ";");
            }
            // Handle undefined tokens
            Token udef = new Token(Tokens.UNDEFINED, this._currentChar);
            throw new ParseException("Unknown token, " + udef.toString() + ". On line " + _linePos + ", char " + _lineCharPos, 0);
        }

        return new Token(Tokens.EOF, "EOF");
    }
}
