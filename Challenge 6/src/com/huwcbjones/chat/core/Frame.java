package com.huwcbjones.chat.core;

import java.io.Serializable;

/**
 * Chat Frame
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Frame implements Serializable {

    private static final long serialVersionUID = 1;

    private Type _type;
    private Object _object;

    public enum Type {
        HELLO,
        MESSAGE,
        LOBBY,
        USER,
        CLIENT,
        DISCONNECT
    }

    public Frame(Frame.Type type, Object object) {
        this._type = type;
        this._object = object;
    }

    public Object getObject() {
        return this._object;
    }

    public Type getType() {
        return this._type
    }

    public boolean isType(Type type) {
        return this._type.equals(type);
    }
}
