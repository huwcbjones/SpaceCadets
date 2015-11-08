package com.huwcbjones.chat.core;

import java.util.Date;

/**
 * Message that is sent via the Frame
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Message {

    private Client _client;
    private String _message;
    private Date _timestamp;

    public Message(Client client, String message) {
        this._client = client;
        this._message = message;
        this._timestamp = new Date();
    }

    /**
     * Gets the message
     *
     * @return String
     */
    public String getMessage() {
        return this._message;
    }

    /**
     * Gets user that sent the message
     *
     * @return ChatClient
     */
    public Client getUser() {
        return this._client;
    }

    /**
     * Gets the time the message was processed by the server
     * @return Timestamp
     */
    public Date getTimestamp() {
        return this._timestamp;
    }
}
