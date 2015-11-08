package com.huwcbjones.chat.core;

import java.io.Serializable;

/**
 * Chat Frame
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Frame implements Serializable {

    private Type _type;
    private Object _object;

    public enum Type {
        MESSAGE,
        DESTINATION,
        USER,
        CLIENT,
        HELLO
    }

    public Frame(Type type, Object object) {
        this._type = type;
        this._object = object;
    }

    public Object getObject() {
        return this._object;
    }

    public Type getType() {
        return this._type;
    }

    public boolean isType(Type type){
        return type.equals(_type);
    }
}
