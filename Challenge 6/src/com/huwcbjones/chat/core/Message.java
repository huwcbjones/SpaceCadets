package com.huwcbjones.chat.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Message that is sent via the Frame
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1;

    private int _clientID;
    private int _lobbyID;
    private String _message;
    private Date _timestamp;

    public Message(int clientID, int lobbyID, String message) {
        this._clientID = clientID;
        this._lobbyID = lobbyID;
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
     * Gets lobby that the message has been sent to
     *
     * @return LobbyID
     */
    public int getLobbyID() {
        return this._lobbyID;
    }

    /**
     * Gets user that sent the message
     *
     * @return Client
     */
    public int getClientID() {
        return this._clientID;
    }

    /**
     * Gets the time the message was processed by the server
     * @return Timestamp
     */
    public Date getTimestamp() {
        return this._timestamp;
    }
}
