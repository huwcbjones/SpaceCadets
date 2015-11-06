package com.huwcbjones.BareBonesInterpreter.Tokens;

/**
 * Created by Huw on 23/10/2015.
 */
public class Token {
    private String _type;
    private Object _value;

    public Token(String type, Object value){
        this._type = type;
        this._value = value;
    }

    public String getType(){
        return _type;
    }

    public Object getValue(){
        return _value;
    }

    public boolean equals(Token compare){
        return (compare.getType().equals(_type) && compare.getValue().equals(_value));
    }

    @Override
    public String toString(){
        return "Token({" + _type + "}, {" + _value.toString() + "})";
    }


}
