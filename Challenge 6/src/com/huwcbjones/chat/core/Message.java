package com.huwcbjones.chat.core;

import java.util.Calendar;
import java.util.Date;

/**
 * Message that is sent via the Protocol
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Message {

    private User _user;
    private String _message;
    private Date _timestamp;

    public Message(User user, String message){
        this._user = user;
        this._message = message;
        this._timestamp = new Date();
    }

    /**
     * Gets the message
     * @return String
     */
    public String getMessage(){
        return this._message;
    }

    /**
     * Gets user that sent the message
     * @return User
     */
    public User getUser(){
        return this._user;
    }
}
