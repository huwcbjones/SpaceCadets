package com.huwcbjones.BareBonesInterpreter;

import com.huwcbjones.BareBonesInterpreter.Types.Type;

/**
 * Created by hjone on 23/10/2015.
 */
public class Symbol {

    private String _name;
    private Object _value;
    private Type _type;

    public Symbol(String name, Object value, Type type) {
        this._name = name;
        this._value = value;
        this._type = type;
    }

    public String getName() {
        return _name;
    }
    public Object getValue() {
        return _value;
    }

    public Type getType() {
        return _type;
    }

    public void setValue(Object value){
        this._value = value;
    }

    @Override
    public String toString() {
        return "Symbol({" + _name + "}, {" + _type.toString() + "}, {" + _value.toString() + "})";
    }
}
