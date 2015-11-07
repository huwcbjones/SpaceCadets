package com.huwcbjones.chat.core;

/**
 * Chat Frame
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Frame {

    private Type _type;
    private Object _object;

    public enum Type {
        MESSAGE,
        DESTINATION
    }

    public Frame(Type type, Object object) {
        this._type = type;
        this._object = object;
    }

    public Object getObject() {
        return this._object;
    }

    public Type getType() {
        return this.getType();
    }
}
