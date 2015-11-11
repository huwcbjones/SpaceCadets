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
        MOTD,
        OK,
        HELP,
        COMMAND,
        MESSAGE,
        P_MESSAGE,
        LOBBY_GET,
        LOBBY_CHANGE,
        LOBBY_ADD,
        LOBBY_REMOVE,
        USER,
        CLIENT_GET,
        CLIENT_SEND,
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
        return this._type;
    }

    public boolean isType(Type type) {
        return this._type.equals(type);
    }
}
